package org.ruboto;

import java.io.IOException;

import org.ruboto.Script;

import android.app.ProgressDialog;
import android.os.Bundle;

public class RubotoPreferenceActivity extends android.preference.PreferenceActivity implements org.ruboto.RubotoComponent {
  public static final int CB_LIST_ITEM_CLICK = 0;
  public static final int CB_ACTIVITY_RESULT = 1;
  public static final int CB_CHILD_TITLE_CHANGED = 2;
  public static final int CB_CONFIGURATION_CHANGED = 3;
  public static final int CB_CONTENT_CHANGED = 4;
  public static final int CB_CONTEXT_ITEM_SELECTED = 5;
  public static final int CB_CONTEXT_MENU_CLOSED = 6;
  public static final int CB_CREATE_CONTEXT_MENU = 7;
  public static final int CB_CREATE_DESCRIPTION = 8;
  public static final int CB_CREATE_OPTIONS_MENU = 9;
  public static final int CB_CREATE_PANEL_MENU = 10;
  public static final int CB_CREATE_PANEL_VIEW = 11;
  public static final int CB_CREATE_THUMBNAIL = 12;
  public static final int CB_CREATE_VIEW = 13;
  public static final int CB_DESTROY = 14;
  public static final int CB_KEY_DOWN = 15;
  public static final int CB_KEY_MULTIPLE = 16;
  public static final int CB_KEY_UP = 17;
  public static final int CB_LOW_MEMORY = 18;
  public static final int CB_MENU_ITEM_SELECTED = 19;
  public static final int CB_MENU_OPENED = 20;
  public static final int CB_NEW_INTENT = 21;
  public static final int CB_OPTIONS_ITEM_SELECTED = 22;
  public static final int CB_OPTIONS_MENU_CLOSED = 23;
  public static final int CB_PANEL_CLOSED = 24;
  public static final int CB_PAUSE = 25;
  public static final int CB_POST_CREATE = 26;
  public static final int CB_POST_RESUME = 27;
  public static final int CB_PREPARE_OPTIONS_MENU = 28;
  public static final int CB_PREPARE_PANEL = 29;
  public static final int CB_RESTART = 30;
  public static final int CB_RESTORE_INSTANCE_STATE = 31;
  public static final int CB_RESUME = 32;
  public static final int CB_SAVE_INSTANCE_STATE = 33;
  public static final int CB_SEARCH_REQUESTED = 34;
  public static final int CB_START = 35;
  public static final int CB_STOP = 36;
  public static final int CB_TITLE_CHANGED = 37;
  public static final int CB_TOUCH_EVENT = 38;
  public static final int CB_TRACKBALL_EVENT = 39;
  public static final int CB_WINDOW_ATTRIBUTES_CHANGED = 40;
  public static final int CB_WINDOW_FOCUS_CHANGED = 41;
  public static final int CB_USER_INTERACTION = 42;
  public static final int CB_USER_LEAVE_HINT = 43;
  public static final int CB_ATTACHED_TO_WINDOW = 44;
  public static final int CB_BACK_PRESSED = 45;
  public static final int CB_DETACHED_FROM_WINDOW = 46;
  public static final int CB_KEY_LONG_PRESS = 47;
  public static final int CB_APPLY_THEME_RESOURCE = 48;

    private final ScriptInfo scriptInfo = new ScriptInfo(49);
    private String remoteVariable = null;
    Bundle[] args;
    private Bundle configBundle = null;

    public RubotoPreferenceActivity setRemoteVariable(String var) {
        remoteVariable = var;
        return this;
    }

    public String getRemoteVariableCall(String call) {
        return (remoteVariable == null ? "" : (remoteVariable + ".")) + call;
    }

    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    /****************************************************************************************
     *
     *  Activity Lifecycle: onCreate
     */

    // FIXME(uwe):  Only used for block based primary activities.  Remove if we remove support for such.
	public void onCreateSuper() {
	    super.onCreate((Bundle) args[0]);
	}

