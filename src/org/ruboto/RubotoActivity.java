package org.ruboto;

import java.io.IOException;

import org.ruboto.Script;

import android.app.ProgressDialog;
import android.os.Bundle;

public class RubotoActivity extends android.app.Activity implements org.ruboto.RubotoComponent {
    public static final String THEME_KEY = "RUBOTO_THEME";
    private final ScriptInfo scriptInfo = new ScriptInfo();
    Bundle[] args;

    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    /****************************************************************************************
     *
     *  Activity Lifecycle: onCreate
     */
    @Override
    public void onCreate(Bundle bundle) {
        System.out.println("RubotoActivity onCreate(): " + getClass().getName());

        // Shut this RubotoActivity down if it's not able to restart 
        if (this.getClass().getName().equals("org.ruboto.RubotoActivity") && !JRubyAdapter.isInitialized()) {
            super.onCreate(bundle);
	          System.out.println("Shutting down stale RubotoActivity: " + getClass().getName());
            finish();
            return;
        }
				
       if (ScriptLoader.isCalledFromJRuby()) {
            super.onCreate(bundle);
            return;
        }
        args = new Bundle[]{bundle};

        // FIXME(uwe):  Deprecated as of Ruboto 0.13.0.  Remove in june 2014 (twelve months).
        Bundle configBundle = getIntent().getBundleExtra("Ruboto Config");
        if (configBundle != null) {
            if (configBundle.containsKey("Theme")) {
                setTheme(configBundle.getInt("Theme"));
            }
        }
        // EMXIF

        if (getIntent().hasExtra(THEME_KEY)) {
            setTheme(getIntent().getIntExtra(THEME_KEY, 0));
        }
        scriptInfo.setFromIntent(getIntent());

        if (JRubyAdapter.isInitialized() && scriptInfo.isReadyToLoad()) {
    	    ScriptLoader.loadScript(this);
    	    ScriptLoader.callOnCreate(this, (Object[]) args);
        } else {
            super.onCreate(bundle);
        }
    }

