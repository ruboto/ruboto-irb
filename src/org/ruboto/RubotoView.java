package org.ruboto;

import org.ruboto.Script;

public class RubotoView extends android.view.View {

  public static final int CB_ANIMATION_END = 0;
  public static final int CB_ANIMATION_START = 1;
  public static final int CB_ATTACHED_TO_WINDOW = 2;
  public static final int CB_CREATE_CONTEXT_MENU = 3;
  public static final int CB_CREATE_DRAWABLE_STATE = 4;
  public static final int CB_DETACHED_FROM_WINDOW = 5;
  public static final int CB_DRAW = 6;
  public static final int CB_FINISH_INFLATE = 7;
  public static final int CB_FOCUS_CHANGED = 8;
  public static final int CB_KEY_DOWN = 9;
  public static final int CB_KEY_MULTIPLE = 10;
  public static final int CB_KEY_SHORTCUT = 11;
  public static final int CB_KEY_UP = 12;
  public static final int CB_LAYOUT = 13;
  public static final int CB_MEASURE = 14;
  public static final int CB_RESTORE_INSTANCE_STATE = 15;
  public static final int CB_SAVE_INSTANCE_STATE = 16;
  public static final int CB_SCROLL_CHANGED = 17;
  public static final int CB_SET_ALPHA = 18;
  public static final int CB_SIZE_CHANGED = 19;
  public static final int CB_TOUCH_EVENT = 20;
  public static final int CB_TRACKBALL_EVENT = 21;
  public static final int CB_WINDOW_FOCUS_CHANGED = 22;
  public static final int CB_WINDOW_VISIBILITY_CHANGED = 23;
  public static final int CB_CHECK_IS_TEXT_EDITOR = 24;
  public static final int CB_CREATE_INPUT_CONNECTION = 25;
  public static final int CB_FINISH_TEMPORARY_DETACH = 26;
  public static final int CB_KEY_PRE_IME = 27;
  public static final int CB_START_TEMPORARY_DETACH = 28;
  public static final int CB_KEY_LONG_PRESS = 29;

    private Object[] callbackProcs = new Object[30];

  public  RubotoView(android.content.Context context) {
    super(context);
  }

  public  RubotoView(android.content.Context context, android.util.AttributeSet attrs) {
    super(context, attrs);
  }

  public  RubotoView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onAnimationEnd() {
    if (callbackProcs[CB_ANIMATION_END] != null) {
      super.onAnimationEnd();
      Script.callMethod(callbackProcs[CB_ANIMATION_END], "call" );
    } else {
      super.onAnimationEnd();
    }
  }

  public void onAnimationStart() {
    if (callbackProcs[CB_ANIMATION_START] != null) {
      super.onAnimationStart();
      Script.callMethod(callbackProcs[CB_ANIMATION_START], "call" );
    } else {
      super.onAnimationStart();
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

  public void onCreateContextMenu(android.view.ContextMenu menu) {
    if (callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
      super.onCreateContextMenu(menu);
      Script.callMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , menu);
    } else {
      super.onCreateContextMenu(menu);
    }
  }

  public int[] onCreateDrawableState(int extraSpace) {
    if (callbackProcs[CB_CREATE_DRAWABLE_STATE] != null) {
      super.onCreateDrawableState(extraSpace);
      return (int[]) Script.callMethod(callbackProcs[CB_CREATE_DRAWABLE_STATE], "call" , extraSpace, int[].class);
    } else {
      return super.onCreateDrawableState(extraSpace);
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

  public void onDraw(android.graphics.Canvas canvas) {
    if (callbackProcs[CB_DRAW] != null) {
      super.onDraw(canvas);
      Script.callMethod(callbackProcs[CB_DRAW], "call" , canvas);
    } else {
      super.onDraw(canvas);
    }
  }

  public void onFinishInflate() {
    if (callbackProcs[CB_FINISH_INFLATE] != null) {
      super.onFinishInflate();
      Script.callMethod(callbackProcs[CB_FINISH_INFLATE], "call" );
    } else {
      super.onFinishInflate();
    }
  }

  public void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
    if (callbackProcs[CB_FOCUS_CHANGED] != null) {
      super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
      Script.callMethod(callbackProcs[CB_FOCUS_CHANGED], "call" , new Object[]{gainFocus, direction, previouslyFocusedRect});
    } else {
      super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
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

  public boolean onKeyShortcut(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs[CB_KEY_SHORTCUT] != null) {
      super.onKeyShortcut(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_SHORTCUT], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyShortcut(keyCode, event);
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

  public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (callbackProcs[CB_LAYOUT] != null) {
      super.onLayout(changed, left, top, right, bottom);
      Script.callMethod(callbackProcs[CB_LAYOUT], "call" , new Object[]{changed, left, top, right, bottom});
    } else {
      super.onLayout(changed, left, top, right, bottom);
    }
  }

  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (callbackProcs[CB_MEASURE] != null) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      Script.callMethod(callbackProcs[CB_MEASURE], "call" , new Object[]{widthMeasureSpec, heightMeasureSpec});
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    if (callbackProcs[CB_SIZE_CHANGED] != null) {
      super.onSizeChanged(w, h, oldw, oldh);
      Script.callMethod(callbackProcs[CB_SIZE_CHANGED], "call" , new Object[]{w, h, oldw, oldh});
    } else {
      super.onSizeChanged(w, h, oldw, oldh);
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

  public void onWindowFocusChanged(boolean hasWindowFocus) {
    if (callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
      super.onWindowFocusChanged(hasWindowFocus);
      Script.callMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , hasWindowFocus);
    } else {
      super.onWindowFocusChanged(hasWindowFocus);
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

  public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo outAttrs) {
    if (callbackProcs[CB_CREATE_INPUT_CONNECTION] != null) {
      super.onCreateInputConnection(outAttrs);
      return (android.view.inputmethod.InputConnection) Script.callMethod(callbackProcs[CB_CREATE_INPUT_CONNECTION], "call" , outAttrs, android.view.inputmethod.InputConnection.class);
    } else {
      return super.onCreateInputConnection(outAttrs);
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
      super.onKeyLongPress(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyLongPress(keyCode, event);
    }
  }

}
