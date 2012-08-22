package org.ruboto;

import java.io.IOException;

import org.ruboto.Script;

import android.app.ProgressDialog;
import android.os.Bundle;

public class RubotoActivity extends android.app.Activity {
  public static final int CB_ACTIVITY_RESULT = 0;
  public static final int CB_CHILD_TITLE_CHANGED = 1;
  public static final int CB_CONFIGURATION_CHANGED = 2;
  public static final int CB_CONTENT_CHANGED = 3;
  public static final int CB_CONTEXT_ITEM_SELECTED = 4;
  public static final int CB_CONTEXT_MENU_CLOSED = 5;
  public static final int CB_CREATE_CONTEXT_MENU = 6;
  public static final int CB_CREATE_DESCRIPTION = 7;
  public static final int CB_CREATE_OPTIONS_MENU = 8;
  public static final int CB_CREATE_PANEL_MENU = 9;
  public static final int CB_CREATE_PANEL_VIEW = 10;
  public static final int CB_CREATE_THUMBNAIL = 11;
  public static final int CB_CREATE_VIEW = 12;
  public static final int CB_DESTROY = 13;
  public static final int CB_KEY_DOWN = 14;
  public static final int CB_KEY_MULTIPLE = 15;
  public static final int CB_KEY_UP = 16;
  public static final int CB_LOW_MEMORY = 17;
  public static final int CB_MENU_ITEM_SELECTED = 18;
  public static final int CB_MENU_OPENED = 19;
  public static final int CB_NEW_INTENT = 20;
  public static final int CB_OPTIONS_ITEM_SELECTED = 21;
  public static final int CB_OPTIONS_MENU_CLOSED = 22;
  public static final int CB_PANEL_CLOSED = 23;
  public static final int CB_PAUSE = 24;
  public static final int CB_POST_CREATE = 25;
  public static final int CB_POST_RESUME = 26;
  public static final int CB_PREPARE_OPTIONS_MENU = 27;
  public static final int CB_PREPARE_PANEL = 28;
  public static final int CB_RESTART = 29;
  public static final int CB_RESTORE_INSTANCE_STATE = 30;
  public static final int CB_RESUME = 31;
  public static final int CB_SAVE_INSTANCE_STATE = 32;
  public static final int CB_SEARCH_REQUESTED = 33;
  public static final int CB_START = 34;
  public static final int CB_STOP = 35;
  public static final int CB_TITLE_CHANGED = 36;
  public static final int CB_TOUCH_EVENT = 37;
  public static final int CB_TRACKBALL_EVENT = 38;
  public static final int CB_WINDOW_ATTRIBUTES_CHANGED = 39;
  public static final int CB_WINDOW_FOCUS_CHANGED = 40;
  public static final int CB_USER_INTERACTION = 41;
  public static final int CB_USER_LEAVE_HINT = 42;
  public static final int CB_ATTACHED_TO_WINDOW = 43;
  public static final int CB_BACK_PRESSED = 44;
  public static final int CB_DETACHED_FROM_WINDOW = 45;
  public static final int CB_KEY_LONG_PRESS = 46;
  public static final int CB_APPLY_THEME_RESOURCE = 47;

    private String rubyClassName;
    private String scriptName;
    private Object rubyInstance;
    private Object[] callbackProcs = new Object[48];
    private String remoteVariable = null;
    private Object[] args;
    private Bundle configBundle = null;

    public void setCallbackProc(int id, Object obj) {
        callbackProcs[id] = obj;
    }
	
    public RubotoActivity setRemoteVariable(String var) {
        remoteVariable = var;
        return this;
    }

    public String getRemoteVariableCall(String call) {
        return (remoteVariable == null ? "" : (remoteVariable + ".")) + call;
    }

    public void setRubyClassName(String name) {
        rubyClassName = name;
    }

    public void setScriptName(String name) {
        scriptName = name;
    }

    /****************************************************************************************
     *
     *  Activity Lifecycle: onCreate
     */
	
    @Override
    public void onCreate(Bundle bundle) {
        System.out.println("RubotoActivity onCreate(): " + getClass().getName());
        args = new Object[1];
        args[0] = bundle;

        configBundle = getIntent().getBundleExtra("RubotoActivity Config");

        if (configBundle != null) {
            if (configBundle.containsKey("Theme")) {
                setTheme(configBundle.getInt("Theme"));
            }
            if (configBundle.containsKey("ClassName")) {
                if (this.getClass().getName() == RubotoActivity.class.getName()) {
                    setRubyClassName(configBundle.getString("ClassName"));
                } else {
                    throw new IllegalArgumentException("Only local Intents may set class name.");
                }
            }
            if (configBundle.containsKey("Script")) {
                if (this.getClass().getName() == RubotoActivity.class.getName()) {
                    setScriptName(configBundle.getString("Script"));
                } else {
                    throw new IllegalArgumentException("Only local Intents may set script name.");
                }
            }
        }

        if (rubyClassName == null && scriptName != null) {
            rubyClassName = Script.toCamelCase(scriptName);
        }
        if (scriptName == null && rubyClassName != null) {
            setScriptName(Script.toSnakeCase(rubyClassName) + ".rb");
        }

        super.onCreate(bundle);

        if (JRubyAdapter.isInitialized()) {
            prepareJRuby();
    	    loadScript();
        }
    }