    // FIXME(uwe):  What is this for?
    public boolean rubotoAttachable() {
      return true;
    }
    // EMXIF

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onActivityResult(requestCode, resultCode, data); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onActivityResult");
      {super.onActivityResult(requestCode, resultCode, data); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onActivityResult(requestCode, resultCode, data); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_activity_result}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_activity_result", new Object[]{requestCode, resultCode, data});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onActivityResult}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onActivityResult", new Object[]{requestCode, resultCode, data});
      } else {
        {super.onActivityResult(requestCode, resultCode, data); return;}
      }
    }
  }

  public void onChildTitleChanged(android.app.Activity childActivity, java.lang.CharSequence title) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onChildTitleChanged(childActivity, title); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onChildTitleChanged");
      {super.onChildTitleChanged(childActivity, title); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onChildTitleChanged(childActivity, title); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_child_title_changed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_child_title_changed", new Object[]{childActivity, title});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onChildTitleChanged}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onChildTitleChanged", new Object[]{childActivity, title});
      } else {
        {super.onChildTitleChanged(childActivity, title); return;}
      }
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onConfigurationChanged(newConfig); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onConfigurationChanged");
      {super.onConfigurationChanged(newConfig); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onConfigurationChanged(newConfig); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_configuration_changed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_configuration_changed", newConfig);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onConfigurationChanged}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onConfigurationChanged", newConfig);
      } else {
        {super.onConfigurationChanged(newConfig); return;}
      }
    }
  }

  public void onContentChanged() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onContentChanged(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onContentChanged");
      {super.onContentChanged(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onContentChanged(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_content_changed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_content_changed");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContentChanged}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onContentChanged");
      } else {
        {super.onContentChanged(); return;}
      }
    }
  }

  public boolean onContextItemSelected(android.view.MenuItem item) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onContextItemSelected(item);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onContextItemSelected");
      return super.onContextItemSelected(item);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onContextItemSelected(item);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_context_item_selected}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_context_item_selected", item);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContextItemSelected}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onContextItemSelected", item);
      } else {
        return super.onContextItemSelected(item);
      }
    }
  }

  public void onContextMenuClosed(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onContextMenuClosed(menu); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onContextMenuClosed");
      {super.onContextMenuClosed(menu); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onContextMenuClosed(menu); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_context_menu_closed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_context_menu_closed", menu);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onContextMenuClosed}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onContextMenuClosed", menu);
      } else {
        {super.onContextMenuClosed(menu); return;}
      }
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onCreateContextMenu(menu, v, menuInfo); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateContextMenu");
      {super.onCreateContextMenu(menu, v, menuInfo); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onCreateContextMenu(menu, v, menuInfo); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_context_menu}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_create_context_menu", new Object[]{menu, v, menuInfo});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateContextMenu}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onCreateContextMenu", new Object[]{menu, v, menuInfo});
      } else {
        {super.onCreateContextMenu(menu, v, menuInfo); return;}
      }
    }
  }

  public java.lang.CharSequence onCreateDescription() {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateDescription();
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateDescription");
      return super.onCreateDescription();
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onCreateDescription();
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_description}")) {
      return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, scriptInfo.getRubyInstance(), "on_create_description");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateDescription}")) {
        return (java.lang.CharSequence) JRubyAdapter.runRubyMethod(java.lang.CharSequence.class, scriptInfo.getRubyInstance(), "onCreateDescription");
      } else {
        return super.onCreateDescription();
      }
    }
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateOptionsMenu(menu);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateOptionsMenu");
      return super.onCreateOptionsMenu(menu);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onCreateOptionsMenu(menu);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_options_menu}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_create_options_menu", menu);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateOptionsMenu}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onCreateOptionsMenu", menu);
      } else {
        return super.onCreateOptionsMenu(menu);
      }
    }
  }

  public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreatePanelMenu(featureId, menu);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreatePanelMenu");
      return super.onCreatePanelMenu(featureId, menu);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onCreatePanelMenu(featureId, menu);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_panel_menu}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_create_panel_menu", new Object[]{featureId, menu});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreatePanelMenu}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onCreatePanelMenu", new Object[]{featureId, menu});
      } else {
        return super.onCreatePanelMenu(featureId, menu);
      }
    }
  }

  public android.view.View onCreatePanelView(int featureId) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreatePanelView(featureId);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreatePanelView");
      return super.onCreatePanelView(featureId);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onCreatePanelView(featureId);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_panel_view}")) {
      return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "on_create_panel_view", featureId);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreatePanelView}")) {
        return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "onCreatePanelView", featureId);
      } else {
        return super.onCreatePanelView(featureId);
      }
    }
  }

  public boolean onCreateThumbnail(android.graphics.Bitmap outBitmap, android.graphics.Canvas canvas) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateThumbnail(outBitmap, canvas);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateThumbnail");
      return super.onCreateThumbnail(outBitmap, canvas);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onCreateThumbnail(outBitmap, canvas);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_thumbnail}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_create_thumbnail", new Object[]{outBitmap, canvas});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateThumbnail}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onCreateThumbnail", new Object[]{outBitmap, canvas});
      } else {
        return super.onCreateThumbnail(outBitmap, canvas);
      }
    }
  }

  public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onCreateView(name, context, attrs);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onCreateView");
      return super.onCreateView(name, context, attrs);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onCreateView(name, context, attrs);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create_view}")) {
      return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "on_create_view", new Object[]{name, context, attrs});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreateView}")) {
        return (android.view.View) JRubyAdapter.runRubyMethod(android.view.View.class, scriptInfo.getRubyInstance(), "onCreateView", new Object[]{name, context, attrs});
      } else {
        return super.onCreateView(name, context, attrs);
      }
    }
  }

  public void onDestroy() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onDestroy(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onDestroy");
      {super.onDestroy(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onDestroy(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_destroy}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_destroy");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDestroy}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDestroy");
      } else {
        {super.onDestroy(); return;}
      }
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyDown(keyCode, event);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyDown");
      return super.onKeyDown(keyCode, event);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onKeyDown(keyCode, event);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_down}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_down", new Object[]{keyCode, event});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyDown}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyDown", new Object[]{keyCode, event});
      } else {
        return super.onKeyDown(keyCode, event);
      }
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyMultiple(keyCode, repeatCount, event);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyMultiple");
      return super.onKeyMultiple(keyCode, repeatCount, event);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onKeyMultiple(keyCode, repeatCount, event);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_multiple}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_multiple", new Object[]{keyCode, repeatCount, event});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyMultiple}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyMultiple", new Object[]{keyCode, repeatCount, event});
      } else {
        return super.onKeyMultiple(keyCode, repeatCount, event);
      }
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyUp(keyCode, event);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyUp");
      return super.onKeyUp(keyCode, event);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onKeyUp(keyCode, event);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_up}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_up", new Object[]{keyCode, event});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyUp}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyUp", new Object[]{keyCode, event});
      } else {
        return super.onKeyUp(keyCode, event);
      }
    }
  }

  public void onLowMemory() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onLowMemory(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onLowMemory");
      {super.onLowMemory(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onLowMemory(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_low_memory}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_low_memory");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onLowMemory}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onLowMemory");
      } else {
        {super.onLowMemory(); return;}
      }
    }
  }

  public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onMenuItemSelected(featureId, item);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onMenuItemSelected");
      return super.onMenuItemSelected(featureId, item);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onMenuItemSelected(featureId, item);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_menu_item_selected}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_menu_item_selected", new Object[]{featureId, item});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onMenuItemSelected}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onMenuItemSelected", new Object[]{featureId, item});
      } else {
        return super.onMenuItemSelected(featureId, item);
      }
    }
  }

  public boolean onMenuOpened(int featureId, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onMenuOpened(featureId, menu);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onMenuOpened");
      return super.onMenuOpened(featureId, menu);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onMenuOpened(featureId, menu);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_menu_opened}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_menu_opened", new Object[]{featureId, menu});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onMenuOpened}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onMenuOpened", new Object[]{featureId, menu});
      } else {
        return super.onMenuOpened(featureId, menu);
      }
    }
  }

  public void onNewIntent(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onNewIntent(intent); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onNewIntent");
      {super.onNewIntent(intent); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onNewIntent(intent); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_new_intent}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_new_intent", intent);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onNewIntent}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onNewIntent", intent);
      } else {
        {super.onNewIntent(intent); return;}
      }
    }
  }

  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onOptionsItemSelected(item);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onOptionsItemSelected");
      return super.onOptionsItemSelected(item);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onOptionsItemSelected(item);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_options_item_selected}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_options_item_selected", item);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onOptionsItemSelected}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onOptionsItemSelected", item);
      } else {
        return super.onOptionsItemSelected(item);
      }
    }
  }

  public void onOptionsMenuClosed(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onOptionsMenuClosed(menu); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onOptionsMenuClosed");
      {super.onOptionsMenuClosed(menu); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onOptionsMenuClosed(menu); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_options_menu_closed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_options_menu_closed", menu);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onOptionsMenuClosed}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onOptionsMenuClosed", menu);
      } else {
        {super.onOptionsMenuClosed(menu); return;}
      }
    }
  }

  public void onPanelClosed(int featureId, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPanelClosed(featureId, menu); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPanelClosed");
      {super.onPanelClosed(featureId, menu); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onPanelClosed(featureId, menu); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_panel_closed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_panel_closed", new Object[]{featureId, menu});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPanelClosed}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPanelClosed", new Object[]{featureId, menu});
      } else {
        {super.onPanelClosed(featureId, menu); return;}
      }
    }
  }

  public void onPause() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPause(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPause");
      {super.onPause(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onPause(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_pause}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_pause");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPause}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPause");
      } else {
        {super.onPause(); return;}
      }
    }
  }

  public void onPostCreate(android.os.Bundle savedInstanceState) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPostCreate(savedInstanceState); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPostCreate");
      {super.onPostCreate(savedInstanceState); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onPostCreate(savedInstanceState); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_post_create}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_post_create", savedInstanceState);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPostCreate}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPostCreate", savedInstanceState);
      } else {
        {super.onPostCreate(savedInstanceState); return;}
      }
    }
  }

  public void onPostResume() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onPostResume(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPostResume");
      {super.onPostResume(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onPostResume(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_post_resume}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_post_resume");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPostResume}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onPostResume");
      } else {
        {super.onPostResume(); return;}
      }
    }
  }

  public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onPrepareOptionsMenu(menu);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPrepareOptionsMenu");
      return super.onPrepareOptionsMenu(menu);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onPrepareOptionsMenu(menu);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_prepare_options_menu}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_prepare_options_menu", menu);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPrepareOptionsMenu}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onPrepareOptionsMenu", menu);
      } else {
        return super.onPrepareOptionsMenu(menu);
      }
    }
  }

  public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onPreparePanel(featureId, view, menu);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onPreparePanel");
      return super.onPreparePanel(featureId, view, menu);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onPreparePanel(featureId, view, menu);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_prepare_panel}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_prepare_panel", new Object[]{featureId, view, menu});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onPreparePanel}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onPreparePanel", new Object[]{featureId, view, menu});
      } else {
        return super.onPreparePanel(featureId, view, menu);
      }
    }
  }

  public void onRestart() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onRestart(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onRestart");
      {super.onRestart(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onRestart(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_restart}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_restart");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRestart}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRestart");
      } else {
        {super.onRestart(); return;}
      }
    }
  }

  public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onRestoreInstanceState(savedInstanceState); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onRestoreInstanceState");
      {super.onRestoreInstanceState(savedInstanceState); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onRestoreInstanceState(savedInstanceState); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_restore_instance_state}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_restore_instance_state", savedInstanceState);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRestoreInstanceState}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRestoreInstanceState", savedInstanceState);
      } else {
        {super.onRestoreInstanceState(savedInstanceState); return;}
      }
    }
  }

  public void onResume() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onResume(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onResume");
      {super.onResume(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onResume(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_resume}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_resume");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onResume}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onResume");
      } else {
        {super.onResume(); return;}
      }
    }
  }

  public void onSaveInstanceState(android.os.Bundle outState) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onSaveInstanceState(outState); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onSaveInstanceState");
      {super.onSaveInstanceState(outState); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onSaveInstanceState(outState); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_save_instance_state}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_save_instance_state", outState);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onSaveInstanceState}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onSaveInstanceState", outState);
      } else {
        {super.onSaveInstanceState(outState); return;}
      }
    }
  }

  public boolean onSearchRequested() {
    if (ScriptLoader.isCalledFromJRuby()) return super.onSearchRequested();
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onSearchRequested");
      return super.onSearchRequested();
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onSearchRequested();
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_search_requested}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_search_requested");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onSearchRequested}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onSearchRequested");
      } else {
        return super.onSearchRequested();
      }
    }
  }

  public void onStart() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onStart(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onStart");
      {super.onStart(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onStart(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_start");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStart}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onStart");
      } else {
        {super.onStart(); return;}
      }
    }
  }

  public void onStop() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onStop(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onStop");
      {super.onStop(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onStop(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_stop}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_stop");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStop}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onStop");
      } else {
        {super.onStop(); return;}
      }
    }
  }

  public void onTitleChanged(java.lang.CharSequence title, int color) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onTitleChanged(title, color); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onTitleChanged");
      {super.onTitleChanged(title, color); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onTitleChanged(title, color); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_title_changed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_title_changed", new Object[]{title, color});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTitleChanged}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onTitleChanged", new Object[]{title, color});
      } else {
        {super.onTitleChanged(title, color); return;}
      }
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onTouchEvent(event);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onTouchEvent");
      return super.onTouchEvent(event);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onTouchEvent(event);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_touch_event}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_touch_event", event);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTouchEvent}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onTouchEvent", event);
      } else {
        return super.onTouchEvent(event);
      }
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onTrackballEvent(event);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onTrackballEvent");
      return super.onTrackballEvent(event);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onTrackballEvent(event);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_trackball_event}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_trackball_event", event);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onTrackballEvent}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onTrackballEvent", event);
      } else {
        return super.onTrackballEvent(event);
      }
    }
  }

  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onWindowAttributesChanged(params); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onWindowAttributesChanged");
      {super.onWindowAttributesChanged(params); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onWindowAttributesChanged(params); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_window_attributes_changed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_window_attributes_changed", params);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onWindowAttributesChanged}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onWindowAttributesChanged", params);
      } else {
        {super.onWindowAttributesChanged(params); return;}
      }
    }
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onWindowFocusChanged(hasFocus); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onWindowFocusChanged");
      {super.onWindowFocusChanged(hasFocus); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onWindowFocusChanged(hasFocus); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_window_focus_changed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_window_focus_changed", hasFocus);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onWindowFocusChanged}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onWindowFocusChanged", hasFocus);
      } else {
        {super.onWindowFocusChanged(hasFocus); return;}
      }
    }
  }

  public void onUserInteraction() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onUserInteraction(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onUserInteraction");
      {super.onUserInteraction(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onUserInteraction(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_user_interaction}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_user_interaction");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUserInteraction}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onUserInteraction");
      } else {
        {super.onUserInteraction(); return;}
      }
    }
  }

  public void onUserLeaveHint() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onUserLeaveHint(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onUserLeaveHint");
      {super.onUserLeaveHint(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onUserLeaveHint(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_user_leave_hint}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_user_leave_hint");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUserLeaveHint}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onUserLeaveHint");
      } else {
        {super.onUserLeaveHint(); return;}
      }
    }
  }

  public void onAttachedToWindow() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onAttachedToWindow(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onAttachedToWindow");
      {super.onAttachedToWindow(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onAttachedToWindow(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_attached_to_window}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_attached_to_window");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onAttachedToWindow}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onAttachedToWindow");
      } else {
        {super.onAttachedToWindow(); return;}
      }
    }
  }

  public void onBackPressed() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onBackPressed(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onBackPressed");
      {super.onBackPressed(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onBackPressed(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_back_pressed}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_back_pressed");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBackPressed}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onBackPressed");
      } else {
        {super.onBackPressed(); return;}
      }
    }
  }

  public void onDetachedFromWindow() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onDetachedFromWindow(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onDetachedFromWindow");
      {super.onDetachedFromWindow(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onDetachedFromWindow(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_detached_from_window}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_detached_from_window");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDetachedFromWindow}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDetachedFromWindow");
      } else {
        {super.onDetachedFromWindow(); return;}
      }
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onKeyLongPress(keyCode, event);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onKeyLongPress");
      return super.onKeyLongPress(keyCode, event);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onKeyLongPress(keyCode, event);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_key_long_press}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_key_long_press", new Object[]{keyCode, event});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onKeyLongPress}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onKeyLongPress", new Object[]{keyCode, event});
      } else {
        return super.onKeyLongPress(keyCode, event);
      }
    }
  }

  public void onApplyThemeResource(android.content.res.Resources.Theme theme, int resid, boolean first) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onApplyThemeResource(theme, resid, first); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onApplyThemeResource");
      {super.onApplyThemeResource(theme, resid, first); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onApplyThemeResource(theme, resid, first); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_apply_theme_resource}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_apply_theme_resource", new Object[]{theme, resid, first});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onApplyThemeResource}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onApplyThemeResource", new Object[]{theme, resid, first});
      } else {
        {super.onApplyThemeResource(theme, resid, first); return;}
      }
    }
  }

}
