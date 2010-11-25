package org.ruboto;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;
import java.io.IOException;
import android.app.ProgressDialog;

public class RubotoActivity extends android.app.Activity implements android.view.View.OnTouchListener, android.view.View.OnKeyListener, android.content.DialogInterface.OnClickListener, android.app.DatePickerDialog.OnDateSetListener, android.app.TimePickerDialog.OnTimeSetListener, android.widget.TabHost.OnTabChangeListener, android.widget.TextView.OnEditorActionListener, android.widget.DatePicker.OnDateChangedListener, android.widget.TimePicker.OnTimeChangedListener, android.hardware.SensorEventListener {
  private Ruby __ruby__;
  private String scriptName;
  private String remoteVariable = "";
  public Object[] args;

  public static final int CB_ACTIVITY_RESULT = 0;
  public static final int CB_CHILD_TITLE_CHANGED = 1;
  public static final int CB_CONFIGURATION_CHANGED = 2;
  public static final int CB_CONTENT_CHANGED = 3;
  public static final int CB_CONTEXT_ITEM_SELECTED = 4;
  public static final int CB_CONTEXT_MENU_CLOSED = 5;
  public static final int CB_CREATE_CONTEXT_MENU = 6;
  public static final int CB_CREATE_DESCRIPTION = 7;
  public static final int CB_CREATE_DIALOG = 8;
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
  public static final int CB_PREPARE_DIALOG = 28;
  public static final int CB_PREPARE_OPTIONS_MENU = 29;
  public static final int CB_PREPARE_PANEL = 30;
  public static final int CB_RESTART = 31;
  public static final int CB_RESTORE_INSTANCE_STATE = 32;
  public static final int CB_RESUME = 33;
  public static final int CB_RETAIN_NON_CONFIGURATION_INSTANCE = 34;
  public static final int CB_SAVE_INSTANCE_STATE = 35;
  public static final int CB_SEARCH_REQUESTED = 36;
  public static final int CB_START = 37;
  public static final int CB_STOP = 38;
  public static final int CB_TITLE_CHANGED = 39;
  public static final int CB_TOUCH_EVENT = 40;
  public static final int CB_TRACKBALL_EVENT = 41;
  public static final int CB_WINDOW_ATTRIBUTES_CHANGED = 42;
  public static final int CB_WINDOW_FOCUS_CHANGED = 43;
  public static final int CB_USER_INTERACTION = 44;
  public static final int CB_USER_LEAVE_HINT = 45;
  public static final int CB_ATTACHED_TO_WINDOW = 46;
  public static final int CB_DETACHED_FROM_WINDOW = 47;
  public static final int CB_KEY_LONG_PRESS = 48;
  public static final int CB_APPLY_THEME_RESOURCE = 49;
  public static final int CB_TOUCH = 50;
  public static final int CB_KEY = 51;
  public static final int CB_CLICK = 52;
  public static final int CB_DATE_SET = 53;
  public static final int CB_TIME_SET = 54;
  public static final int CB_TAB_CHANGED = 55;
  public static final int CB_EDITOR_ACTION = 56;
  public static final int CB_DATE_CHANGED = 57;
  public static final int CB_TIME_CHANGED = 58;
  public static final int CB_ACCURACY_CHANGED = 59;
  public static final int CB_SENSOR_CHANGED = 60;
  private IRubyObject[] callbackProcs = new IRubyObject[63];

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();

    if (__ruby__ == null) {
      Script.setUpJRuby(null);
      __ruby__ = Script.getRuby();
    }

    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public RubotoActivity setRemoteVariable(String var) {
    remoteVariable = ((var == null) ? "" : (var + "."));
    return this;
  }

  public void setScriptName(String name){
    scriptName = name;
  }

  /****************************************************************************************
   * 
   *  Activity Lifecycle: onCreate
   */
	