    // TODO(uwe):  Only needed for non-class-based definitions
    // Can be removed if we stop supporting non-class-based definitions
    // This causes JRuby to initialize and takes a while.
    protected void prepareJRuby() {
    	JRubyAdapter.put("$context", this);
    	JRubyAdapter.put("$activity", this);
    	JRubyAdapter.put("$bundle", args[0]);
    }
    // TODO end

    protected void loadScript() {
        try {
            if (scriptName != null) {
                System.out.println("Looking for Ruby class: " + rubyClassName);
                Object rubyClass = JRubyAdapter.get(rubyClassName);
                Script rubyScript = new Script(scriptName);
                if (rubyScript.exists()) {
                    String script = rubyScript.getContents();
                    if (script.matches("(?s).*class " + rubyClassName + ".*")) {
                        if (!rubyClassName.equals(getClass().getSimpleName())) {
                            System.out.println("Script defines methods on meta class");
                            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
                            if (JRubyAdapter.isJRubyPreOneSeven() || JRubyAdapter.isRubyOneEight()) {
                                JRubyAdapter.put("$java_instance", this);
                                JRubyAdapter.put(rubyClassName, JRubyAdapter.runScriptlet("class << $java_instance; self; end"));
                            } else if (JRubyAdapter.isJRubyOneSeven() && JRubyAdapter.isRubyOneNine()) {
                                JRubyAdapter.runScriptlet("Java::" + getClass().getName() + ".__persistent__ = true");
                                JRubyAdapter.put(rubyClassName, JRubyAdapter.runRubyMethod(this, "singleton_class"));
                            } else {
                                throw new RuntimeException("Unknown JRuby/Ruby version: " + JRubyAdapter.get("JRUBY_VERSION") + "/" + JRubyAdapter.get("RUBY_VERSION"));
                            }
                        }
                    }
                    if (rubyClass == null) {
                        System.out.println("Loading script: " + scriptName);
                        if (script.matches("(?s).*class " + rubyClassName + ".*")) {
                            System.out.println("Script contains class definition");
                            if (rubyClassName.equals(getClass().getSimpleName())) {
                                System.out.println("Script has separate Java class");
                                // FIXME(uwe): Simplify when we stop support for JRuby < 1.7.0
                                if (!JRubyAdapter.isJRubyPreOneSeven()) {
                                    JRubyAdapter.runScriptlet("Java::" + getClass().getName() + ".__persistent__ = true");
                                }
                                JRubyAdapter.put(rubyClassName, JRubyAdapter.runScriptlet("Java::" + getClass().getName()));
                            }
                            System.out.println("Set class: " + JRubyAdapter.get(rubyClassName));
                        }
                        JRubyAdapter.setScriptFilename(scriptName);
                        JRubyAdapter.runScriptlet(script);
                        rubyClass = JRubyAdapter.get(rubyClassName);
                    }
                    rubyInstance = this;
                } else if (rubyClass != null) {
                    // We have a predefined Ruby class without corresponding Ruby source file.
                    System.out.println("Create separate Ruby instance for class: " + rubyClass);
                    rubyInstance = JRubyAdapter.runRubyMethod(rubyClass, "new");
                    JRubyAdapter.runRubyMethod(rubyInstance, "instance_variable_set", "@ruboto_java_instance", this);
                } else {
                    // Neither script file nor predefined class
                    throw new RuntimeException("Either script or predefined class must be present.");
                }
                if (rubyClass != null) {
                    System.out.println("Call on_create on: " + rubyInstance + ", " + JRubyAdapter.get("JRUBY_VERSION"));
                    // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
                    if (JRubyAdapter.isJRubyPreOneSeven()) {
                        JRubyAdapter.put("$ruby_instance", rubyInstance);
                        JRubyAdapter.runScriptlet("$ruby_instance.on_create($bundle)");
                    } else if (JRubyAdapter.isJRubyOneSeven()) {
                        JRubyAdapter.runRubyMethod(rubyInstance, "on_create", args[0]);
                    } else {
                        throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
                    }
                }
            } else if (configBundle != null) {
                // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
                if (JRubyAdapter.isJRubyPreOneSeven()) {
            	    JRubyAdapter.runScriptlet("$activity.initialize_ruboto");
            	    JRubyAdapter.runScriptlet("$activity.on_create($bundle)");
                } else if (JRubyAdapter.isJRubyOneSeven()) {
            	    JRubyAdapter.runRubyMethod(this, "initialize_ruboto");
                    JRubyAdapter.runRubyMethod(this, "on_create", args[0]);
                } else {
                    throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            	}
            }
        } catch(IOException e){
            e.printStackTrace();
            ProgressDialog.show(this, "Script failed", "Something bad happened", true, true);
        }
    }