    @Override
    public void onCreate(Bundle bundle) {
        System.out.println("RubotoPreferenceActivity onCreate(): " + getClass().getName());
        if (ScriptLoader.isCalledFromJRuby()) {
            super.onCreate(bundle);
            return;
        }
        args = new Bundle[1];
        args[0] = bundle;

        configBundle = getIntent().getBundleExtra("RubotoActivity Config");

        if (configBundle != null) {
            if (configBundle.containsKey("Theme")) {
                setTheme(configBundle.getInt("Theme"));
            }
            if (configBundle.containsKey("ClassName")) {
                if (this.getClass().getName() == RubotoPreferenceActivity.class.getName()) {
                    scriptInfo.setRubyClassName(configBundle.getString("ClassName"));
                } else {
                    throw new IllegalArgumentException("Only local Intents may set class name.");
                }
            }
            if (configBundle.containsKey("Script")) {
                if (this.getClass().getName() == RubotoPreferenceActivity.class.getName()) {
                    scriptInfo.setScriptName(configBundle.getString("Script"));
                } else {
                    throw new IllegalArgumentException("Only local Intents may set script name.");
                }
            }
        }

        if (JRubyAdapter.isInitialized()) {
            prepareJRuby();
    	    ScriptLoader.loadScript(this, (Object[]) args);
        } else {
            super.onCreate(bundle);
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

    public boolean rubotoAttachable() {
      return true;
    }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onListItemClick(l, v, position, id); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_LIST_ITEM_CLICK] != null) {
        super.onListItemClick(l, v, position, id);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_LIST_ITEM_CLICK], "call" , new Object[]{l, v, position, id});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_list_item_click}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_l", l);
            JRubyAdapter.put("$arg_v", v);
            JRubyAdapter.put("$arg_position", position);
            JRubyAdapter.put("$arg_id", id);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_list_item_click($arg_l, $arg_v, $arg_position, $arg_id)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_list_item_click", new Object[]{l, v, position, id});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onListItemClick}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_l", l);
              JRubyAdapter.put("$arg_v", v);
              JRubyAdapter.put("$arg_position", position);
              JRubyAdapter.put("$arg_id", id);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onListItemClick($arg_l, $arg_v, $arg_position, $arg_id)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onListItemClick", new Object[]{l, v, position, id});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onListItemClick(l, v, position, id); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onListItemClick");
      {super.onListItemClick(l, v, position, id); return;}
    }
  }

  public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onActivityResult(requestCode, resultCode, data); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_ACTIVITY_RESULT] != null) {
        super.onActivityResult(requestCode, resultCode, data);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_ACTIVITY_RESULT], "call" , new Object[]{requestCode, resultCode, data});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_activity_result}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_requestCode", requestCode);
            JRubyAdapter.put("$arg_resultCode", resultCode);
            JRubyAdapter.put("$arg_data", data);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_activity_result($arg_requestCode, $arg_resultCode, $arg_data)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_activity_result", new Object[]{requestCode, resultCode, data});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onActivityResult}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_requestCode", requestCode);
              JRubyAdapter.put("$arg_resultCode", resultCode);
              JRubyAdapter.put("$arg_data", data);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onActivityResult($arg_requestCode, $arg_resultCode, $arg_data)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onActivityResult", new Object[]{requestCode, resultCode, data});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onActivityResult(requestCode, resultCode, data); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onActivityResult");
      {super.onActivityResult(requestCode, resultCode, data); return;}
    }
  }

  public void onChildTitleChanged(android.app.Activity childActivity, java.lang.CharSequence title) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onChildTitleChanged(childActivity, title); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CHILD_TITLE_CHANGED] != null) {
        super.onChildTitleChanged(childActivity, title);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_CHILD_TITLE_CHANGED], "call" , new Object[]{childActivity, title});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_child_title_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_childActivity", childActivity);
            JRubyAdapter.put("$arg_title", title);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_child_title_changed($arg_childActivity, $arg_title)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_child_title_changed", new Object[]{childActivity, title});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onChildTitleChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_childActivity", childActivity);
              JRubyAdapter.put("$arg_title", title);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onChildTitleChanged($arg_childActivity, $arg_title)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onChildTitleChanged", new Object[]{childActivity, title});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onChildTitleChanged(childActivity, title); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onChildTitleChanged");
      {super.onChildTitleChanged(childActivity, title); return;}
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onConfigurationChanged(newConfig); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CONFIGURATION_CHANGED] != null) {
        super.onConfigurationChanged(newConfig);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_CONFIGURATION_CHANGED], "call" , newConfig);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_configuration_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_newConfig", newConfig);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_configuration_changed($arg_newConfig)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_configuration_changed", newConfig);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onConfigurationChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_newConfig", newConfig);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onConfigurationChanged($arg_newConfig)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onConfigurationChanged", newConfig);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onConfigurationChanged(newConfig); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onConfigurationChanged");
      {super.onConfigurationChanged(newConfig); return;}
    }
  }

  public void onContentChanged() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onContentChanged(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CONTENT_CHANGED] != null) {
        super.onContentChanged();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_CONTENT_CHANGED], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_content_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_content_changed()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_content_changed");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContentChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onContentChanged()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onContentChanged");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onContentChanged(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onContentChanged");
      {super.onContentChanged(); return;}
    }
  }

  public boolean onContextItemSelected(android.view.MenuItem item) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onContextItemSelected(item);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CONTEXT_ITEM_SELECTED] != null) {
        super.onContextItemSelected(item);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_CONTEXT_ITEM_SELECTED], "call" , item);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_context_item_selected}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_item", item);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_context_item_selected($arg_item)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_context_item_selected", item);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContextItemSelected}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_item", item);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onContextItemSelected($arg_item)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onContextItemSelected", item);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onContextItemSelected");
      return super.onContextItemSelected(item);
    }
  }

  public void onContextMenuClosed(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onContextMenuClosed(menu); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CONTEXT_MENU_CLOSED] != null) {
        super.onContextMenuClosed(menu);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_CONTEXT_MENU_CLOSED], "call" , menu);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_context_menu_closed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_context_menu_closed($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_context_menu_closed", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContextMenuClosed}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onContextMenuClosed($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onContextMenuClosed", menu);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onContextMenuClosed(menu); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onContextMenuClosed");
      {super.onContextMenuClosed(menu); return;}
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onCreateContextMenu(menu, v, menuInfo); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_CONTEXT_MENU] != null) {
        super.onCreateContextMenu(menu, v, menuInfo);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_CREATE_CONTEXT_MENU], "call" , new Object[]{menu, v, menuInfo});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_context_menu}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$arg_v", v);
            JRubyAdapter.put("$arg_menuInfo", menuInfo);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_create_context_menu($arg_menu, $arg_v, $arg_menuInfo)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_create_context_menu", new Object[]{menu, v, menuInfo});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateContextMenu}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$arg_v", v);
              JRubyAdapter.put("$arg_menuInfo", menuInfo);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onCreateContextMenu($arg_menu, $arg_v, $arg_menuInfo)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onCreateContextMenu", new Object[]{menu, v, menuInfo});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onCreateContextMenu(menu, v, menuInfo); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreateContextMenu");
      {super.onCreateContextMenu(menu, v, menuInfo); return;}
    }
  }

  public java.lang.CharSequence onCreateDescription() {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateDescription();
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_DESCRIPTION] != null) {
        super.onCreateDescription();
        return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, scriptInfo.getCallbackProcs()[CB_CREATE_DESCRIPTION], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_description}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (java.lang.CharSequence) JRubyAdapter.runScriptlet("$ruby_instance.on_create_description()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, scriptInfo.getRubyInstance(), "on_create_description");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateDescription}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (java.lang.CharSequence) JRubyAdapter.runScriptlet("$ruby_instance.onCreateDescription()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, scriptInfo.getRubyInstance(), "onCreateDescription");
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreateDescription");
      return super.onCreateDescription();
    }
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateOptionsMenu(menu);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_OPTIONS_MENU] != null) {
        super.onCreateOptionsMenu(menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_CREATE_OPTIONS_MENU], "call" , menu);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_options_menu}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_create_options_menu($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_create_options_menu", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateOptionsMenu}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onCreateOptionsMenu($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onCreateOptionsMenu", menu);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreateOptionsMenu");
      return super.onCreateOptionsMenu(menu);
    }
  }

  public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreatePanelMenu(featureId, menu);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_PANEL_MENU] != null) {
        super.onCreatePanelMenu(featureId, menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_CREATE_PANEL_MENU], "call" , new Object[]{featureId, menu});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_panel_menu}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_create_panel_menu($arg_featureId, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_create_panel_menu", new Object[]{featureId, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreatePanelMenu}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onCreatePanelMenu($arg_featureId, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onCreatePanelMenu", new Object[]{featureId, menu});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreatePanelMenu");
      return super.onCreatePanelMenu(featureId, menu);
    }
  }

  public android.view.View onCreatePanelView(int featureId) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreatePanelView(featureId);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_PANEL_VIEW] != null) {
        super.onCreatePanelView(featureId);
        return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getCallbackProcs()[CB_CREATE_PANEL_VIEW], "call" , featureId);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_panel_view}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.on_create_panel_view($arg_featureId)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "on_create_panel_view", featureId);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreatePanelView}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.onCreatePanelView($arg_featureId)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "onCreatePanelView", featureId);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreatePanelView");
      return super.onCreatePanelView(featureId);
    }
  }

  public boolean onCreateThumbnail(android.graphics.Bitmap outBitmap, android.graphics.Canvas canvas) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateThumbnail(outBitmap, canvas);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_THUMBNAIL] != null) {
        super.onCreateThumbnail(outBitmap, canvas);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_CREATE_THUMBNAIL], "call" , new Object[]{outBitmap, canvas});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_thumbnail}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_outBitmap", outBitmap);
            JRubyAdapter.put("$arg_canvas", canvas);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_create_thumbnail($arg_outBitmap, $arg_canvas)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_create_thumbnail", new Object[]{outBitmap, canvas});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateThumbnail}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_outBitmap", outBitmap);
              JRubyAdapter.put("$arg_canvas", canvas);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onCreateThumbnail($arg_outBitmap, $arg_canvas)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onCreateThumbnail", new Object[]{outBitmap, canvas});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreateThumbnail");
      return super.onCreateThumbnail(outBitmap, canvas);
    }
  }

  public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateView(name, context, attrs);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CREATE_VIEW] != null) {
        super.onCreateView(name, context, attrs);
        return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getCallbackProcs()[CB_CREATE_VIEW], "call" , new Object[]{name, context, attrs});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_view}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_name", name);
            JRubyAdapter.put("$arg_context", context);
            JRubyAdapter.put("$arg_attrs", attrs);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.on_create_view($arg_name, $arg_context, $arg_attrs)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "on_create_view", new Object[]{name, context, attrs});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateView}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_name", name);
              JRubyAdapter.put("$arg_context", context);
              JRubyAdapter.put("$arg_attrs", attrs);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (android.view.View) JRubyAdapter.runScriptlet("$ruby_instance.onCreateView($arg_name, $arg_context, $arg_attrs)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "onCreateView", new Object[]{name, context, attrs});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onCreateView");
      return super.onCreateView(name, context, attrs);
    }
  }

  public void onDestroy() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onDestroy(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_DESTROY] != null) {
        super.onDestroy();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_DESTROY], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_destroy}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_destroy()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_destroy");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDestroy}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onDestroy()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDestroy");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onDestroy(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onDestroy");
      {super.onDestroy(); return;}
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyDown(keyCode, event);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_KEY_DOWN] != null) {
        super.onKeyDown(keyCode, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_KEY_DOWN], "call" , new Object[]{keyCode, event});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_down}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_down($arg_keyCode, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_down", new Object[]{keyCode, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyDown}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyDown($arg_keyCode, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyDown", new Object[]{keyCode, event});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onKeyDown");
      return super.onKeyDown(keyCode, event);
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyMultiple(keyCode, repeatCount, event);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_KEY_MULTIPLE] != null) {
        super.onKeyMultiple(keyCode, repeatCount, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_KEY_MULTIPLE], "call" , new Object[]{keyCode, repeatCount, event});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_multiple}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_repeatCount", repeatCount);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_multiple($arg_keyCode, $arg_repeatCount, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_multiple", new Object[]{keyCode, repeatCount, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyMultiple}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_repeatCount", repeatCount);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyMultiple($arg_keyCode, $arg_repeatCount, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyMultiple", new Object[]{keyCode, repeatCount, event});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onKeyMultiple");
      return super.onKeyMultiple(keyCode, repeatCount, event);
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyUp(keyCode, event);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_KEY_UP] != null) {
        super.onKeyUp(keyCode, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_KEY_UP], "call" , new Object[]{keyCode, event});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_up}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_up($arg_keyCode, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_up", new Object[]{keyCode, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyUp}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyUp($arg_keyCode, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyUp", new Object[]{keyCode, event});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onKeyUp");
      return super.onKeyUp(keyCode, event);
    }
  }

  public void onLowMemory() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onLowMemory(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_LOW_MEMORY] != null) {
        super.onLowMemory();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_LOW_MEMORY], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_low_memory}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_low_memory()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_low_memory");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onLowMemory}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onLowMemory()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onLowMemory");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onLowMemory(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onLowMemory");
      {super.onLowMemory(); return;}
    }
  }

  public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onMenuItemSelected(featureId, item);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_MENU_ITEM_SELECTED] != null) {
        super.onMenuItemSelected(featureId, item);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_MENU_ITEM_SELECTED], "call" , new Object[]{featureId, item});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_menu_item_selected}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_item", item);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_menu_item_selected($arg_featureId, $arg_item)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_menu_item_selected", new Object[]{featureId, item});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onMenuItemSelected}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_item", item);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onMenuItemSelected($arg_featureId, $arg_item)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onMenuItemSelected", new Object[]{featureId, item});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onMenuItemSelected");
      return super.onMenuItemSelected(featureId, item);
    }
  }

  public boolean onMenuOpened(int featureId, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onMenuOpened(featureId, menu);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_MENU_OPENED] != null) {
        super.onMenuOpened(featureId, menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_MENU_OPENED], "call" , new Object[]{featureId, menu});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_menu_opened}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_menu_opened($arg_featureId, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_menu_opened", new Object[]{featureId, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onMenuOpened}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onMenuOpened($arg_featureId, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onMenuOpened", new Object[]{featureId, menu});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onMenuOpened");
      return super.onMenuOpened(featureId, menu);
    }
  }

  public void onNewIntent(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onNewIntent(intent); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_NEW_INTENT] != null) {
        super.onNewIntent(intent);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_NEW_INTENT], "call" , intent);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_new_intent}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_new_intent($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_new_intent", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onNewIntent}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onNewIntent($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onNewIntent", intent);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onNewIntent(intent); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onNewIntent");
      {super.onNewIntent(intent); return;}
    }
  }

  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onOptionsItemSelected(item);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_OPTIONS_ITEM_SELECTED] != null) {
        super.onOptionsItemSelected(item);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_OPTIONS_ITEM_SELECTED], "call" , item);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_options_item_selected}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_item", item);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_options_item_selected($arg_item)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_options_item_selected", item);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onOptionsItemSelected}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_item", item);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onOptionsItemSelected($arg_item)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onOptionsItemSelected", item);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onOptionsItemSelected");
      return super.onOptionsItemSelected(item);
    }
  }

  public void onOptionsMenuClosed(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onOptionsMenuClosed(menu); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_OPTIONS_MENU_CLOSED] != null) {
        super.onOptionsMenuClosed(menu);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_OPTIONS_MENU_CLOSED], "call" , menu);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_options_menu_closed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_options_menu_closed($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_options_menu_closed", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onOptionsMenuClosed}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onOptionsMenuClosed($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onOptionsMenuClosed", menu);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onOptionsMenuClosed(menu); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onOptionsMenuClosed");
      {super.onOptionsMenuClosed(menu); return;}
    }
  }

  public void onPanelClosed(int featureId, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPanelClosed(featureId, menu); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_PANEL_CLOSED] != null) {
        super.onPanelClosed(featureId, menu);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_PANEL_CLOSED], "call" , new Object[]{featureId, menu});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_panel_closed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_panel_closed($arg_featureId, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_panel_closed", new Object[]{featureId, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPanelClosed}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onPanelClosed($arg_featureId, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPanelClosed", new Object[]{featureId, menu});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onPanelClosed(featureId, menu); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onPanelClosed");
      {super.onPanelClosed(featureId, menu); return;}
    }
  }

  public void onPause() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPause(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_PAUSE] != null) {
        super.onPause();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_PAUSE], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_pause}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_pause()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_pause");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPause}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onPause()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPause");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onPause(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onPause");
      {super.onPause(); return;}
    }
  }

  public void onPostCreate(android.os.Bundle savedInstanceState) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPostCreate(savedInstanceState); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_POST_CREATE] != null) {
        super.onPostCreate(savedInstanceState);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_POST_CREATE], "call" , savedInstanceState);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_post_create}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_post_create($arg_savedInstanceState)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_post_create", savedInstanceState);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPostCreate}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onPostCreate($arg_savedInstanceState)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPostCreate", savedInstanceState);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onPostCreate(savedInstanceState); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onPostCreate");
      {super.onPostCreate(savedInstanceState); return;}
    }
  }

  public void onPostResume() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPostResume(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_POST_RESUME] != null) {
        super.onPostResume();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_POST_RESUME], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_post_resume}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_post_resume()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_post_resume");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPostResume}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onPostResume()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPostResume");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onPostResume(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onPostResume");
      {super.onPostResume(); return;}
    }
  }

  public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onPrepareOptionsMenu(menu);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_PREPARE_OPTIONS_MENU] != null) {
        super.onPrepareOptionsMenu(menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_PREPARE_OPTIONS_MENU], "call" , menu);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_prepare_options_menu}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_prepare_options_menu($arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_prepare_options_menu", menu);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPrepareOptionsMenu}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onPrepareOptionsMenu($arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onPrepareOptionsMenu", menu);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onPrepareOptionsMenu");
      return super.onPrepareOptionsMenu(menu);
    }
  }

  public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onPreparePanel(featureId, view, menu);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_PREPARE_PANEL] != null) {
        super.onPreparePanel(featureId, view, menu);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_PREPARE_PANEL], "call" , new Object[]{featureId, view, menu});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_prepare_panel}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_featureId", featureId);
            JRubyAdapter.put("$arg_view", view);
            JRubyAdapter.put("$arg_menu", menu);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_prepare_panel($arg_featureId, $arg_view, $arg_menu)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_prepare_panel", new Object[]{featureId, view, menu});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPreparePanel}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_featureId", featureId);
              JRubyAdapter.put("$arg_view", view);
              JRubyAdapter.put("$arg_menu", menu);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onPreparePanel($arg_featureId, $arg_view, $arg_menu)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onPreparePanel", new Object[]{featureId, view, menu});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onPreparePanel");
      return super.onPreparePanel(featureId, view, menu);
    }
  }

  public void onRestart() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onRestart(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_RESTART] != null) {
        super.onRestart();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_RESTART], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_restart}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_restart()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_restart");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRestart}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onRestart()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRestart");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onRestart(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onRestart");
      {super.onRestart(); return;}
    }
  }

  public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onRestoreInstanceState(savedInstanceState); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_RESTORE_INSTANCE_STATE] != null) {
        super.onRestoreInstanceState(savedInstanceState);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_RESTORE_INSTANCE_STATE], "call" , savedInstanceState);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_restore_instance_state}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_restore_instance_state($arg_savedInstanceState)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_restore_instance_state", savedInstanceState);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRestoreInstanceState}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_savedInstanceState", savedInstanceState);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onRestoreInstanceState($arg_savedInstanceState)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRestoreInstanceState", savedInstanceState);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onRestoreInstanceState(savedInstanceState); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onRestoreInstanceState");
      {super.onRestoreInstanceState(savedInstanceState); return;}
    }
  }

  public void onResume() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onResume(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_RESUME] != null) {
        super.onResume();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_RESUME], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_resume}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_resume()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_resume");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onResume}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onResume()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onResume");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onResume(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onResume");
      {super.onResume(); return;}
    }
  }

  public void onSaveInstanceState(android.os.Bundle outState) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onSaveInstanceState(outState); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_SAVE_INSTANCE_STATE] != null) {
        super.onSaveInstanceState(outState);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_SAVE_INSTANCE_STATE], "call" , outState);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_save_instance_state}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_outState", outState);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_save_instance_state($arg_outState)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_save_instance_state", outState);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onSaveInstanceState}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_outState", outState);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onSaveInstanceState($arg_outState)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onSaveInstanceState", outState);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onSaveInstanceState(outState); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onSaveInstanceState");
      {super.onSaveInstanceState(outState); return;}
    }
  }

  public boolean onSearchRequested() {
    if (ScriptLoader.isCalledFromJRuby()) return super.onSearchRequested();
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_SEARCH_REQUESTED] != null) {
        super.onSearchRequested();
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_SEARCH_REQUESTED], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_search_requested}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_search_requested()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_search_requested");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onSearchRequested}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onSearchRequested()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onSearchRequested");
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onSearchRequested");
      return super.onSearchRequested();
    }
  }

  public void onStart() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onStart(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_START] != null) {
        super.onStart();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_START], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_start()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_start");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStart}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onStart()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onStart");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onStart(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onStart");
      {super.onStart(); return;}
    }
  }

  public void onStop() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onStop(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_STOP] != null) {
        super.onStop();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_STOP], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_stop}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_stop()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_stop");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStop}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onStop()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onStop");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onStop(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onStop");
      {super.onStop(); return;}
    }
  }

  public void onTitleChanged(java.lang.CharSequence title, int color) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onTitleChanged(title, color); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_TITLE_CHANGED] != null) {
        super.onTitleChanged(title, color);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_TITLE_CHANGED], "call" , new Object[]{title, color});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_title_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_title", title);
            JRubyAdapter.put("$arg_color", color);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_title_changed($arg_title, $arg_color)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_title_changed", new Object[]{title, color});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTitleChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_title", title);
              JRubyAdapter.put("$arg_color", color);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onTitleChanged($arg_title, $arg_color)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onTitleChanged", new Object[]{title, color});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onTitleChanged(title, color); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onTitleChanged");
      {super.onTitleChanged(title, color); return;}
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onTouchEvent(event);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_TOUCH_EVENT] != null) {
        super.onTouchEvent(event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_TOUCH_EVENT], "call" , event);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_touch_event}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_touch_event($arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_touch_event", event);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTouchEvent}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onTouchEvent($arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onTouchEvent", event);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onTouchEvent");
      return super.onTouchEvent(event);
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onTrackballEvent(event);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_TRACKBALL_EVENT] != null) {
        super.onTrackballEvent(event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_TRACKBALL_EVENT], "call" , event);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_trackball_event}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_trackball_event($arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_trackball_event", event);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTrackballEvent}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onTrackballEvent($arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onTrackballEvent", event);
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onTrackballEvent");
      return super.onTrackballEvent(event);
    }
  }

  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onWindowAttributesChanged(params); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_WINDOW_ATTRIBUTES_CHANGED] != null) {
        super.onWindowAttributesChanged(params);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_WINDOW_ATTRIBUTES_CHANGED], "call" , params);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_window_attributes_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_params", params);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_window_attributes_changed($arg_params)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_window_attributes_changed", params);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onWindowAttributesChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_params", params);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onWindowAttributesChanged($arg_params)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onWindowAttributesChanged", params);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onWindowAttributesChanged(params); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onWindowAttributesChanged");
      {super.onWindowAttributesChanged(params); return;}
    }
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onWindowFocusChanged(hasFocus); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_WINDOW_FOCUS_CHANGED] != null) {
        super.onWindowFocusChanged(hasFocus);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_WINDOW_FOCUS_CHANGED], "call" , hasFocus);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_window_focus_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_hasFocus", hasFocus);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_window_focus_changed($arg_hasFocus)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_window_focus_changed", hasFocus);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onWindowFocusChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_hasFocus", hasFocus);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onWindowFocusChanged($arg_hasFocus)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onWindowFocusChanged", hasFocus);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onWindowFocusChanged(hasFocus); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onWindowFocusChanged");
      {super.onWindowFocusChanged(hasFocus); return;}
    }
  }

  public void onUserInteraction() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onUserInteraction(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_USER_INTERACTION] != null) {
        super.onUserInteraction();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_USER_INTERACTION], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_user_interaction}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_user_interaction()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_user_interaction");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUserInteraction}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onUserInteraction()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onUserInteraction");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onUserInteraction(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onUserInteraction");
      {super.onUserInteraction(); return;}
    }
  }

  public void onUserLeaveHint() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onUserLeaveHint(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_USER_LEAVE_HINT] != null) {
        super.onUserLeaveHint();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_USER_LEAVE_HINT], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_user_leave_hint}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_user_leave_hint()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_user_leave_hint");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUserLeaveHint}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onUserLeaveHint()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onUserLeaveHint");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onUserLeaveHint(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onUserLeaveHint");
      {super.onUserLeaveHint(); return;}
    }
  }

  public void onAttachedToWindow() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onAttachedToWindow(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_ATTACHED_TO_WINDOW] != null) {
        super.onAttachedToWindow();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_ATTACHED_TO_WINDOW], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_attached_to_window}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_attached_to_window()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_attached_to_window");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onAttachedToWindow}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onAttachedToWindow()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onAttachedToWindow");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onAttachedToWindow(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onAttachedToWindow");
      {super.onAttachedToWindow(); return;}
    }
  }

  public void onBackPressed() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onBackPressed(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_BACK_PRESSED] != null) {
        super.onBackPressed();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_BACK_PRESSED], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_back_pressed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_back_pressed()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_back_pressed");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBackPressed}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onBackPressed()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onBackPressed");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onBackPressed(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onBackPressed");
      {super.onBackPressed(); return;}
    }
  }

  public void onDetachedFromWindow() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onDetachedFromWindow(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_DETACHED_FROM_WINDOW] != null) {
        super.onDetachedFromWindow();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_DETACHED_FROM_WINDOW], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_detached_from_window}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_detached_from_window()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_detached_from_window");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDetachedFromWindow}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onDetachedFromWindow()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDetachedFromWindow");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onDetachedFromWindow(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onDetachedFromWindow");
      {super.onDetachedFromWindow(); return;}
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyLongPress(keyCode, event);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_KEY_LONG_PRESS] != null) {
        super.onKeyLongPress(keyCode, event);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_long_press}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_keyCode", keyCode);
            JRubyAdapter.put("$arg_event", event);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_key_long_press($arg_keyCode, $arg_event)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_long_press", new Object[]{keyCode, event});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyLongPress}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_keyCode", keyCode);
              JRubyAdapter.put("$arg_event", event);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onKeyLongPress($arg_keyCode, $arg_event)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyLongPress", new Object[]{keyCode, event});
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
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onKeyLongPress");
      return super.onKeyLongPress(keyCode, event);
    }
  }

  public void onApplyThemeResource(android.content.res.Resources.Theme theme, int resid, boolean first) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onApplyThemeResource(theme, resid, first); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_APPLY_THEME_RESOURCE] != null) {
        super.onApplyThemeResource(theme, resid, first);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_APPLY_THEME_RESOURCE], "call" , new Object[]{theme, resid, first});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_apply_theme_resource}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_theme", theme);
            JRubyAdapter.put("$arg_resid", resid);
            JRubyAdapter.put("$arg_first", first);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_apply_theme_resource($arg_theme, $arg_resid, $arg_first)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_apply_theme_resource", new Object[]{theme, resid, first});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onApplyThemeResource}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_theme", theme);
              JRubyAdapter.put("$arg_resid", resid);
              JRubyAdapter.put("$arg_first", first);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onApplyThemeResource($arg_theme, $arg_resid, $arg_first)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onApplyThemeResource", new Object[]{theme, resid, first});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onApplyThemeResource(theme, resid, first); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoPreferenceActivity#onApplyThemeResource");
      {super.onApplyThemeResource(theme, resid, first); return;}
    }
  }

}
