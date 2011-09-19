package org.ruboto;

import java.io.IOException;

import org.ruboto.Script;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RubotoActivity extends android.app.Activity {
    private String scriptName;
    private String remoteVariable = null;
    private Object[] args;
    private Bundle configBundle;

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
  public static final int CB_RETAIN_NON_CONFIGURATION_INSTANCE = 32;
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
    
        if (Script.isInitialized()) {
            prepareJRuby();
    	    loadScript();
        }
    }

    // This causes JRuby to initialize and takes while
    protected void prepareJRuby() {
        Script.put("$context", this);
        Script.put("$activity", this);
        Script.put("$bundle", args[0]);
    }

    protected void loadScript() {
        try {
            if (scriptName != null) {
                new Script(scriptName).execute(); doesn't this work:
            } else {
                // TODO: Why doesn't this work? 
                // Script.callMethod(this, "initialize_ruboto");
                Script.execute("$activity.initialize_ruboto");
                // TODO: Why doesn't this work? 
                // Script.callMethod(this, "on_create", args[0]);
                Script.execute("$activity.on_create($bundle)");
            }
        } catch(IOException e){
            e.printStackTrace();
            ProgressDialog.show(this, "Script failed", "Something bad happened", true, true);
        }
    }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    if (callbackProcs[CB_ACTIVITY_RESULT] != null) {
      super.onActivityResult(requestCode, resultCode, data);
      Script.callMethod(callbackProcs[CB_ACTIVITY_RESULT], "call" , new Object[]{requestCode, resultCode, data});
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  public void onChildTitleChanged(android.app.Activity childActivity, java.lang.CharSequence title) {
    if (callbackProcs[CB_CHILD_TITLE_CHANGED] != null) {
      super.onChildTitleChanged(childActivity, title);
      Script.callMethod(callbackProcs[CB_CHILD_TITLE_CHANGED], "call" , new Object[]{childActivity, title});
    } else {
      super.onChildTitleChanged(childActivity, title);
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
      super.onConfigurationChanged(newConfig);
      Script.callMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
    } else {
      super.onConfigurationChanged(newConfig);
    }
  }

  public void onContentChanged() {
    if (callbackProcs[CB_CONTENT_CHANGED] != null) {
      super.onContentChanged();
      Script.callMethod(callbackProcs[CB_CONTENT_CHANGED], "call" );
    } else {
      super.onContentChanged();
    }
  }

  public boolean onContextItemSelected(android.view.MenuItem item) {
    if (callbackProcs[CB_CONTEXT_ITEM_SELECTED] != null) {
      super.onContextItemSelected(item);
      return (Boolean) Script.callMethod(callbackProcs[CB_CONTEXT_ITEM_SELECTED], "call" , item, Boolean.class);
    } else {
      return super.onContextItemSelected(item);
    }
  }

  public void onContextMenuClosed(android.view.Menu menu) {
    if (callbackProcs[CB_CONTEXT_MENU_CLOSED] != null) {
      super.onContextMenuClosed(menu);
      Script.callMethod(callbackProcs[CB_CONTEXT_MENU_CLOSED], "call" , menu);
    } else {
      super.onContextMenuClosed(menu);
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    if (callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
      super.onCreateContextMenu(menu, v, menuInfo);
      Script.callMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , new Object[]{menu, v, menuInfo});
    } else {
      super.onCreateContextMenu(menu, v, menuInfo);
    }
  }

  public java.lang.CharSequence onCreateDescription() {
    if (callbackProcs[CB_CREATE_DESCRIPTION] != null) {
      super.onCreateDescription();
      return (java.lang.CharSequence) Script.callMethod(callbackProcs[CB_CREATE_DESCRIPTION], "call" , java.lang.CharSequence.class);
    } else {
      return super.onCreateDescription();
    }
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    if (callbackProcs[CB_CREATE_OPTIONS_MENU] != null) {
      super.onCreateOptionsMenu(menu);
      return (Boolean) Script.callMethod(callbackProcs[CB_CREATE_OPTIONS_MENU], "call" , menu, Boolean.class);
    } else {
      return super.onCreateOptionsMenu(menu);
    }
  }

  public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
    if (callbackProcs[CB_CREATE_PANEL_MENU] != null) {
      super.onCreatePanelMenu(featureId, menu);
      return (Boolean) Script.callMethod(callbackProcs[CB_CREATE_PANEL_MENU], "call" , new Object[]{featureId, menu}, Boolean.class);
    } else {
      return super.onCreatePanelMenu(featureId, menu);
    }
  }

  public android.view.View onCreatePanelView(int featureId) {
    if (callbackProcs[CB_CREATE_PANEL_VIEW] != null) {
      super.onCreatePanelView(featureId);
      return (android.view.View) Script.callMethod(callbackProcs[CB_CREATE_PANEL_VIEW], "call" , featureId, android.view.View.class);
    } else {
      return super.onCreatePanelView(featureId);
    }
  }

  public boolean onCreateThumbnail(android.graphics.Bitmap outBitmap, android.graphics.Canvas canvas) {
    if (callbackProcs[CB_CREATE_THUMBNAIL] != null) {
      super.onCreateThumbnail(outBitmap, canvas);
      return (Boolean) Script.callMethod(callbackProcs[CB_CREATE_THUMBNAIL], "call" , new Object[]{outBitmap, canvas}, Boolean.class);
    } else {
      return super.onCreateThumbnail(outBitmap, canvas);
    }
  }

  public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
    if (callbackProcs[CB_CREATE_VIEW] != null) {
      super.onCreateView(name, context, attrs);
      return (android.view.View) Script.callMethod(callbackProcs[CB_CREATE_VIEW], "call" , new Object[]{name, context, attrs}, android.view.View.class);
    } else {
      return super.onCreateView(name, context, attrs);
    }
  }

  public void onDestroy() {
    if (callbackProcs[CB_DESTROY] != null) {
      super.onDestroy();
      Script.callMethod(callbackProcs[CB_DESTROY], "call" );
    } else {
      super.onDestroy();
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_DOWN] != null) {
      super.onKeyDown(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_DOWN], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_MULTIPLE] != null) {
      super.onKeyMultiple(keyCode, repeatCount, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_MULTIPLE], "call" , new Object[]{keyCode, repeatCount, event}, Boolean.class);
    } else {
      return super.onKeyMultiple(keyCode, repeatCount, event);
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_UP] != null) {
      super.onKeyUp(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_UP], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyUp(keyCode, event);
    }
  }

  public void onLowMemory() {
    if (callbackProcs[CB_LOW_MEMORY] != null) {
      super.onLowMemory();
      Script.callMethod(callbackProcs[CB_LOW_MEMORY], "call" );
    } else {
      super.onLowMemory();
    }
  }

  public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    if (callbackProcs[CB_MENU_ITEM_SELECTED] != null) {
      super.onMenuItemSelected(featureId, item);
      return (Boolean) Script.callMethod(callbackProcs[CB_MENU_ITEM_SELECTED], "call" , new Object[]{featureId, item}, Boolean.class);
    } else {
      return super.onMenuItemSelected(featureId, item);
    }
  }

  public boolean onMenuOpened(int featureId, android.view.Menu menu) {
    if (callbackProcs[CB_MENU_OPENED] != null) {
      super.onMenuOpened(featureId, menu);
      return (Boolean) Script.callMethod(callbackProcs[CB_MENU_OPENED], "call" , new Object[]{featureId, menu}, Boolean.class);
    } else {
      return super.onMenuOpened(featureId, menu);
    }
  }

  public void onNewIntent(android.content.Intent intent) {
    if (callbackProcs[CB_NEW_INTENT] != null) {
      super.onNewIntent(intent);
      Script.callMethod(callbackProcs[CB_NEW_INTENT], "call" , intent);
    } else {
      super.onNewIntent(intent);
    }
  }

  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (callbackProcs[CB_OPTIONS_ITEM_SELECTED] != null) {
      super.onOptionsItemSelected(item);
      return (Boolean) Script.callMethod(callbackProcs[CB_OPTIONS_ITEM_SELECTED], "call" , item, Boolean.class);
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void onOptionsMenuClosed(android.view.Menu menu) {
    if (callbackProcs[CB_OPTIONS_MENU_CLOSED] != null) {
      super.onOptionsMenuClosed(menu);
      Script.callMethod(callbackProcs[CB_OPTIONS_MENU_CLOSED], "call" , menu);
    } else {
      super.onOptionsMenuClosed(menu);
    }
  }

  public void onPanelClosed(int featureId, android.view.Menu menu) {
    if (callbackProcs[CB_PANEL_CLOSED] != null) {
      super.onPanelClosed(featureId, menu);
      Script.callMethod(callbackProcs[CB_PANEL_CLOSED], "call" , new Object[]{featureId, menu});
    } else {
      super.onPanelClosed(featureId, menu);
    }
  }

  public void onPause() {
    if (callbackProcs[CB_PAUSE] != null) {
      super.onPause();
      Script.callMethod(callbackProcs[CB_PAUSE], "call" );
    } else {
      super.onPause();
    }
  }

  public void onPostCreate(android.os.Bundle savedInstanceState) {
    if (callbackProcs[CB_POST_CREATE] != null) {
      super.onPostCreate(savedInstanceState);
      Script.callMethod(callbackProcs[CB_POST_CREATE], "call" , savedInstanceState);
    } else {
      super.onPostCreate(savedInstanceState);
    }
  }

  public void onPostResume() {
    if (callbackProcs[CB_POST_RESUME] != null) {
      super.onPostResume();
      Script.callMethod(callbackProcs[CB_POST_RESUME], "call" );
    } else {
      super.onPostResume();
    }
  }

  public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    if (callbackProcs[CB_PREPARE_OPTIONS_MENU] != null) {
      super.onPrepareOptionsMenu(menu);
      return (Boolean) Script.callMethod(callbackProcs[CB_PREPARE_OPTIONS_MENU], "call" , menu, Boolean.class);
    } else {
      return super.onPrepareOptionsMenu(menu);
    }
  }

  public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
    if (callbackProcs[CB_PREPARE_PANEL] != null) {
      super.onPreparePanel(featureId, view, menu);
      return (Boolean) Script.callMethod(callbackProcs[CB_PREPARE_PANEL], "call" , new Object[]{featureId, view, menu}, Boolean.class);
    } else {
      return super.onPreparePanel(featureId, view, menu);
    }
  }

  public void onRestart() {
    if (callbackProcs[CB_RESTART] != null) {
      super.onRestart();
      Script.callMethod(callbackProcs[CB_RESTART], "call" );
    } else {
      super.onRestart();
    }
  }

  public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
    if (callbackProcs[CB_RESTORE_INSTANCE_STATE] != null) {
      super.onRestoreInstanceState(savedInstanceState);
      Script.callMethod(callbackProcs[CB_RESTORE_INSTANCE_STATE], "call" , savedInstanceState);
    } else {
      super.onRestoreInstanceState(savedInstanceState);
    }
  }

  public void onResume() {
    if (callbackProcs[CB_RESUME] != null) {
      super.onResume();
      Script.callMethod(callbackProcs[CB_RESUME], "call" );
    } else {
      super.onResume();
    }
  }

  public java.lang.Object onRetainNonConfigurationInstance() {
    if (callbackProcs[CB_RETAIN_NON_CONFIGURATION_INSTANCE] != null) {
      super.onRetainNonConfigurationInstance();
      return (java.lang.Object) Script.callMethod(callbackProcs[CB_RETAIN_NON_CONFIGURATION_INSTANCE], "call" , java.lang.Object.class);
    } else {
      return super.onRetainNonConfigurationInstance();
    }
  }

  public void onSaveInstanceState(android.os.Bundle outState) {
    if (callbackProcs[CB_SAVE_INSTANCE_STATE] != null) {
      super.onSaveInstanceState(outState);
      Script.callMethod(callbackProcs[CB_SAVE_INSTANCE_STATE], "call" , outState);
    } else {
      super.onSaveInstanceState(outState);
    }
  }

  public boolean onSearchRequested() {
    if (callbackProcs[CB_SEARCH_REQUESTED] != null) {
      super.onSearchRequested();
      return (Boolean) Script.callMethod(callbackProcs[CB_SEARCH_REQUESTED], "call" , Boolean.class);
    } else {
      return super.onSearchRequested();
    }
  }

  public void onStart() {
    if (callbackProcs[CB_START] != null) {
      super.onStart();
      Script.callMethod(callbackProcs[CB_START], "call" );
    } else {
      super.onStart();
    }
  }

  public void onStop() {
    if (callbackProcs[CB_STOP] != null) {
      super.onStop();
      Script.callMethod(callbackProcs[CB_STOP], "call" );
    } else {
      super.onStop();
    }
  }

  public void onTitleChanged(java.lang.CharSequence title, int color) {
    if (callbackProcs[CB_TITLE_CHANGED] != null) {
      super.onTitleChanged(title, color);
      Script.callMethod(callbackProcs[CB_TITLE_CHANGED], "call" , new Object[]{title, color});
    } else {
      super.onTitleChanged(title, color);
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (callbackProcs[CB_TOUCH_EVENT] != null) {
      super.onTouchEvent(event);
      return (Boolean) Script.callMethod(callbackProcs[CB_TOUCH_EVENT], "call" , event, Boolean.class);
    } else {
      return super.onTouchEvent(event);
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (callbackProcs[CB_TRACKBALL_EVENT] != null) {
      super.onTrackballEvent(event);
      return (Boolean) Script.callMethod(callbackProcs[CB_TRACKBALL_EVENT], "call" , event, Boolean.class);
    } else {
      return super.onTrackballEvent(event);
    }
  }

  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    if (callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED] != null) {
      super.onWindowAttributesChanged(params);
      Script.callMethod(callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED], "call" , params);
    } else {
      super.onWindowAttributesChanged(params);
    }
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
      super.onWindowFocusChanged(hasFocus);
      Script.callMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , hasFocus);
    } else {
      super.onWindowFocusChanged(hasFocus);
    }
  }

  public void onUserInteraction() {
    if (callbackProcs[CB_USER_INTERACTION] != null) {
      super.onUserInteraction();
      Script.callMethod(callbackProcs[CB_USER_INTERACTION], "call" );
    } else {
      super.onUserInteraction();
    }
  }

  public void onUserLeaveHint() {
    if (callbackProcs[CB_USER_LEAVE_HINT] != null) {
      super.onUserLeaveHint();
      Script.callMethod(callbackProcs[CB_USER_LEAVE_HINT], "call" );
    } else {
      super.onUserLeaveHint();
    }
  }

  public void onAttachedToWindow() {
    if (callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
      super.onAttachedToWindow();
      Script.callMethod(callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
    } else {
      super.onAttachedToWindow();
    }
  }

  public void onBackPressed() {
    if (callbackProcs[CB_BACK_PRESSED] != null) {
      super.onBackPressed();
      Script.callMethod(callbackProcs[CB_BACK_PRESSED], "call" );
    } else {
      super.onBackPressed();
    }
  }

  public void onDetachedFromWindow() {
    if (callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
      super.onDetachedFromWindow();
      Script.callMethod(callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
    } else {
      super.onDetachedFromWindow();
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_LONG_PRESS] != null) {
      super.onKeyLongPress(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyLongPress(keyCode, event);
    }
  }

}