    public boolean rubotoAttachable() {
      return true;
    }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_ACTIVITY_RESULT] != null) {
        super.onActivityResult(requestCode, resultCode, data);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_ACTIVITY_RESULT], "call" , new Object[]{requestCode, resultCode, data});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_activity_result}")) {
          super.onActivityResult(requestCode, resultCode, data);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_requestCode", requestCode);
            JRubyAdapter.put("$arg_resultCode", resultCode);
            JRubyAdapter.put("$arg_data", data);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_activity_result($arg_requestCode, $arg_resultCode, $arg_data)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_activity_result", new Object[]{requestCode, resultCode, data});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onActivityResult}")) {
            super.onActivityResult(requestCode, resultCode, data);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_requestCode", requestCode);
              JRubyAdapter.put("$arg_resultCode", resultCode);
              JRubyAdapter.put("$arg_data", data);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onActivityResult($arg_requestCode, $arg_resultCode, $arg_data)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onActivityResult", new Object[]{requestCode, resultCode, data});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onActivityResult(requestCode, resultCode, data);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onActivityResult");
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  public void onChildTitleChanged(android.app.Activity childActivity, java.lang.CharSequence title) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CHILD_TITLE_CHANGED] != null) {
        super.onChildTitleChanged(childActivity, title);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_CHILD_TITLE_CHANGED], "call" , new Object[]{childActivity, title});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_child_title_changed}")) {
          super.onChildTitleChanged(childActivity, title);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_childActivity", childActivity);
            JRubyAdapter.put("$arg_title", title);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_child_title_changed($arg_childActivity, $arg_title)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_child_title_changed", new Object[]{childActivity, title});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onChildTitleChanged}")) {
            super.onChildTitleChanged(childActivity, title);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_childActivity", childActivity);
              JRubyAdapter.put("$arg_title", title);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onChildTitleChanged($arg_childActivity, $arg_title)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onChildTitleChanged", new Object[]{childActivity, title});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onChildTitleChanged(childActivity, title);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onChildTitleChanged");
      super.onChildTitleChanged(childActivity, title);
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
        super.onConfigurationChanged(newConfig);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_configuration_changed}")) {
          super.onConfigurationChanged(newConfig);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_newConfig", newConfig);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_configuration_changed($arg_newConfig)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_configuration_changed", newConfig);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onConfigurationChanged}")) {
            super.onConfigurationChanged(newConfig);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_newConfig", newConfig);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onConfigurationChanged($arg_newConfig)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onConfigurationChanged", newConfig);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onConfigurationChanged(newConfig);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onConfigurationChanged");
      super.onConfigurationChanged(newConfig);
    }
  }

  public void onContentChanged() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CONTENT_CHANGED] != null) {
        super.onContentChanged();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_CONTENT_CHANGED], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_content_changed}")) {
          super.onContentChanged();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_content_changed()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_content_changed");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContentChanged}")) {
            super.onContentChanged();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onContentChanged()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onContentChanged");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onContentChanged();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onContentChanged");
      super.onContentChanged();
    }
  }

  public boolean onContextItemSelected(android.view.MenuItem item) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CONTEXT_ITEM_SELECTED] != null) {
        super.onContextItemSelected(item);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_CONTEXT_ITEM_SELECTED], "call" , item);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_context_item_selected}")) {
          super.onContextItemSelected(item);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_item", item);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_context_item_selected($arg_item)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_context_item_selected", item);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContextItemSelected}")) {
            super.onContextItemSelected(item);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_item", item);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onContextItemSelected($arg_item)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onContextItemSelected", item);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onContextItemSelected(item);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onContextItemSelected");
      return super.onContextItemSelected(item);
    }
  }

  public void onContextMenuClosed(android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CONTEXT_MENU_CLOSED] != null) {
        super.onContextMenuClosed(menu);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_CONTEXT_MENU_CLOSED], "call" , menu);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_context_menu_closed}")) {
          super.onContextMenuClosed(menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_context_menu_closed($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_context_menu_closed", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContextMenuClosed}")) {
            super.onContextMenuClosed(menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onContextMenuClosed($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onContextMenuClosed", menu);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onContextMenuClosed(menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onContextMenuClosed");
      super.onContextMenuClosed(menu);
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
        super.onCreateContextMenu(menu, v, menuInfo);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , new Object[]{menu, v, menuInfo});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_context_menu}")) {
          super.onCreateContextMenu(menu, v, menuInfo);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$arg_v", v);
            JRubyAdapter.put("$arg_menuInfo", menuInfo);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_create_context_menu($arg_menu, $arg_v, $arg_menuInfo)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_create_context_menu", new Object[]{menu, v, menuInfo});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateContextMenu}")) {
            super.onCreateContextMenu(menu, v, menuInfo);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$arg_v", v);
              JRubyAdapter.put("$arg_menuInfo", menuInfo);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onCreateContextMenu($arg_menu, $arg_v, $arg_menuInfo)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onCreateContextMenu", new Object[]{menu, v, menuInfo});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onCreateContextMenu(menu, v, menuInfo);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateContextMenu");
      super.onCreateContextMenu(menu, v, menuInfo);
    }
  }

  public java.lang.CharSequence onCreateDescription() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_DESCRIPTION] != null) {
        super.onCreateDescription();
        return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, callbackProcs[CB_CREATE_DESCRIPTION], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_description}")) {
          super.onCreateDescription();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (java.lang.CharSequence) JRubyAdapter.runScriptlet("$ruby_instance.on_create_description()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, rubyInstance, "on_create_description");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateDescription}")) {
            super.onCreateDescription();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (java.lang.CharSequence) JRubyAdapter.runScriptlet("$ruby_instance.onCreateDescription()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, rubyInstance, "onCreateDescription");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onCreateDescription();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateDescription");
      return super.onCreateDescription();
    }
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_OPTIONS_MENU] != null) {
        super.onCreateOptionsMenu(menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_CREATE_OPTIONS_MENU], "call" , menu);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_options_menu}")) {
          super.onCreateOptionsMenu(menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_create_options_menu($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_create_options_menu", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateOptionsMenu}")) {
            super.onCreateOptionsMenu(menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onCreateOptionsMenu($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onCreateOptionsMenu", menu);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onCreateOptionsMenu(menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateOptionsMenu");
      return super.onCreateOptionsMenu(menu);
    }
  }

  public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_PANEL_MENU] != null) {
        super.onCreatePanelMenu(featureId, menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_CREATE_PANEL_MENU], "call" , new Object[]{featureId, menu});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_panel_menu}")) {
          super.onCreatePanelMenu(featureId, menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_create_panel_menu($arg_featureId, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_create_panel_menu", new Object[]{featureId, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreatePanelMenu}")) {
            super.onCreatePanelMenu(featureId, menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onCreatePanelMenu($arg_featureId, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onCreatePanelMenu", new Object[]{featureId, menu});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onCreatePanelMenu(featureId, menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreatePanelMenu");
      return super.onCreatePanelMenu(featureId, menu);
    }
  }

  public android.view.View onCreatePanelView(int featureId) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_PANEL_VIEW] != null) {
        super.onCreatePanelView(featureId);
        return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, callbackProcs[CB_CREATE_PANEL_VIEW], "call" , featureId);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_panel_view}")) {
          super.onCreatePanelView(featureId);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.on_create_panel_view($arg_featureId)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, rubyInstance, "on_create_panel_view", featureId);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreatePanelView}")) {
            super.onCreatePanelView(featureId);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.onCreatePanelView($arg_featureId)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, rubyInstance, "onCreatePanelView", featureId);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onCreatePanelView(featureId);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreatePanelView");
      return super.onCreatePanelView(featureId);
    }
  }

  public boolean onCreateThumbnail(android.graphics.Bitmap outBitmap, android.graphics.Canvas canvas) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_THUMBNAIL] != null) {
        super.onCreateThumbnail(outBitmap, canvas);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_CREATE_THUMBNAIL], "call" , new Object[]{outBitmap, canvas});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_thumbnail}")) {
          super.onCreateThumbnail(outBitmap, canvas);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_outBitmap", outBitmap);
            JRubyAdapter.put("$arg_canvas", canvas);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_create_thumbnail($arg_outBitmap, $arg_canvas)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_create_thumbnail", new Object[]{outBitmap, canvas});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateThumbnail}")) {
            super.onCreateThumbnail(outBitmap, canvas);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_outBitmap", outBitmap);
              JRubyAdapter.put("$arg_canvas", canvas);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onCreateThumbnail($arg_outBitmap, $arg_canvas)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onCreateThumbnail", new Object[]{outBitmap, canvas});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onCreateThumbnail(outBitmap, canvas);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateThumbnail");
      return super.onCreateThumbnail(outBitmap, canvas);
    }
  }

  public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CREATE_VIEW] != null) {
        super.onCreateView(name, context, attrs);
        return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, callbackProcs[CB_CREATE_VIEW], "call" , new Object[]{name, context, attrs});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_view}")) {
          super.onCreateView(name, context, attrs);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_name", name);
            JRubyAdapter.put("$arg_context", context);
            JRubyAdapter.put("$arg_attrs", attrs);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.on_create_view($arg_name, $arg_context, $arg_attrs)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, rubyInstance, "on_create_view", new Object[]{name, context, attrs});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateView}")) {
            super.onCreateView(name, context, attrs);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_name", name);
              JRubyAdapter.put("$arg_context", context);
              JRubyAdapter.put("$arg_attrs", attrs);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.onCreateView($arg_name, $arg_context, $arg_attrs)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, rubyInstance, "onCreateView", new Object[]{name, context, attrs});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onCreateView(name, context, attrs);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateView");
      return super.onCreateView(name, context, attrs);
    }
  }

  public void onDestroy() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_DESTROY] != null) {
        super.onDestroy();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_DESTROY], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_destroy}")) {
          super.onDestroy();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_destroy()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_destroy");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDestroy}")) {
            super.onDestroy();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onDestroy()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onDestroy");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onDestroy();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onDestroy");
      super.onDestroy();
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_KEY_DOWN] != null) {
        super.onKeyDown(keyCode, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_KEY_DOWN], "call" , new Object[]{keyCode, event});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_down}")) {
          super.onKeyDown(keyCode, event);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_down($arg_keyCode, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_key_down", new Object[]{keyCode, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyDown}")) {
            super.onKeyDown(keyCode, event);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyDown($arg_keyCode, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onKeyDown", new Object[]{keyCode, event});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onKeyDown(keyCode, event);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyDown");
      return super.onKeyDown(keyCode, event);
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_KEY_MULTIPLE] != null) {
        super.onKeyMultiple(keyCode, repeatCount, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_KEY_MULTIPLE], "call" , new Object[]{keyCode, repeatCount, event});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_multiple}")) {
          super.onKeyMultiple(keyCode, repeatCount, event);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_repeatCount", repeatCount);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_multiple($arg_keyCode, $arg_repeatCount, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_key_multiple", new Object[]{keyCode, repeatCount, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyMultiple}")) {
            super.onKeyMultiple(keyCode, repeatCount, event);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_repeatCount", repeatCount);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyMultiple($arg_keyCode, $arg_repeatCount, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onKeyMultiple", new Object[]{keyCode, repeatCount, event});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onKeyMultiple(keyCode, repeatCount, event);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyMultiple");
      return super.onKeyMultiple(keyCode, repeatCount, event);
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_KEY_UP] != null) {
        super.onKeyUp(keyCode, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_KEY_UP], "call" , new Object[]{keyCode, event});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_up}")) {
          super.onKeyUp(keyCode, event);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_up($arg_keyCode, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_key_up", new Object[]{keyCode, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyUp}")) {
            super.onKeyUp(keyCode, event);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyUp($arg_keyCode, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onKeyUp", new Object[]{keyCode, event});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onKeyUp(keyCode, event);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyUp");
      return super.onKeyUp(keyCode, event);
    }
  }

  public void onLowMemory() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_LOW_MEMORY] != null) {
        super.onLowMemory();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_LOW_MEMORY], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_low_memory}")) {
          super.onLowMemory();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_low_memory()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_low_memory");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onLowMemory}")) {
            super.onLowMemory();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onLowMemory()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onLowMemory");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onLowMemory();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onLowMemory");
      super.onLowMemory();
    }
  }

  public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_MENU_ITEM_SELECTED] != null) {
        super.onMenuItemSelected(featureId, item);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_MENU_ITEM_SELECTED], "call" , new Object[]{featureId, item});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_menu_item_selected}")) {
          super.onMenuItemSelected(featureId, item);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_item", item);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_menu_item_selected($arg_featureId, $arg_item)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_menu_item_selected", new Object[]{featureId, item});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onMenuItemSelected}")) {
            super.onMenuItemSelected(featureId, item);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_item", item);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onMenuItemSelected($arg_featureId, $arg_item)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onMenuItemSelected", new Object[]{featureId, item});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onMenuItemSelected(featureId, item);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onMenuItemSelected");
      return super.onMenuItemSelected(featureId, item);
    }
  }

  public boolean onMenuOpened(int featureId, android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_MENU_OPENED] != null) {
        super.onMenuOpened(featureId, menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_MENU_OPENED], "call" , new Object[]{featureId, menu});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_menu_opened}")) {
          super.onMenuOpened(featureId, menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_menu_opened($arg_featureId, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_menu_opened", new Object[]{featureId, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onMenuOpened}")) {
            super.onMenuOpened(featureId, menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onMenuOpened($arg_featureId, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onMenuOpened", new Object[]{featureId, menu});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onMenuOpened(featureId, menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onMenuOpened");
      return super.onMenuOpened(featureId, menu);
    }
  }

  public void onNewIntent(android.content.Intent intent) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_NEW_INTENT] != null) {
        super.onNewIntent(intent);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_NEW_INTENT], "call" , intent);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_new_intent}")) {
          super.onNewIntent(intent);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_new_intent($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_new_intent", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onNewIntent}")) {
            super.onNewIntent(intent);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onNewIntent($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onNewIntent", intent);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onNewIntent(intent);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onNewIntent");
      super.onNewIntent(intent);
    }
  }

  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_OPTIONS_ITEM_SELECTED] != null) {
        super.onOptionsItemSelected(item);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_OPTIONS_ITEM_SELECTED], "call" , item);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_options_item_selected}")) {
          super.onOptionsItemSelected(item);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_item", item);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_options_item_selected($arg_item)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_options_item_selected", item);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onOptionsItemSelected}")) {
            super.onOptionsItemSelected(item);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_item", item);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onOptionsItemSelected($arg_item)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onOptionsItemSelected", item);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onOptionsItemSelected(item);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onOptionsItemSelected");
      return super.onOptionsItemSelected(item);
    }
  }

  public void onOptionsMenuClosed(android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_OPTIONS_MENU_CLOSED] != null) {
        super.onOptionsMenuClosed(menu);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_OPTIONS_MENU_CLOSED], "call" , menu);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_options_menu_closed}")) {
          super.onOptionsMenuClosed(menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_options_menu_closed($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_options_menu_closed", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onOptionsMenuClosed}")) {
            super.onOptionsMenuClosed(menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onOptionsMenuClosed($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onOptionsMenuClosed", menu);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onOptionsMenuClosed(menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onOptionsMenuClosed");
      super.onOptionsMenuClosed(menu);
    }
  }

  public void onPanelClosed(int featureId, android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_PANEL_CLOSED] != null) {
        super.onPanelClosed(featureId, menu);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_PANEL_CLOSED], "call" , new Object[]{featureId, menu});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_panel_closed}")) {
          super.onPanelClosed(featureId, menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_panel_closed($arg_featureId, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_panel_closed", new Object[]{featureId, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPanelClosed}")) {
            super.onPanelClosed(featureId, menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onPanelClosed($arg_featureId, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onPanelClosed", new Object[]{featureId, menu});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onPanelClosed(featureId, menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPanelClosed");
      super.onPanelClosed(featureId, menu);
    }
  }

  public void onPause() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_PAUSE] != null) {
        super.onPause();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_PAUSE], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_pause}")) {
          super.onPause();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_pause()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_pause");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPause}")) {
            super.onPause();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onPause()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onPause");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onPause();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPause");
      super.onPause();
    }
  }

  public void onPostCreate(android.os.Bundle savedInstanceState) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_POST_CREATE] != null) {
        super.onPostCreate(savedInstanceState);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_POST_CREATE], "call" , savedInstanceState);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_post_create}")) {
          super.onPostCreate(savedInstanceState);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_post_create($arg_savedInstanceState)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_post_create", savedInstanceState);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPostCreate}")) {
            super.onPostCreate(savedInstanceState);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onPostCreate($arg_savedInstanceState)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onPostCreate", savedInstanceState);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onPostCreate(savedInstanceState);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPostCreate");
      super.onPostCreate(savedInstanceState);
    }
  }

  public void onPostResume() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_POST_RESUME] != null) {
        super.onPostResume();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_POST_RESUME], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_post_resume}")) {
          super.onPostResume();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_post_resume()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_post_resume");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPostResume}")) {
            super.onPostResume();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onPostResume()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onPostResume");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onPostResume();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPostResume");
      super.onPostResume();
    }
  }

  public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_PREPARE_OPTIONS_MENU] != null) {
        super.onPrepareOptionsMenu(menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_PREPARE_OPTIONS_MENU], "call" , menu);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_prepare_options_menu}")) {
          super.onPrepareOptionsMenu(menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_prepare_options_menu($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_prepare_options_menu", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPrepareOptionsMenu}")) {
            super.onPrepareOptionsMenu(menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onPrepareOptionsMenu($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onPrepareOptionsMenu", menu);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onPrepareOptionsMenu(menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPrepareOptionsMenu");
      return super.onPrepareOptionsMenu(menu);
    }
  }

  public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_PREPARE_PANEL] != null) {
        super.onPreparePanel(featureId, view, menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_PREPARE_PANEL], "call" , new Object[]{featureId, view, menu});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_prepare_panel}")) {
          super.onPreparePanel(featureId, view, menu);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_view", view);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_prepare_panel($arg_featureId, $arg_view, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_prepare_panel", new Object[]{featureId, view, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPreparePanel}")) {
            super.onPreparePanel(featureId, view, menu);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_view", view);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onPreparePanel($arg_featureId, $arg_view, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onPreparePanel", new Object[]{featureId, view, menu});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onPreparePanel(featureId, view, menu);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPreparePanel");
      return super.onPreparePanel(featureId, view, menu);
    }
  }

  public void onRestart() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_RESTART] != null) {
        super.onRestart();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_RESTART], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_restart}")) {
          super.onRestart();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_restart()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_restart");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRestart}")) {
            super.onRestart();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onRestart()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onRestart");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onRestart();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onRestart");
      super.onRestart();
    }
  }

  public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_RESTORE_INSTANCE_STATE] != null) {
        super.onRestoreInstanceState(savedInstanceState);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_RESTORE_INSTANCE_STATE], "call" , savedInstanceState);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_restore_instance_state}")) {
          super.onRestoreInstanceState(savedInstanceState);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_restore_instance_state($arg_savedInstanceState)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_restore_instance_state", savedInstanceState);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRestoreInstanceState}")) {
            super.onRestoreInstanceState(savedInstanceState);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onRestoreInstanceState($arg_savedInstanceState)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onRestoreInstanceState", savedInstanceState);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onRestoreInstanceState(savedInstanceState);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onRestoreInstanceState");
      super.onRestoreInstanceState(savedInstanceState);
    }
  }

  public void onResume() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_RESUME] != null) {
        super.onResume();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_RESUME], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_resume}")) {
          super.onResume();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_resume()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_resume");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onResume}")) {
            super.onResume();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onResume()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onResume");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onResume();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onResume");
      super.onResume();
    }
  }

  public void onSaveInstanceState(android.os.Bundle outState) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_SAVE_INSTANCE_STATE] != null) {
        super.onSaveInstanceState(outState);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_SAVE_INSTANCE_STATE], "call" , outState);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_save_instance_state}")) {
          super.onSaveInstanceState(outState);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_outState", outState);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_save_instance_state($arg_outState)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_save_instance_state", outState);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onSaveInstanceState}")) {
            super.onSaveInstanceState(outState);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_outState", outState);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onSaveInstanceState($arg_outState)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onSaveInstanceState", outState);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onSaveInstanceState(outState);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onSaveInstanceState");
      super.onSaveInstanceState(outState);
    }
  }

  public boolean onSearchRequested() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_SEARCH_REQUESTED] != null) {
        super.onSearchRequested();
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_SEARCH_REQUESTED], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_search_requested}")) {
          super.onSearchRequested();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_search_requested()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_search_requested");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onSearchRequested}")) {
            super.onSearchRequested();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onSearchRequested()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onSearchRequested");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onSearchRequested();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onSearchRequested");
      return super.onSearchRequested();
    }
  }

  public void onStart() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_START] != null) {
        super.onStart();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_START], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start}")) {
          super.onStart();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_start()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_start");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStart}")) {
            super.onStart();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onStart()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onStart");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onStart();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onStart");
      super.onStart();
    }
  }

  public void onStop() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_STOP] != null) {
        super.onStop();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_STOP], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_stop}")) {
          super.onStop();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_stop()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_stop");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStop}")) {
            super.onStop();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onStop()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onStop");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onStop();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onStop");
      super.onStop();
    }
  }

  public void onTitleChanged(java.lang.CharSequence title, int color) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_TITLE_CHANGED] != null) {
        super.onTitleChanged(title, color);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_TITLE_CHANGED], "call" , new Object[]{title, color});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_title_changed}")) {
          super.onTitleChanged(title, color);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_title", title);
            JRubyAdapter.put("$arg_color", color);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_title_changed($arg_title, $arg_color)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_title_changed", new Object[]{title, color});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTitleChanged}")) {
            super.onTitleChanged(title, color);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_title", title);
              JRubyAdapter.put("$arg_color", color);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onTitleChanged($arg_title, $arg_color)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onTitleChanged", new Object[]{title, color});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onTitleChanged(title, color);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onTitleChanged");
      super.onTitleChanged(title, color);
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_TOUCH_EVENT] != null) {
        super.onTouchEvent(event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_TOUCH_EVENT], "call" , event);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_touch_event}")) {
          super.onTouchEvent(event);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_touch_event($arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_touch_event", event);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTouchEvent}")) {
            super.onTouchEvent(event);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onTouchEvent($arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onTouchEvent", event);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onTouchEvent(event);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onTouchEvent");
      return super.onTouchEvent(event);
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_TRACKBALL_EVENT] != null) {
        super.onTrackballEvent(event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_TRACKBALL_EVENT], "call" , event);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_trackball_event}")) {
          super.onTrackballEvent(event);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_trackball_event($arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_trackball_event", event);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTrackballEvent}")) {
            super.onTrackballEvent(event);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onTrackballEvent($arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onTrackballEvent", event);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onTrackballEvent(event);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onTrackballEvent");
      return super.onTrackballEvent(event);
    }
  }

  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED] != null) {
        super.onWindowAttributesChanged(params);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED], "call" , params);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_window_attributes_changed}")) {
          super.onWindowAttributesChanged(params);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_params", params);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_window_attributes_changed($arg_params)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_window_attributes_changed", params);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onWindowAttributesChanged}")) {
            super.onWindowAttributesChanged(params);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_params", params);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onWindowAttributesChanged($arg_params)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onWindowAttributesChanged", params);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onWindowAttributesChanged(params);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onWindowAttributesChanged");
      super.onWindowAttributesChanged(params);
    }
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
        super.onWindowFocusChanged(hasFocus);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , hasFocus);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_window_focus_changed}")) {
          super.onWindowFocusChanged(hasFocus);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_hasFocus", hasFocus);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_window_focus_changed($arg_hasFocus)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_window_focus_changed", hasFocus);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onWindowFocusChanged}")) {
            super.onWindowFocusChanged(hasFocus);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_hasFocus", hasFocus);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onWindowFocusChanged($arg_hasFocus)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onWindowFocusChanged", hasFocus);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onWindowFocusChanged(hasFocus);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onWindowFocusChanged");
      super.onWindowFocusChanged(hasFocus);
    }
  }

  public void onUserInteraction() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_USER_INTERACTION] != null) {
        super.onUserInteraction();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_USER_INTERACTION], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_user_interaction}")) {
          super.onUserInteraction();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_user_interaction()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_user_interaction");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUserInteraction}")) {
            super.onUserInteraction();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onUserInteraction()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onUserInteraction");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onUserInteraction();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onUserInteraction");
      super.onUserInteraction();
    }
  }

  public void onUserLeaveHint() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_USER_LEAVE_HINT] != null) {
        super.onUserLeaveHint();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_USER_LEAVE_HINT], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_user_leave_hint}")) {
          super.onUserLeaveHint();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_user_leave_hint()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_user_leave_hint");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUserLeaveHint}")) {
            super.onUserLeaveHint();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onUserLeaveHint()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onUserLeaveHint");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onUserLeaveHint();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onUserLeaveHint");
      super.onUserLeaveHint();
    }
  }

  public void onAttachedToWindow() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
        super.onAttachedToWindow();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_attached_to_window}")) {
          super.onAttachedToWindow();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_attached_to_window()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_attached_to_window");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onAttachedToWindow}")) {
            super.onAttachedToWindow();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onAttachedToWindow()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onAttachedToWindow");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onAttachedToWindow();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onAttachedToWindow");
      super.onAttachedToWindow();
    }
  }

  public void onBackPressed() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_BACK_PRESSED] != null) {
        super.onBackPressed();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_BACK_PRESSED], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_back_pressed}")) {
          super.onBackPressed();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_back_pressed()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_back_pressed");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBackPressed}")) {
            super.onBackPressed();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onBackPressed()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onBackPressed");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onBackPressed();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onBackPressed");
      super.onBackPressed();
    }
  }

  public void onDetachedFromWindow() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
        super.onDetachedFromWindow();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_detached_from_window}")) {
          super.onDetachedFromWindow();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_detached_from_window()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_detached_from_window");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDetachedFromWindow}")) {
            super.onDetachedFromWindow();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onDetachedFromWindow()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onDetachedFromWindow");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onDetachedFromWindow();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onDetachedFromWindow");
      super.onDetachedFromWindow();
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_KEY_LONG_PRESS] != null) {
        super.onKeyLongPress(keyCode, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_long_press}")) {
          super.onKeyLongPress(keyCode, event);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_long_press($arg_keyCode, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_key_long_press", new Object[]{keyCode, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyLongPress}")) {
            super.onKeyLongPress(keyCode, event);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyLongPress($arg_keyCode, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onKeyLongPress", new Object[]{keyCode, event});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onKeyLongPress(keyCode, event);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyLongPress");
      return super.onKeyLongPress(keyCode, event);
    }
  }

  public void onApplyThemeResource(android.content.res.Resources.Theme theme, int resid, boolean first) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_APPLY_THEME_RESOURCE] != null) {
        super.onApplyThemeResource(theme, resid, first);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_APPLY_THEME_RESOURCE], "call" , new Object[]{theme, resid, first});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_apply_theme_resource}")) {
          super.onApplyThemeResource(theme, resid, first);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_theme", theme);
            JRubyAdapter.put("$arg_resid", resid);
            JRubyAdapter.put("$arg_first", first);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_apply_theme_resource($arg_theme, $arg_resid, $arg_first)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_apply_theme_resource", new Object[]{theme, resid, first});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onApplyThemeResource}")) {
            super.onApplyThemeResource(theme, resid, first);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_theme", theme);
              JRubyAdapter.put("$arg_resid", resid);
              JRubyAdapter.put("$arg_first", first);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onApplyThemeResource($arg_theme, $arg_resid, $arg_first)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onApplyThemeResource", new Object[]{theme, resid, first});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onApplyThemeResource(theme, resid, first);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onApplyThemeResource");
      super.onApplyThemeResource(theme, resid, first);
    }
  }

}
