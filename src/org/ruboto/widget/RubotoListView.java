package org.ruboto.widget;

import org.ruboto.Script;

public class RubotoListView extends android.widget.ListView {

  public static final int CB_MEASURE = 0;
  public static final int CB_SIZE_CHANGED = 1;
  public static final int CB_FOCUS_CHANGED = 2;
  public static final int CB_KEY_DOWN = 3;
  public static final int CB_KEY_UP = 4;
  public static final int CB_KEY_MULTIPLE = 5;
  public static final int CB_TOUCH_EVENT = 6;
  public static final int CB_FINISH_INFLATE = 7;
  public static final int CB_FILTER_COMPLETE = 8;
  public static final int CB_GLOBAL_LAYOUT = 9;
  public static final int CB_RESTORE_INSTANCE_STATE = 10;
  public static final int CB_SAVE_INSTANCE_STATE = 11;
  public static final int CB_TEXT_CHANGED = 12;
  public static final int CB_TOUCH_MODE_CHANGED = 13;
  public static final int CB_DETACHED_FROM_WINDOW = 14;
  public static final int CB_LAYOUT = 15;
  public static final int CB_INTERCEPT_TOUCH_EVENT = 16;
  public static final int CB_CREATE_DRAWABLE_STATE = 17;
  public static final int CB_WINDOW_FOCUS_CHANGED = 18;
  public static final int CB_DISPLAY_HINT = 19;
  public static final int CB_CREATE_INPUT_CONNECTION = 20;
  public static final int CB_ATTACHED_TO_WINDOW = 21;
  public static final int CB_OVER_SCROLLED = 22;
  public static final int CB_REMOTE_ADAPTER_CONNECTED = 23;
  public static final int CB_REMOTE_ADAPTER_DISCONNECTED = 24;
  public static final int CB_GENERIC_MOTION_EVENT = 25;
  public static final int CB_REQUEST_FOCUS_IN_DESCENDANTS = 26;
  public static final int CB_ANIMATION_START = 27;
  public static final int CB_ANIMATION_END = 28;
  public static final int CB_CREATE_CONTEXT_MENU = 29;
  public static final int CB_DRAW = 30;
  public static final int CB_KEY_SHORTCUT = 31;
  public static final int CB_SCROLL_CHANGED = 32;
  public static final int CB_SET_ALPHA = 33;
  public static final int CB_TRACKBALL_EVENT = 34;
  public static final int CB_WINDOW_VISIBILITY_CHANGED = 35;
  public static final int CB_CHECK_IS_TEXT_EDITOR = 36;
  public static final int CB_FINISH_TEMPORARY_DETACH = 37;
  public static final int CB_KEY_PRE_IME = 38;
  public static final int CB_START_TEMPORARY_DETACH = 39;
  public static final int CB_KEY_LONG_PRESS = 40;
  public static final int CB_CONFIGURATION_CHANGED = 41;
  public static final int CB_VISIBILITY_CHANGED = 42;
  public static final int CB_FILTER_TOUCH_EVENT_FOR_SECURITY = 43;
  public static final int CB_DRAG_EVENT = 44;

    private Object[] callbackProcs = new Object[45];

  public  RubotoListView(android.content.Context context) {
    super(context);
  }

  public  RubotoListView(android.content.Context context, android.util.AttributeSet attrs) {
    super(context, attrs);
  }

  public  RubotoListView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onMeasure(int arg0, int arg1) {
    if (callbackProcs[CB_MEASURE] != null) {
      Script.callMethod(callbackProcs[CB_MEASURE], "call" , new Object[]{arg0, arg1});
    }
  }

