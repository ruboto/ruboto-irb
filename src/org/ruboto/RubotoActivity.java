package org.ruboto;

import java.io.IOException;

import org.ruboto.Script;

import android.app.ProgressDialog;
import android.os.Bundle;

public class RubotoActivity extends android.app.Activity {
    private String scriptName;
    private String remoteVariable = null;
    private Object[] args;
    private Bundle configBundle = null;
    private Object rubyInstance;

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

    private Object[] callbackProcs = new Object[48];

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

    public void setScriptName(String name) {
        scriptName = name;
    }

    /****************************************************************************************
     *
     *  Activity Lifecycle: onCreate
     */
	
    @Override
    public void onCreate(Bundle bundle) {
        args = new Object[1];
        args[0] = bundle;

        configBundle = getIntent().getBundleExtra("RubotoActivity Config");

        if (configBundle != null) {
            if (configBundle.containsKey("Theme")) {
                setTheme(configBundle.getInt("Theme"));
            }
            if (configBundle.containsKey("Script")) {
                if (this.getClass().getName() == RubotoActivity.class.getName()) {
                    setScriptName(configBundle.getString("Script"));
                } else {
                    throw new IllegalArgumentException("Only local Intents may set script name.");
                }
            }
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
                String rubyClassName = Script.toCamelCase(scriptName);
                System.out.println("Looking for Ruby class: " + rubyClassName);
                Object rubyClass = JRubyAdapter.get(rubyClassName);
                if (rubyClass == null) {
                    System.out.println("Loading script: " + scriptName);
                    JRubyAdapter.exec(new Script(scriptName).getContents());
                    rubyClass = JRubyAdapter.get(rubyClassName);
                }
                if (rubyClass != null) {
                    System.out.println("Instanciating Ruby class: " + rubyClassName);
                    rubyInstance = JRubyAdapter.callMethod(rubyClass, "new", this, Object.class);
                    JRubyAdapter.callMethod(rubyInstance, "on_create", args[0]);
                }
            } else if (configBundle != null) {
                // TODO: Why doesn't this work?
                // JRubyAdapter.callMethod(this, "initialize_ruboto");
            	JRubyAdapter.execute("$activity.initialize_ruboto");
                // TODO: Why doesn't this work?
                // JRubyAdapter.callMethod(this, "on_create", args[0]);
            	JRubyAdapter.execute("$activity.on_create($bundle)");
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
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_activity_result"}, Boolean.class)) {
      super.onActivityResult(requestCode, resultCode, data);
      JRubyAdapter.callMethod(rubyInstance, "on_activity_result" , new Object[]{requestCode, resultCode, data});
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onActivityResult"}, Boolean.class)) {
        super.onActivityResult(requestCode, resultCode, data);
        JRubyAdapter.callMethod(rubyInstance, "onActivityResult" , new Object[]{requestCode, resultCode, data});
      } else {
        if (callbackProcs != null && callbackProcs[CB_ACTIVITY_RESULT] != null) {
          super.onActivityResult(requestCode, resultCode, data);
          JRubyAdapter.callMethod(callbackProcs[CB_ACTIVITY_RESULT], "call" , new Object[]{requestCode, resultCode, data});
        } else {
          super.onActivityResult(requestCode, resultCode, data);
        }
      }
    }
  }

  public void onChildTitleChanged(android.app.Activity childActivity, java.lang.CharSequence title) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_child_title_changed"}, Boolean.class)) {
      super.onChildTitleChanged(childActivity, title);
      JRubyAdapter.callMethod(rubyInstance, "on_child_title_changed" , new Object[]{childActivity, title});
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onChildTitleChanged"}, Boolean.class)) {
        super.onChildTitleChanged(childActivity, title);
        JRubyAdapter.callMethod(rubyInstance, "onChildTitleChanged" , new Object[]{childActivity, title});
      } else {
        if (callbackProcs != null && callbackProcs[CB_CHILD_TITLE_CHANGED] != null) {
          super.onChildTitleChanged(childActivity, title);
          JRubyAdapter.callMethod(callbackProcs[CB_CHILD_TITLE_CHANGED], "call" , new Object[]{childActivity, title});
        } else {
          super.onChildTitleChanged(childActivity, title);
        }
      }
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_configuration_changed"}, Boolean.class)) {
      super.onConfigurationChanged(newConfig);
      JRubyAdapter.callMethod(rubyInstance, "on_configuration_changed" , newConfig);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onConfigurationChanged"}, Boolean.class)) {
        super.onConfigurationChanged(newConfig);
        JRubyAdapter.callMethod(rubyInstance, "onConfigurationChanged" , newConfig);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
          super.onConfigurationChanged(newConfig);
          JRubyAdapter.callMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
        } else {
          super.onConfigurationChanged(newConfig);
        }
      }
    }
  }

  public void onContentChanged() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_content_changed"}, Boolean.class)) {
      super.onContentChanged();
      JRubyAdapter.callMethod(rubyInstance, "on_content_changed" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onContentChanged"}, Boolean.class)) {
        super.onContentChanged();
        JRubyAdapter.callMethod(rubyInstance, "onContentChanged" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_CONTENT_CHANGED] != null) {
          super.onContentChanged();
          JRubyAdapter.callMethod(callbackProcs[CB_CONTENT_CHANGED], "call" );
        } else {
          super.onContentChanged();
        }
      }
    }
  }

  public boolean onContextItemSelected(android.view.MenuItem item) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_context_item_selected"}, Boolean.class)) {
      super.onContextItemSelected(item);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_context_item_selected" , item, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onContextItemSelected"}, Boolean.class)) {
        super.onContextItemSelected(item);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onContextItemSelected" , item, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CONTEXT_ITEM_SELECTED] != null) {
          super.onContextItemSelected(item);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_CONTEXT_ITEM_SELECTED], "call" , item, Boolean.class);
        } else {
          return super.onContextItemSelected(item);
        }
      }
    }
  }

  public void onContextMenuClosed(android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_context_menu_closed"}, Boolean.class)) {
      super.onContextMenuClosed(menu);
      JRubyAdapter.callMethod(rubyInstance, "on_context_menu_closed" , menu);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onContextMenuClosed"}, Boolean.class)) {
        super.onContextMenuClosed(menu);
        JRubyAdapter.callMethod(rubyInstance, "onContextMenuClosed" , menu);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CONTEXT_MENU_CLOSED] != null) {
          super.onContextMenuClosed(menu);
          JRubyAdapter.callMethod(callbackProcs[CB_CONTEXT_MENU_CLOSED], "call" , menu);
        } else {
          super.onContextMenuClosed(menu);
        }
      }
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_context_menu"}, Boolean.class)) {
      super.onCreateContextMenu(menu, v, menuInfo);
      JRubyAdapter.callMethod(rubyInstance, "on_create_context_menu" , new Object[]{menu, v, menuInfo});
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreateContextMenu"}, Boolean.class)) {
        super.onCreateContextMenu(menu, v, menuInfo);
        JRubyAdapter.callMethod(rubyInstance, "onCreateContextMenu" , new Object[]{menu, v, menuInfo});
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
          super.onCreateContextMenu(menu, v, menuInfo);
          JRubyAdapter.callMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , new Object[]{menu, v, menuInfo});
        } else {
          super.onCreateContextMenu(menu, v, menuInfo);
        }
      }
    }
  }

  public java.lang.CharSequence onCreateDescription() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_description"}, Boolean.class)) {
      super.onCreateDescription();
      return (java.lang.CharSequence) JRubyAdapter.callMethod(rubyInstance, "on_create_description" , java.lang.CharSequence.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreateDescription"}, Boolean.class)) {
        super.onCreateDescription();
        return (java.lang.CharSequence) JRubyAdapter.callMethod(rubyInstance, "onCreateDescription" , java.lang.CharSequence.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_DESCRIPTION] != null) {
          super.onCreateDescription();
          return (java.lang.CharSequence) JRubyAdapter.callMethod(callbackProcs[CB_CREATE_DESCRIPTION], "call" , java.lang.CharSequence.class);
        } else {
          return super.onCreateDescription();
        }
      }
    }
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_options_menu"}, Boolean.class)) {
      super.onCreateOptionsMenu(menu);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_create_options_menu" , menu, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreateOptionsMenu"}, Boolean.class)) {
        super.onCreateOptionsMenu(menu);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onCreateOptionsMenu" , menu, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_OPTIONS_MENU] != null) {
          super.onCreateOptionsMenu(menu);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_CREATE_OPTIONS_MENU], "call" , menu, Boolean.class);
        } else {
          return super.onCreateOptionsMenu(menu);
        }
      }
    }
  }

  public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_panel_menu"}, Boolean.class)) {
      super.onCreatePanelMenu(featureId, menu);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_create_panel_menu" , new Object[]{featureId, menu}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreatePanelMenu"}, Boolean.class)) {
        super.onCreatePanelMenu(featureId, menu);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onCreatePanelMenu" , new Object[]{featureId, menu}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_PANEL_MENU] != null) {
          super.onCreatePanelMenu(featureId, menu);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_CREATE_PANEL_MENU], "call" , new Object[]{featureId, menu}, Boolean.class);
        } else {
          return super.onCreatePanelMenu(featureId, menu);
        }
      }
    }
  }

  public android.view.View onCreatePanelView(int featureId) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_panel_view"}, Boolean.class)) {
      super.onCreatePanelView(featureId);
      return (android.view.View) JRubyAdapter.callMethod(rubyInstance, "on_create_panel_view" , featureId, android.view.View.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreatePanelView"}, Boolean.class)) {
        super.onCreatePanelView(featureId);
        return (android.view.View) JRubyAdapter.callMethod(rubyInstance, "onCreatePanelView" , featureId, android.view.View.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_PANEL_VIEW] != null) {
          super.onCreatePanelView(featureId);
          return (android.view.View) JRubyAdapter.callMethod(callbackProcs[CB_CREATE_PANEL_VIEW], "call" , featureId, android.view.View.class);
        } else {
          return super.onCreatePanelView(featureId);
        }
      }
    }
  }

  public boolean onCreateThumbnail(android.graphics.Bitmap outBitmap, android.graphics.Canvas canvas) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_thumbnail"}, Boolean.class)) {
      super.onCreateThumbnail(outBitmap, canvas);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_create_thumbnail" , new Object[]{outBitmap, canvas}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreateThumbnail"}, Boolean.class)) {
        super.onCreateThumbnail(outBitmap, canvas);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onCreateThumbnail" , new Object[]{outBitmap, canvas}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_THUMBNAIL] != null) {
          super.onCreateThumbnail(outBitmap, canvas);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_CREATE_THUMBNAIL], "call" , new Object[]{outBitmap, canvas}, Boolean.class);
        } else {
          return super.onCreateThumbnail(outBitmap, canvas);
        }
      }
    }
  }

  public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_create_view"}, Boolean.class)) {
      super.onCreateView(name, context, attrs);
      return (android.view.View) JRubyAdapter.callMethod(rubyInstance, "on_create_view" , new Object[]{name, context, attrs}, android.view.View.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onCreateView"}, Boolean.class)) {
        super.onCreateView(name, context, attrs);
        return (android.view.View) JRubyAdapter.callMethod(rubyInstance, "onCreateView" , new Object[]{name, context, attrs}, android.view.View.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CREATE_VIEW] != null) {
          super.onCreateView(name, context, attrs);
          return (android.view.View) JRubyAdapter.callMethod(callbackProcs[CB_CREATE_VIEW], "call" , new Object[]{name, context, attrs}, android.view.View.class);
        } else {
          return super.onCreateView(name, context, attrs);
        }
      }
    }
  }

  public void onDestroy() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_destroy"}, Boolean.class)) {
      super.onDestroy();
      JRubyAdapter.callMethod(rubyInstance, "on_destroy" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onDestroy"}, Boolean.class)) {
        super.onDestroy();
        JRubyAdapter.callMethod(rubyInstance, "onDestroy" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_DESTROY] != null) {
          super.onDestroy();
          JRubyAdapter.callMethod(callbackProcs[CB_DESTROY], "call" );
        } else {
          super.onDestroy();
        }
      }
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_key_down"}, Boolean.class)) {
      super.onKeyDown(keyCode, event);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_key_down" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onKeyDown"}, Boolean.class)) {
        super.onKeyDown(keyCode, event);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onKeyDown" , new Object[]{keyCode, event}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_KEY_DOWN] != null) {
          super.onKeyDown(keyCode, event);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_KEY_DOWN], "call" , new Object[]{keyCode, event}, Boolean.class);
        } else {
          return super.onKeyDown(keyCode, event);
        }
      }
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_key_multiple"}, Boolean.class)) {
      super.onKeyMultiple(keyCode, repeatCount, event);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_key_multiple" , new Object[]{keyCode, repeatCount, event}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onKeyMultiple"}, Boolean.class)) {
        super.onKeyMultiple(keyCode, repeatCount, event);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onKeyMultiple" , new Object[]{keyCode, repeatCount, event}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_KEY_MULTIPLE] != null) {
          super.onKeyMultiple(keyCode, repeatCount, event);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_KEY_MULTIPLE], "call" , new Object[]{keyCode, repeatCount, event}, Boolean.class);
        } else {
          return super.onKeyMultiple(keyCode, repeatCount, event);
        }
      }
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_key_up"}, Boolean.class)) {
      super.onKeyUp(keyCode, event);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_key_up" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onKeyUp"}, Boolean.class)) {
        super.onKeyUp(keyCode, event);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onKeyUp" , new Object[]{keyCode, event}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_KEY_UP] != null) {
          super.onKeyUp(keyCode, event);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_KEY_UP], "call" , new Object[]{keyCode, event}, Boolean.class);
        } else {
          return super.onKeyUp(keyCode, event);
        }
      }
    }
  }

  public void onLowMemory() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_low_memory"}, Boolean.class)) {
      super.onLowMemory();
      JRubyAdapter.callMethod(rubyInstance, "on_low_memory" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onLowMemory"}, Boolean.class)) {
        super.onLowMemory();
        JRubyAdapter.callMethod(rubyInstance, "onLowMemory" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_LOW_MEMORY] != null) {
          super.onLowMemory();
          JRubyAdapter.callMethod(callbackProcs[CB_LOW_MEMORY], "call" );
        } else {
          super.onLowMemory();
        }
      }
    }
  }

  public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_menu_item_selected"}, Boolean.class)) {
      super.onMenuItemSelected(featureId, item);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_menu_item_selected" , new Object[]{featureId, item}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onMenuItemSelected"}, Boolean.class)) {
        super.onMenuItemSelected(featureId, item);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onMenuItemSelected" , new Object[]{featureId, item}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_MENU_ITEM_SELECTED] != null) {
          super.onMenuItemSelected(featureId, item);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_MENU_ITEM_SELECTED], "call" , new Object[]{featureId, item}, Boolean.class);
        } else {
          return super.onMenuItemSelected(featureId, item);
        }
      }
    }
  }

  public boolean onMenuOpened(int featureId, android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_menu_opened"}, Boolean.class)) {
      super.onMenuOpened(featureId, menu);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_menu_opened" , new Object[]{featureId, menu}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onMenuOpened"}, Boolean.class)) {
        super.onMenuOpened(featureId, menu);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onMenuOpened" , new Object[]{featureId, menu}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_MENU_OPENED] != null) {
          super.onMenuOpened(featureId, menu);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_MENU_OPENED], "call" , new Object[]{featureId, menu}, Boolean.class);
        } else {
          return super.onMenuOpened(featureId, menu);
        }
      }
    }
  }

  public void onNewIntent(android.content.Intent intent) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_new_intent"}, Boolean.class)) {
      super.onNewIntent(intent);
      JRubyAdapter.callMethod(rubyInstance, "on_new_intent" , intent);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onNewIntent"}, Boolean.class)) {
        super.onNewIntent(intent);
        JRubyAdapter.callMethod(rubyInstance, "onNewIntent" , intent);
      } else {
        if (callbackProcs != null && callbackProcs[CB_NEW_INTENT] != null) {
          super.onNewIntent(intent);
          JRubyAdapter.callMethod(callbackProcs[CB_NEW_INTENT], "call" , intent);
        } else {
          super.onNewIntent(intent);
        }
      }
    }
  }

  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_options_item_selected"}, Boolean.class)) {
      super.onOptionsItemSelected(item);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_options_item_selected" , item, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onOptionsItemSelected"}, Boolean.class)) {
        super.onOptionsItemSelected(item);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onOptionsItemSelected" , item, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_OPTIONS_ITEM_SELECTED] != null) {
          super.onOptionsItemSelected(item);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_OPTIONS_ITEM_SELECTED], "call" , item, Boolean.class);
        } else {
          return super.onOptionsItemSelected(item);
        }
      }
    }
  }

  public void onOptionsMenuClosed(android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_options_menu_closed"}, Boolean.class)) {
      super.onOptionsMenuClosed(menu);
      JRubyAdapter.callMethod(rubyInstance, "on_options_menu_closed" , menu);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onOptionsMenuClosed"}, Boolean.class)) {
        super.onOptionsMenuClosed(menu);
        JRubyAdapter.callMethod(rubyInstance, "onOptionsMenuClosed" , menu);
      } else {
        if (callbackProcs != null && callbackProcs[CB_OPTIONS_MENU_CLOSED] != null) {
          super.onOptionsMenuClosed(menu);
          JRubyAdapter.callMethod(callbackProcs[CB_OPTIONS_MENU_CLOSED], "call" , menu);
        } else {
          super.onOptionsMenuClosed(menu);
        }
      }
    }
  }

  public void onPanelClosed(int featureId, android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_panel_closed"}, Boolean.class)) {
      super.onPanelClosed(featureId, menu);
      JRubyAdapter.callMethod(rubyInstance, "on_panel_closed" , new Object[]{featureId, menu});
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onPanelClosed"}, Boolean.class)) {
        super.onPanelClosed(featureId, menu);
        JRubyAdapter.callMethod(rubyInstance, "onPanelClosed" , new Object[]{featureId, menu});
      } else {
        if (callbackProcs != null && callbackProcs[CB_PANEL_CLOSED] != null) {
          super.onPanelClosed(featureId, menu);
          JRubyAdapter.callMethod(callbackProcs[CB_PANEL_CLOSED], "call" , new Object[]{featureId, menu});
        } else {
          super.onPanelClosed(featureId, menu);
        }
      }
    }
  }

  public void onPause() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_pause"}, Boolean.class)) {
      super.onPause();
      JRubyAdapter.callMethod(rubyInstance, "on_pause" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onPause"}, Boolean.class)) {
        super.onPause();
        JRubyAdapter.callMethod(rubyInstance, "onPause" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_PAUSE] != null) {
          super.onPause();
          JRubyAdapter.callMethod(callbackProcs[CB_PAUSE], "call" );
        } else {
          super.onPause();
        }
      }
    }
  }

  public void onPostCreate(android.os.Bundle savedInstanceState) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_post_create"}, Boolean.class)) {
      super.onPostCreate(savedInstanceState);
      JRubyAdapter.callMethod(rubyInstance, "on_post_create" , savedInstanceState);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onPostCreate"}, Boolean.class)) {
        super.onPostCreate(savedInstanceState);
        JRubyAdapter.callMethod(rubyInstance, "onPostCreate" , savedInstanceState);
      } else {
        if (callbackProcs != null && callbackProcs[CB_POST_CREATE] != null) {
          super.onPostCreate(savedInstanceState);
          JRubyAdapter.callMethod(callbackProcs[CB_POST_CREATE], "call" , savedInstanceState);
        } else {
          super.onPostCreate(savedInstanceState);
        }
      }
    }
  }

  public void onPostResume() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_post_resume"}, Boolean.class)) {
      super.onPostResume();
      JRubyAdapter.callMethod(rubyInstance, "on_post_resume" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onPostResume"}, Boolean.class)) {
        super.onPostResume();
        JRubyAdapter.callMethod(rubyInstance, "onPostResume" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_POST_RESUME] != null) {
          super.onPostResume();
          JRubyAdapter.callMethod(callbackProcs[CB_POST_RESUME], "call" );
        } else {
          super.onPostResume();
        }
      }
    }
  }

  public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_prepare_options_menu"}, Boolean.class)) {
      super.onPrepareOptionsMenu(menu);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_prepare_options_menu" , menu, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onPrepareOptionsMenu"}, Boolean.class)) {
        super.onPrepareOptionsMenu(menu);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onPrepareOptionsMenu" , menu, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_PREPARE_OPTIONS_MENU] != null) {
          super.onPrepareOptionsMenu(menu);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_PREPARE_OPTIONS_MENU], "call" , menu, Boolean.class);
        } else {
          return super.onPrepareOptionsMenu(menu);
        }
      }
    }
  }

  public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_prepare_panel"}, Boolean.class)) {
      super.onPreparePanel(featureId, view, menu);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_prepare_panel" , new Object[]{featureId, view, menu}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onPreparePanel"}, Boolean.class)) {
        super.onPreparePanel(featureId, view, menu);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onPreparePanel" , new Object[]{featureId, view, menu}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_PREPARE_PANEL] != null) {
          super.onPreparePanel(featureId, view, menu);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_PREPARE_PANEL], "call" , new Object[]{featureId, view, menu}, Boolean.class);
        } else {
          return super.onPreparePanel(featureId, view, menu);
        }
      }
    }
  }

  public void onRestart() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_restart"}, Boolean.class)) {
      super.onRestart();
      JRubyAdapter.callMethod(rubyInstance, "on_restart" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onRestart"}, Boolean.class)) {
        super.onRestart();
        JRubyAdapter.callMethod(rubyInstance, "onRestart" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_RESTART] != null) {
          super.onRestart();
          JRubyAdapter.callMethod(callbackProcs[CB_RESTART], "call" );
        } else {
          super.onRestart();
        }
      }
    }
  }

  public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_restore_instance_state"}, Boolean.class)) {
      super.onRestoreInstanceState(savedInstanceState);
      JRubyAdapter.callMethod(rubyInstance, "on_restore_instance_state" , savedInstanceState);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onRestoreInstanceState"}, Boolean.class)) {
        super.onRestoreInstanceState(savedInstanceState);
        JRubyAdapter.callMethod(rubyInstance, "onRestoreInstanceState" , savedInstanceState);
      } else {
        if (callbackProcs != null && callbackProcs[CB_RESTORE_INSTANCE_STATE] != null) {
          super.onRestoreInstanceState(savedInstanceState);
          JRubyAdapter.callMethod(callbackProcs[CB_RESTORE_INSTANCE_STATE], "call" , savedInstanceState);
        } else {
          super.onRestoreInstanceState(savedInstanceState);
        }
      }
    }
  }

  public void onResume() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_resume"}, Boolean.class)) {
      super.onResume();
      JRubyAdapter.callMethod(rubyInstance, "on_resume" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onResume"}, Boolean.class)) {
        super.onResume();
        JRubyAdapter.callMethod(rubyInstance, "onResume" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_RESUME] != null) {
          super.onResume();
          JRubyAdapter.callMethod(callbackProcs[CB_RESUME], "call" );
        } else {
          super.onResume();
        }
      }
    }
  }

  public void onSaveInstanceState(android.os.Bundle outState) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_save_instance_state"}, Boolean.class)) {
      super.onSaveInstanceState(outState);
      JRubyAdapter.callMethod(rubyInstance, "on_save_instance_state" , outState);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onSaveInstanceState"}, Boolean.class)) {
        super.onSaveInstanceState(outState);
        JRubyAdapter.callMethod(rubyInstance, "onSaveInstanceState" , outState);
      } else {
        if (callbackProcs != null && callbackProcs[CB_SAVE_INSTANCE_STATE] != null) {
          super.onSaveInstanceState(outState);
          JRubyAdapter.callMethod(callbackProcs[CB_SAVE_INSTANCE_STATE], "call" , outState);
        } else {
          super.onSaveInstanceState(outState);
        }
      }
    }
  }

  public boolean onSearchRequested() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_search_requested"}, Boolean.class)) {
      super.onSearchRequested();
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_search_requested" , Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onSearchRequested"}, Boolean.class)) {
        super.onSearchRequested();
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onSearchRequested" , Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_SEARCH_REQUESTED] != null) {
          super.onSearchRequested();
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_SEARCH_REQUESTED], "call" , Boolean.class);
        } else {
          return super.onSearchRequested();
        }
      }
    }
  }

  public void onStart() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_start"}, Boolean.class)) {
      super.onStart();
      JRubyAdapter.callMethod(rubyInstance, "on_start" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onStart"}, Boolean.class)) {
        super.onStart();
        JRubyAdapter.callMethod(rubyInstance, "onStart" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_START] != null) {
          super.onStart();
          JRubyAdapter.callMethod(callbackProcs[CB_START], "call" );
        } else {
          super.onStart();
        }
      }
    }
  }

  public void onStop() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_stop"}, Boolean.class)) {
      super.onStop();
      JRubyAdapter.callMethod(rubyInstance, "on_stop" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onStop"}, Boolean.class)) {
        super.onStop();
        JRubyAdapter.callMethod(rubyInstance, "onStop" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_STOP] != null) {
          super.onStop();
          JRubyAdapter.callMethod(callbackProcs[CB_STOP], "call" );
        } else {
          super.onStop();
        }
      }
    }
  }

  public void onTitleChanged(java.lang.CharSequence title, int color) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_title_changed"}, Boolean.class)) {
      super.onTitleChanged(title, color);
      JRubyAdapter.callMethod(rubyInstance, "on_title_changed" , new Object[]{title, color});
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onTitleChanged"}, Boolean.class)) {
        super.onTitleChanged(title, color);
        JRubyAdapter.callMethod(rubyInstance, "onTitleChanged" , new Object[]{title, color});
      } else {
        if (callbackProcs != null && callbackProcs[CB_TITLE_CHANGED] != null) {
          super.onTitleChanged(title, color);
          JRubyAdapter.callMethod(callbackProcs[CB_TITLE_CHANGED], "call" , new Object[]{title, color});
        } else {
          super.onTitleChanged(title, color);
        }
      }
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_touch_event"}, Boolean.class)) {
      super.onTouchEvent(event);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_touch_event" , event, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onTouchEvent"}, Boolean.class)) {
        super.onTouchEvent(event);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onTouchEvent" , event, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_TOUCH_EVENT] != null) {
          super.onTouchEvent(event);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_TOUCH_EVENT], "call" , event, Boolean.class);
        } else {
          return super.onTouchEvent(event);
        }
      }
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_trackball_event"}, Boolean.class)) {
      super.onTrackballEvent(event);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_trackball_event" , event, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onTrackballEvent"}, Boolean.class)) {
        super.onTrackballEvent(event);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onTrackballEvent" , event, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_TRACKBALL_EVENT] != null) {
          super.onTrackballEvent(event);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_TRACKBALL_EVENT], "call" , event, Boolean.class);
        } else {
          return super.onTrackballEvent(event);
        }
      }
    }
  }

  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_window_attributes_changed"}, Boolean.class)) {
      super.onWindowAttributesChanged(params);
      JRubyAdapter.callMethod(rubyInstance, "on_window_attributes_changed" , params);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onWindowAttributesChanged"}, Boolean.class)) {
        super.onWindowAttributesChanged(params);
        JRubyAdapter.callMethod(rubyInstance, "onWindowAttributesChanged" , params);
      } else {
        if (callbackProcs != null && callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED] != null) {
          super.onWindowAttributesChanged(params);
          JRubyAdapter.callMethod(callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED], "call" , params);
        } else {
          super.onWindowAttributesChanged(params);
        }
      }
    }
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_window_focus_changed"}, Boolean.class)) {
      super.onWindowFocusChanged(hasFocus);
      JRubyAdapter.callMethod(rubyInstance, "on_window_focus_changed" , hasFocus);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onWindowFocusChanged"}, Boolean.class)) {
        super.onWindowFocusChanged(hasFocus);
        JRubyAdapter.callMethod(rubyInstance, "onWindowFocusChanged" , hasFocus);
      } else {
        if (callbackProcs != null && callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
          super.onWindowFocusChanged(hasFocus);
          JRubyAdapter.callMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , hasFocus);
        } else {
          super.onWindowFocusChanged(hasFocus);
        }
      }
    }
  }

  public void onUserInteraction() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_user_interaction"}, Boolean.class)) {
      super.onUserInteraction();
      JRubyAdapter.callMethod(rubyInstance, "on_user_interaction" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onUserInteraction"}, Boolean.class)) {
        super.onUserInteraction();
        JRubyAdapter.callMethod(rubyInstance, "onUserInteraction" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_USER_INTERACTION] != null) {
          super.onUserInteraction();
          JRubyAdapter.callMethod(callbackProcs[CB_USER_INTERACTION], "call" );
        } else {
          super.onUserInteraction();
        }
      }
    }
  }

  public void onUserLeaveHint() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_user_leave_hint"}, Boolean.class)) {
      super.onUserLeaveHint();
      JRubyAdapter.callMethod(rubyInstance, "on_user_leave_hint" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onUserLeaveHint"}, Boolean.class)) {
        super.onUserLeaveHint();
        JRubyAdapter.callMethod(rubyInstance, "onUserLeaveHint" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_USER_LEAVE_HINT] != null) {
          super.onUserLeaveHint();
          JRubyAdapter.callMethod(callbackProcs[CB_USER_LEAVE_HINT], "call" );
        } else {
          super.onUserLeaveHint();
        }
      }
    }
  }

  public void onAttachedToWindow() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_attached_to_window"}, Boolean.class)) {
      super.onAttachedToWindow();
      JRubyAdapter.callMethod(rubyInstance, "on_attached_to_window" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onAttachedToWindow"}, Boolean.class)) {
        super.onAttachedToWindow();
        JRubyAdapter.callMethod(rubyInstance, "onAttachedToWindow" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
          super.onAttachedToWindow();
          JRubyAdapter.callMethod(callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
        } else {
          super.onAttachedToWindow();
        }
      }
    }
  }

  public void onBackPressed() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_back_pressed"}, Boolean.class)) {
      super.onBackPressed();
      JRubyAdapter.callMethod(rubyInstance, "on_back_pressed" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onBackPressed"}, Boolean.class)) {
        super.onBackPressed();
        JRubyAdapter.callMethod(rubyInstance, "onBackPressed" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_BACK_PRESSED] != null) {
          super.onBackPressed();
          JRubyAdapter.callMethod(callbackProcs[CB_BACK_PRESSED], "call" );
        } else {
          super.onBackPressed();
        }
      }
    }
  }

  public void onDetachedFromWindow() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_detached_from_window"}, Boolean.class)) {
      super.onDetachedFromWindow();
      JRubyAdapter.callMethod(rubyInstance, "on_detached_from_window" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onDetachedFromWindow"}, Boolean.class)) {
        super.onDetachedFromWindow();
        JRubyAdapter.callMethod(rubyInstance, "onDetachedFromWindow" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
          super.onDetachedFromWindow();
          JRubyAdapter.callMethod(callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
        } else {
          super.onDetachedFromWindow();
        }
      }
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_key_long_press"}, Boolean.class)) {
      super.onKeyLongPress(keyCode, event);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_key_long_press" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onKeyLongPress"}, Boolean.class)) {
        super.onKeyLongPress(keyCode, event);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onKeyLongPress" , new Object[]{keyCode, event}, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_KEY_LONG_PRESS] != null) {
          super.onKeyLongPress(keyCode, event);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event}, Boolean.class);
        } else {
          return super.onKeyLongPress(keyCode, event);
        }
      }
    }
  }

  public void onApplyThemeResource(android.content.res.Resources.Theme theme, int resid, boolean first) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_apply_theme_resource"}, Boolean.class)) {
      super.onApplyThemeResource(theme, resid, first);
      JRubyAdapter.callMethod(rubyInstance, "on_apply_theme_resource" , new Object[]{theme, resid, first});
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onApplyThemeResource"}, Boolean.class)) {
        super.onApplyThemeResource(theme, resid, first);
        JRubyAdapter.callMethod(rubyInstance, "onApplyThemeResource" , new Object[]{theme, resid, first});
      } else {
        if (callbackProcs != null && callbackProcs[CB_APPLY_THEME_RESOURCE] != null) {
          super.onApplyThemeResource(theme, resid, first);
          JRubyAdapter.callMethod(callbackProcs[CB_APPLY_THEME_RESOURCE], "call" , new Object[]{theme, resid, first});
        } else {
          super.onApplyThemeResource(theme, resid, first);
        }
      }
    }
  }

}