  @Override
  public void onCreate(android.os.Bundle arg0) {
    args = new Object[1];
    args[0] = arg0;

    super.onCreate(arg0);

    Script.copyScriptsIfNeeded(getFilesDir().getAbsolutePath() + "/scripts", getAssets());

    getRuby();

    Script.defineGlobalVariable("$activity", this);
    Script.defineGlobalVariable("$bundle", arg0);

    android.os.Bundle configBundle = getIntent().getBundleExtra("RubotoActivity Config");

    if (configBundle != null) {
      setRemoteVariable(configBundle.getString("Remote Variable"));
      if (configBundle.getBoolean("Define Remote Variable")) {
        Script.defineGlobalVariable(configBundle.getString("Remote Variable"), this);
        setRemoteVariable(configBundle.getString("Remote Variable"));
      }
      if (configBundle.getString("Initialize Script") != null) {
        Script.execute(configBundle.getString("Initialize Script"));
      }
      Script.execute(remoteVariable + "on_create($bundle)");
    } else {
      try {
        new Script(scriptName).execute();
      } catch(IOException e){
        e.printStackTrace();
        ProgressDialog.show(this, "Script failed", "Something bad happened", true, false);
      }
    }
  }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    if (callbackProcs[CB_ACTIVITY_RESULT] != null) {
      super.onActivityResult(requestCode, resultCode, data);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ACTIVITY_RESULT], "call" , JavaUtil.convertJavaToRuby(getRuby(), requestCode), JavaUtil.convertJavaToRuby(getRuby(), resultCode), JavaUtil.convertJavaToRuby(getRuby(), data));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  public void onChildTitleChanged(android.app.Activity childActivity, java.lang.CharSequence title) {
    if (callbackProcs[CB_CHILD_TITLE_CHANGED] != null) {
      super.onChildTitleChanged(childActivity, title);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CHILD_TITLE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), childActivity), JavaUtil.convertJavaToRuby(getRuby(), title));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onChildTitleChanged(childActivity, title);
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
      super.onConfigurationChanged(newConfig);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CONFIGURATION_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), newConfig));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onConfigurationChanged(newConfig);
    }
  }

  public void onContentChanged() {
    if (callbackProcs[CB_CONTENT_CHANGED] != null) {
      super.onContentChanged();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CONTENT_CHANGED], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onContentChanged();
    }
  }

  public boolean onContextItemSelected(android.view.MenuItem item) {
    if (callbackProcs[CB_CONTEXT_ITEM_SELECTED] != null) {
      super.onContextItemSelected(item);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CONTEXT_ITEM_SELECTED], "call" , JavaUtil.convertJavaToRuby(getRuby(), item)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onContextItemSelected(item);
    }
  }

  public void onContextMenuClosed(android.view.Menu menu) {
    if (callbackProcs[CB_CONTEXT_MENU_CLOSED] != null) {
      super.onContextMenuClosed(menu);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CONTEXT_MENU_CLOSED], "call" , JavaUtil.convertJavaToRuby(getRuby(), menu));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onContextMenuClosed(menu);
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
    if (callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
      super.onCreateContextMenu(menu, v, menuInfo);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , JavaUtil.convertJavaToRuby(getRuby(), menu), JavaUtil.convertJavaToRuby(getRuby(), v), JavaUtil.convertJavaToRuby(getRuby(), menuInfo));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onCreateContextMenu(menu, v, menuInfo);
    }
  }

  public java.lang.CharSequence onCreateDescription() {
    if (callbackProcs[CB_CREATE_DESCRIPTION] != null) {
      super.onCreateDescription();
      try {
        return (java.lang.CharSequence)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_DESCRIPTION], "call" ).toJava(java.lang.CharSequence.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return super.onCreateDescription();
    }
  }

  public android.app.Dialog onCreateDialog(int id) {
    if (callbackProcs[CB_CREATE_DIALOG] != null) {
      super.onCreateDialog(id);
      try {
        return (android.app.Dialog)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_DIALOG], "call" , JavaUtil.convertJavaToRuby(getRuby(), id)).toJava(android.app.Dialog.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return super.onCreateDialog(id);
    }
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    if (callbackProcs[CB_CREATE_OPTIONS_MENU] != null) {
      super.onCreateOptionsMenu(menu);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_OPTIONS_MENU], "call" , JavaUtil.convertJavaToRuby(getRuby(), menu)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onCreateOptionsMenu(menu);
    }
  }

  public boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
    if (callbackProcs[CB_CREATE_PANEL_MENU] != null) {
      super.onCreatePanelMenu(featureId, menu);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_PANEL_MENU], "call" , JavaUtil.convertJavaToRuby(getRuby(), featureId), JavaUtil.convertJavaToRuby(getRuby(), menu)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onCreatePanelMenu(featureId, menu);
    }
  }

  public android.view.View onCreatePanelView(int featureId) {
    if (callbackProcs[CB_CREATE_PANEL_VIEW] != null) {
      super.onCreatePanelView(featureId);
      try {
        return (android.view.View)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_PANEL_VIEW], "call" , JavaUtil.convertJavaToRuby(getRuby(), featureId)).toJava(android.view.View.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return super.onCreatePanelView(featureId);
    }
  }

  public boolean onCreateThumbnail(android.graphics.Bitmap outBitmap, android.graphics.Canvas canvas) {
    if (callbackProcs[CB_CREATE_THUMBNAIL] != null) {
      super.onCreateThumbnail(outBitmap, canvas);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_THUMBNAIL], "call" , JavaUtil.convertJavaToRuby(getRuby(), outBitmap), JavaUtil.convertJavaToRuby(getRuby(), canvas)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onCreateThumbnail(outBitmap, canvas);
    }
  }

  public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
    if (callbackProcs[CB_CREATE_VIEW] != null) {
      super.onCreateView(name, context, attrs);
      try {
        return (android.view.View)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_VIEW], "call" , JavaUtil.convertJavaToRuby(getRuby(), name), JavaUtil.convertJavaToRuby(getRuby(), context), JavaUtil.convertJavaToRuby(getRuby(), attrs)).toJava(android.view.View.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return super.onCreateView(name, context, attrs);
    }
  }

  public void onDestroy() {
    if (callbackProcs[CB_DESTROY] != null) {
      super.onDestroy();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DESTROY], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onDestroy();
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_DOWN] != null) {
      super.onKeyDown(keyCode, event);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_KEY_DOWN], "call" , JavaUtil.convertJavaToRuby(getRuby(), keyCode), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_MULTIPLE] != null) {
      super.onKeyMultiple(keyCode, repeatCount, event);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_KEY_MULTIPLE], "call" , JavaUtil.convertJavaToRuby(getRuby(), keyCode), JavaUtil.convertJavaToRuby(getRuby(), repeatCount), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onKeyMultiple(keyCode, repeatCount, event);
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_UP] != null) {
      super.onKeyUp(keyCode, event);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_KEY_UP], "call" , JavaUtil.convertJavaToRuby(getRuby(), keyCode), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onKeyUp(keyCode, event);
    }
  }

  public void onLowMemory() {
    if (callbackProcs[CB_LOW_MEMORY] != null) {
      super.onLowMemory();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_LOW_MEMORY], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onLowMemory();
    }
  }

  public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
    if (callbackProcs[CB_MENU_ITEM_SELECTED] != null) {
      super.onMenuItemSelected(featureId, item);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_MENU_ITEM_SELECTED], "call" , JavaUtil.convertJavaToRuby(getRuby(), featureId), JavaUtil.convertJavaToRuby(getRuby(), item)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onMenuItemSelected(featureId, item);
    }
  }

  public boolean onMenuOpened(int featureId, android.view.Menu menu) {
    if (callbackProcs[CB_MENU_OPENED] != null) {
      super.onMenuOpened(featureId, menu);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_MENU_OPENED], "call" , JavaUtil.convertJavaToRuby(getRuby(), featureId), JavaUtil.convertJavaToRuby(getRuby(), menu)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onMenuOpened(featureId, menu);
    }
  }

  public void onNewIntent(android.content.Intent intent) {
    if (callbackProcs[CB_NEW_INTENT] != null) {
      super.onNewIntent(intent);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_NEW_INTENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), intent));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onNewIntent(intent);
    }
  }

  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (callbackProcs[CB_OPTIONS_ITEM_SELECTED] != null) {
      super.onOptionsItemSelected(item);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_OPTIONS_ITEM_SELECTED], "call" , JavaUtil.convertJavaToRuby(getRuby(), item)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void onOptionsMenuClosed(android.view.Menu menu) {
    if (callbackProcs[CB_OPTIONS_MENU_CLOSED] != null) {
      super.onOptionsMenuClosed(menu);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_OPTIONS_MENU_CLOSED], "call" , JavaUtil.convertJavaToRuby(getRuby(), menu));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onOptionsMenuClosed(menu);
    }
  }

  public void onPanelClosed(int featureId, android.view.Menu menu) {
    if (callbackProcs[CB_PANEL_CLOSED] != null) {
      super.onPanelClosed(featureId, menu);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PANEL_CLOSED], "call" , JavaUtil.convertJavaToRuby(getRuby(), featureId), JavaUtil.convertJavaToRuby(getRuby(), menu));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPanelClosed(featureId, menu);
    }
  }

  public void onPause() {
    if (callbackProcs[CB_PAUSE] != null) {
      super.onPause();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PAUSE], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPause();
    }
  }

  public void onPostCreate(android.os.Bundle savedInstanceState) {
    if (callbackProcs[CB_POST_CREATE] != null) {
      super.onPostCreate(savedInstanceState);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_POST_CREATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), savedInstanceState));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPostCreate(savedInstanceState);
    }
  }

  public void onPostResume() {
    if (callbackProcs[CB_POST_RESUME] != null) {
      super.onPostResume();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_POST_RESUME], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPostResume();
    }
  }

  public void onPrepareDialog(int id, android.app.Dialog dialog) {
    if (callbackProcs[CB_PREPARE_DIALOG] != null) {
      super.onPrepareDialog(id, dialog);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PREPARE_DIALOG], "call" , JavaUtil.convertJavaToRuby(getRuby(), id), JavaUtil.convertJavaToRuby(getRuby(), dialog));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPrepareDialog(id, dialog);
    }
  }

  public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    if (callbackProcs[CB_PREPARE_OPTIONS_MENU] != null) {
      super.onPrepareOptionsMenu(menu);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PREPARE_OPTIONS_MENU], "call" , JavaUtil.convertJavaToRuby(getRuby(), menu)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onPrepareOptionsMenu(menu);
    }
  }

  public boolean onPreparePanel(int featureId, android.view.View view, android.view.Menu menu) {
    if (callbackProcs[CB_PREPARE_PANEL] != null) {
      super.onPreparePanel(featureId, view, menu);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PREPARE_PANEL], "call" , JavaUtil.convertJavaToRuby(getRuby(), featureId), JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), menu)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onPreparePanel(featureId, view, menu);
    }
  }

  public void onRestart() {
    if (callbackProcs[CB_RESTART] != null) {
      super.onRestart();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_RESTART], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onRestart();
    }
  }

  public void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
    if (callbackProcs[CB_RESTORE_INSTANCE_STATE] != null) {
      super.onRestoreInstanceState(savedInstanceState);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_RESTORE_INSTANCE_STATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), savedInstanceState));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onRestoreInstanceState(savedInstanceState);
    }
  }

  public void onResume() {
    if (callbackProcs[CB_RESUME] != null) {
      super.onResume();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_RESUME], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onResume();
    }
  }

  public java.lang.Object onRetainNonConfigurationInstance() {
    if (callbackProcs[CB_RETAIN_NON_CONFIGURATION_INSTANCE] != null) {
      super.onRetainNonConfigurationInstance();
      try {
        return (java.lang.Object)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_RETAIN_NON_CONFIGURATION_INSTANCE], "call" ).toJava(java.lang.Object.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return super.onRetainNonConfigurationInstance();
    }
  }

  public void onSaveInstanceState(android.os.Bundle outState) {
    if (callbackProcs[CB_SAVE_INSTANCE_STATE] != null) {
      super.onSaveInstanceState(outState);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SAVE_INSTANCE_STATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), outState));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onSaveInstanceState(outState);
    }
  }

  public boolean onSearchRequested() {
    if (callbackProcs[CB_SEARCH_REQUESTED] != null) {
      super.onSearchRequested();
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SEARCH_REQUESTED], "call" ).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onSearchRequested();
    }
  }

  public void onStart() {
    if (callbackProcs[CB_START] != null) {
      super.onStart();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_START], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onStart();
    }
  }

  public void onStop() {
    if (callbackProcs[CB_STOP] != null) {
      super.onStop();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_STOP], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onStop();
    }
  }

  public void onTitleChanged(java.lang.CharSequence title, int color) {
    if (callbackProcs[CB_TITLE_CHANGED] != null) {
      super.onTitleChanged(title, color);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TITLE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), title), JavaUtil.convertJavaToRuby(getRuby(), color));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onTitleChanged(title, color);
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (callbackProcs[CB_TOUCH_EVENT] != null) {
      super.onTouchEvent(event);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TOUCH_EVENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onTouchEvent(event);
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (callbackProcs[CB_TRACKBALL_EVENT] != null) {
      super.onTrackballEvent(event);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TRACKBALL_EVENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onTrackballEvent(event);
    }
  }

  public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams params) {
    if (callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED] != null) {
      super.onWindowAttributesChanged(params);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_WINDOW_ATTRIBUTES_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), params));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onWindowAttributesChanged(params);
    }
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
      super.onWindowFocusChanged(hasFocus);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), hasFocus));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onWindowFocusChanged(hasFocus);
    }
  }

  public void onUserInteraction() {
    if (callbackProcs[CB_USER_INTERACTION] != null) {
      super.onUserInteraction();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_USER_INTERACTION], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onUserInteraction();
    }
  }

  public void onUserLeaveHint() {
    if (callbackProcs[CB_USER_LEAVE_HINT] != null) {
      super.onUserLeaveHint();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_USER_LEAVE_HINT], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onUserLeaveHint();
    }
  }

  public void onAttachedToWindow() {
    if (callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onDetachedFromWindow() {
    if (callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_LONG_PRESS] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_KEY_LONG_PRESS], "call" , JavaUtil.convertJavaToRuby(getRuby(), keyCode), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public android.app.Dialog onCreateDialog(int id, android.os.Bundle args) {
    if (callbackProcs[CB_CREATE_DIALOG] != null) {
      try {
        return (android.app.Dialog)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_DIALOG], "call" , JavaUtil.convertJavaToRuby(getRuby(), id), JavaUtil.convertJavaToRuby(getRuby(), args)).toJava(android.app.Dialog.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public void onPrepareDialog(int id, android.app.Dialog dialog, android.os.Bundle args) {
    if (callbackProcs[CB_PREPARE_DIALOG] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PREPARE_DIALOG], "call" , JavaUtil.convertJavaToRuby(getRuby(), id), JavaUtil.convertJavaToRuby(getRuby(), dialog), JavaUtil.convertJavaToRuby(getRuby(), args));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onApplyThemeResource(android.content.res.Resources.Theme theme, int resid, boolean first) {
    if (callbackProcs[CB_APPLY_THEME_RESOURCE] != null) {
      super.onApplyThemeResource(theme, resid, first);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_APPLY_THEME_RESOURCE], "call" , JavaUtil.convertJavaToRuby(getRuby(), theme), JavaUtil.convertJavaToRuby(getRuby(), resid), JavaUtil.convertJavaToRuby(getRuby(), first));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onApplyThemeResource(theme, resid, first);
    }
  }

  public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
    if (callbackProcs[CB_TOUCH] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TOUCH], "call" , JavaUtil.convertJavaToRuby(getRuby(), v), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_KEY], "call" , JavaUtil.convertJavaToRuby(getRuby(), v), JavaUtil.convertJavaToRuby(getRuby(), keyCode), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public void onClick(android.content.DialogInterface dialog, int which) {
    if (callbackProcs[CB_CLICK] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CLICK], "call" , JavaUtil.convertJavaToRuby(getRuby(), dialog), JavaUtil.convertJavaToRuby(getRuby(), which));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    if (callbackProcs[CB_DATE_SET] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), year), JavaUtil.convertJavaToRuby(getRuby(), monthOfYear), JavaUtil.convertJavaToRuby(getRuby(), dayOfMonth)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DATE_SET], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
    if (callbackProcs[CB_TIME_SET] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TIME_SET], "call" , JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), hourOfDay), JavaUtil.convertJavaToRuby(getRuby(), minute));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onTabChanged(java.lang.String tabId) {
    if (callbackProcs[CB_TAB_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TAB_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), tabId));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
    if (callbackProcs[CB_EDITOR_ACTION] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_EDITOR_ACTION], "call" , JavaUtil.convertJavaToRuby(getRuby(), v), JavaUtil.convertJavaToRuby(getRuby(), actionId), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public void onDateChanged(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    if (callbackProcs[CB_DATE_CHANGED] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), year), JavaUtil.convertJavaToRuby(getRuby(), monthOfYear), JavaUtil.convertJavaToRuby(getRuby(), dayOfMonth)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DATE_CHANGED], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
    if (callbackProcs[CB_TIME_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TIME_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), hourOfDay), JavaUtil.convertJavaToRuby(getRuby(), minute));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    if (callbackProcs[CB_ACCURACY_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ACCURACY_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), sensor), JavaUtil.convertJavaToRuby(getRuby(), accuracy));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onSensorChanged(android.hardware.SensorEvent event) {
    if (callbackProcs[CB_SENSOR_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SENSOR_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), event));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}	