  public void onSizeChanged(int arg0, int arg1, int arg2, int arg3) {
    if (callbackProcs[CB_SIZE_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_SIZE_CHANGED], "call" , new Object[]{arg0, arg1, arg2, arg3});
    }
  }

  public void onFocusChanged(boolean arg0, int arg1, android.graphics.Rect arg2) {
    if (callbackProcs[CB_FOCUS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_FOCUS_CHANGED], "call" , new Object[]{arg0, arg1, arg2});
    }
  }

  public boolean onKeyDown(int arg0, android.view.KeyEvent arg1) {
    if (callbackProcs[CB_KEY_DOWN] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_DOWN], "call" , new Object[]{arg0, arg1}, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onKeyUp(int arg0, android.view.KeyEvent arg1) {
    if (callbackProcs[CB_KEY_UP] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_UP], "call" , new Object[]{arg0, arg1}, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onKeyMultiple(int arg0, int arg1, android.view.KeyEvent arg2) {
    if (callbackProcs[CB_KEY_MULTIPLE] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_MULTIPLE], "call" , new Object[]{arg0, arg1, arg2}, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent arg0) {
    if (callbackProcs[CB_TOUCH_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_TOUCH_EVENT], "call" , arg0, Boolean.class);
    } else {
      return false;
    }
  }

  public void onFinishInflate() {
    if (callbackProcs[CB_FINISH_INFLATE] != null) {
      Script.callMethod(callbackProcs[CB_FINISH_INFLATE], "call" );
    }
  }

  public void onFilterComplete(int count) {
    if (callbackProcs[CB_FILTER_COMPLETE] != null) {
      super.onFilterComplete(count);
      Script.callMethod(callbackProcs[CB_FILTER_COMPLETE], "call" , count);
    } else {
      super.onFilterComplete(count);
    }
  }

  public void onGlobalLayout() {
    if (callbackProcs[CB_GLOBAL_LAYOUT] != null) {
      super.onGlobalLayout();
      Script.callMethod(callbackProcs[CB_GLOBAL_LAYOUT], "call" );
    } else {
      super.onGlobalLayout();
    }
  }

  public void onRestoreInstanceState(android.os.Parcelable state) {
    if (callbackProcs[CB_RESTORE_INSTANCE_STATE] != null) {
      super.onRestoreInstanceState(state);
      Script.callMethod(callbackProcs[CB_RESTORE_INSTANCE_STATE], "call" , state);
    } else {
      super.onRestoreInstanceState(state);
    }
  }

  public android.os.Parcelable onSaveInstanceState() {
    if (callbackProcs[CB_SAVE_INSTANCE_STATE] != null) {
      super.onSaveInstanceState();
      return (android.os.Parcelable) Script.callMethod(callbackProcs[CB_SAVE_INSTANCE_STATE], "call" , android.os.Parcelable.class);
    } else {
      return super.onSaveInstanceState();
    }
  }

  public void onTextChanged(java.lang.CharSequence s, int start, int before, int count) {
    if (callbackProcs[CB_TEXT_CHANGED] != null) {
      super.onTextChanged(s, start, before, count);
      Script.callMethod(callbackProcs[CB_TEXT_CHANGED], "call" , new Object[]{s, start, before, count});
    } else {
      super.onTextChanged(s, start, before, count);
    }
  }

  public void onTouchModeChanged(boolean isInTouchMode) {
    if (callbackProcs[CB_TOUCH_MODE_CHANGED] != null) {
      super.onTouchModeChanged(isInTouchMode);
      Script.callMethod(callbackProcs[CB_TOUCH_MODE_CHANGED], "call" , isInTouchMode);
    } else {
      super.onTouchModeChanged(isInTouchMode);
    }
  }

  public void onDetachedFromWindow() {
    if (callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
      Script.callMethod(callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
    }
  }

  public void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    if (callbackProcs[CB_LAYOUT] != null) {
      Script.callMethod(callbackProcs[CB_LAYOUT], "call" , new Object[]{arg0, arg1, arg2, arg3, arg4});
    }
  }

  public boolean onInterceptTouchEvent(android.view.MotionEvent arg0) {
    if (callbackProcs[CB_INTERCEPT_TOUCH_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_INTERCEPT_TOUCH_EVENT], "call" , arg0, Boolean.class);
    } else {
      return false;
    }
  }

  public int[] onCreateDrawableState(int arg0) {
    if (callbackProcs[CB_CREATE_DRAWABLE_STATE] != null) {
      return (int[]) Script.callMethod(callbackProcs[CB_CREATE_DRAWABLE_STATE], "call" , arg0, int[].class);
    } else {
      return null;
    }
  }

  public void onWindowFocusChanged(boolean arg0) {
    if (callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , arg0);
    }
  }

  public void onDisplayHint(int arg0) {
    if (callbackProcs[CB_DISPLAY_HINT] != null) {
      Script.callMethod(callbackProcs[CB_DISPLAY_HINT], "call" , arg0);
    }
  }

  public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo arg0) {
    if (callbackProcs[CB_CREATE_INPUT_CONNECTION] != null) {
      return (android.view.inputmethod.InputConnection) Script.callMethod(callbackProcs[CB_CREATE_INPUT_CONNECTION], "call" , arg0, android.view.inputmethod.InputConnection.class);
    } else {
      return null;
    }
  }

  public void onAttachedToWindow() {
    if (callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
      Script.callMethod(callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
    }
  }

  public void onOverScrolled(int arg0, int arg1, boolean arg2, boolean arg3) {
    if (callbackProcs[CB_OVER_SCROLLED] != null) {
      Script.callMethod(callbackProcs[CB_OVER_SCROLLED], "call" , new Object[]{arg0, arg1, arg2, arg3});
    }
  }

  public boolean onRemoteAdapterConnected() {
    if (callbackProcs[CB_REMOTE_ADAPTER_CONNECTED] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_REMOTE_ADAPTER_CONNECTED], "call" , Boolean.class);
    } else {
      return false;
    }
  }

  public void onRemoteAdapterDisconnected() {
    if (callbackProcs[CB_REMOTE_ADAPTER_DISCONNECTED] != null) {
      Script.callMethod(callbackProcs[CB_REMOTE_ADAPTER_DISCONNECTED], "call" );
    }
  }

  public boolean onGenericMotionEvent(android.view.MotionEvent arg0) {
    if (callbackProcs[CB_GENERIC_MOTION_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_GENERIC_MOTION_EVENT], "call" , arg0, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onRequestFocusInDescendants(int direction, android.graphics.Rect previouslyFocusedRect) {
    if (callbackProcs[CB_REQUEST_FOCUS_IN_DESCENDANTS] != null) {
      super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
      return (Boolean) Script.callMethod(callbackProcs[CB_REQUEST_FOCUS_IN_DESCENDANTS], "call" , new Object[]{direction, previouslyFocusedRect}, Boolean.class);
    } else {
      return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }
  }

  public void onAnimationStart() {
    if (callbackProcs[CB_ANIMATION_START] != null) {
      Script.callMethod(callbackProcs[CB_ANIMATION_START], "call" );
    }
  }

  public void onAnimationEnd() {
    if (callbackProcs[CB_ANIMATION_END] != null) {
      Script.callMethod(callbackProcs[CB_ANIMATION_END], "call" );
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu) {
    if (callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
      super.onCreateContextMenu(menu);
      Script.callMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , menu);
    } else {
      super.onCreateContextMenu(menu);
    }
  }

  public void onDraw(android.graphics.Canvas canvas) {
    if (callbackProcs[CB_DRAW] != null) {
      super.onDraw(canvas);
      Script.callMethod(callbackProcs[CB_DRAW], "call" , canvas);
    } else {
      super.onDraw(canvas);
    }
  }

  public boolean onKeyShortcut(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_SHORTCUT] != null) {
      super.onKeyShortcut(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_SHORTCUT], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyShortcut(keyCode, event);
    }
  }

  public void onScrollChanged(int l, int t, int oldl, int oldt) {
    if (callbackProcs[CB_SCROLL_CHANGED] != null) {
      super.onScrollChanged(l, t, oldl, oldt);
      Script.callMethod(callbackProcs[CB_SCROLL_CHANGED], "call" , new Object[]{l, t, oldl, oldt});
    } else {
      super.onScrollChanged(l, t, oldl, oldt);
    }
  }

  public boolean onSetAlpha(int alpha) {
    if (callbackProcs[CB_SET_ALPHA] != null) {
      super.onSetAlpha(alpha);
      return (Boolean) Script.callMethod(callbackProcs[CB_SET_ALPHA], "call" , alpha, Boolean.class);
    } else {
      return super.onSetAlpha(alpha);
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

  public void onWindowVisibilityChanged(int visibility) {
    if (callbackProcs[CB_WINDOW_VISIBILITY_CHANGED] != null) {
      super.onWindowVisibilityChanged(visibility);
      Script.callMethod(callbackProcs[CB_WINDOW_VISIBILITY_CHANGED], "call" , visibility);
    } else {
      super.onWindowVisibilityChanged(visibility);
    }
  }

  public boolean onCheckIsTextEditor() {
    if (callbackProcs[CB_CHECK_IS_TEXT_EDITOR] != null) {
      super.onCheckIsTextEditor();
      return (Boolean) Script.callMethod(callbackProcs[CB_CHECK_IS_TEXT_EDITOR], "call" , Boolean.class);
    } else {
      return super.onCheckIsTextEditor();
    }
  }

  public void onFinishTemporaryDetach() {
    if (callbackProcs[CB_FINISH_TEMPORARY_DETACH] != null) {
      super.onFinishTemporaryDetach();
      Script.callMethod(callbackProcs[CB_FINISH_TEMPORARY_DETACH], "call" );
    } else {
      super.onFinishTemporaryDetach();
    }
  }

  public boolean onKeyPreIme(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_PRE_IME] != null) {
      super.onKeyPreIme(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_PRE_IME], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyPreIme(keyCode, event);
    }
  }

  public void onStartTemporaryDetach() {
    if (callbackProcs[CB_START_TEMPORARY_DETACH] != null) {
      super.onStartTemporaryDetach();
      Script.callMethod(callbackProcs[CB_START_TEMPORARY_DETACH], "call" );
    } else {
      super.onStartTemporaryDetach();
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_LONG_PRESS] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return false;
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
    }
  }

  public void onVisibilityChanged(android.view.View changedView, int visibility) {
    if (callbackProcs[CB_VISIBILITY_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_VISIBILITY_CHANGED], "call" , new Object[]{changedView, visibility});
    }
  }

  public boolean onFilterTouchEventForSecurity(android.view.MotionEvent event) {
    if (callbackProcs[CB_FILTER_TOUCH_EVENT_FOR_SECURITY] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_FILTER_TOUCH_EVENT_FOR_SECURITY], "call" , event, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onDragEvent(android.view.DragEvent arg0) {
    if (callbackProcs[CB_DRAG_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_DRAG_EVENT], "call" , arg0, Boolean.class);
    } else {
      return false;
    }
  }

}
