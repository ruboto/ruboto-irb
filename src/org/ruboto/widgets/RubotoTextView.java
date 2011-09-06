package org.ruboto.widgets;

import org.ruboto.Script;

public class RubotoTextView extends android.widget.TextView {

  public static final int CB_PRE_DRAW = 0;
  public static final int CB_RESTORE_INSTANCE_STATE = 1;
  public static final int CB_SAVE_INSTANCE_STATE = 2;
  public static final int CB_TEXT_CHANGED = 3;
  public static final int CB_BEGIN_BATCH_EDIT = 4;
  public static final int CB_COMMIT_COMPLETION = 5;
  public static final int CB_EDITOR_ACTION = 6;
  public static final int CB_END_BATCH_EDIT = 7;
  public static final int CB_PRIVATE_I_M_E_COMMAND = 8;
  public static final int CB_SELECTION_CHANGED = 9;
  public static final int CB_TEXT_CONTEXT_MENU_ITEM = 10;
  public static final int CB_DETACHED_FROM_WINDOW = 11;
  public static final int CB_MEASURE = 12;
  public static final int CB_CREATE_DRAWABLE_STATE = 13;
  public static final int CB_FOCUS_CHANGED = 14;
  public static final int CB_START_TEMPORARY_DETACH = 15;
  public static final int CB_FINISH_TEMPORARY_DETACH = 16;
  public static final int CB_WINDOW_FOCUS_CHANGED = 17;
  public static final int CB_VISIBILITY_CHANGED = 18;
  public static final int CB_KEY_DOWN = 19;
  public static final int CB_KEY_UP = 20;
  public static final int CB_KEY_MULTIPLE = 21;
  public static final int CB_KEY_SHORTCUT = 22;
  public static final int CB_CHECK_IS_TEXT_EDITOR = 23;
  public static final int CB_CREATE_INPUT_CONNECTION = 24;
  public static final int CB_CREATE_CONTEXT_MENU = 25;
  public static final int CB_TRACKBALL_EVENT = 26;
  public static final int CB_TOUCH_EVENT = 27;
  public static final int CB_DRAW = 28;
  public static final int CB_ATTACHED_TO_WINDOW = 29;
  public static final int CB_SET_ALPHA = 30;
  public static final int CB_DRAG_EVENT = 31;
  public static final int CB_COMMIT_CORRECTION = 32;
  public static final int CB_GENERIC_MOTION_EVENT = 33;
  public static final int CB_ANIMATION_END = 34;
  public static final int CB_ANIMATION_START = 35;
  public static final int CB_FINISH_INFLATE = 36;
  public static final int CB_LAYOUT = 37;
  public static final int CB_SCROLL_CHANGED = 38;
  public static final int CB_SIZE_CHANGED = 39;
  public static final int CB_WINDOW_VISIBILITY_CHANGED = 40;
  public static final int CB_KEY_PRE_IME = 41;
  public static final int CB_KEY_LONG_PRESS = 42;
  public static final int CB_CONFIGURATION_CHANGED = 43;
  public static final int CB_DISPLAY_HINT = 44;
  public static final int CB_FILTER_TOUCH_EVENT_FOR_SECURITY = 45;
  public static final int CB_OVER_SCROLLED = 46;

    private Object[] callbackProcs = new Object[47];

  public  RubotoTextView(android.content.Context context) {
    super(context);
  }

  public  RubotoTextView(android.content.Context context, android.util.AttributeSet attrs) {
    super(context, attrs);
  }

  public  RubotoTextView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onPreDraw() {
    if (callbackProcs[CB_PRE_DRAW] != null) {
      super.onPreDraw();
      return (Boolean) Script.callMethod(callbackProcs[CB_PRE_DRAW], "call" , Boolean.class);
    } else {
      return super.onPreDraw();
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

  public void onTextChanged(java.lang.CharSequence text, int start, int before, int after) {
    if (callbackProcs[CB_TEXT_CHANGED] != null) {
      super.onTextChanged(text, start, before, after);
      Script.callMethod(callbackProcs[CB_TEXT_CHANGED], "call" , new Object[]{text, start, before, after});
    } else {
      super.onTextChanged(text, start, before, after);
    }
  }

  public void onBeginBatchEdit() {
    if (callbackProcs[CB_BEGIN_BATCH_EDIT] != null) {
      super.onBeginBatchEdit();
      Script.callMethod(callbackProcs[CB_BEGIN_BATCH_EDIT], "call" );
    } else {
      super.onBeginBatchEdit();
    }
  }

  public void onCommitCompletion(android.view.inputmethod.CompletionInfo text) {
    if (callbackProcs[CB_COMMIT_COMPLETION] != null) {
      super.onCommitCompletion(text);
      Script.callMethod(callbackProcs[CB_COMMIT_COMPLETION], "call" , text);
    } else {
      super.onCommitCompletion(text);
    }
  }

  public void onEditorAction(int actionCode) {
    if (callbackProcs[CB_EDITOR_ACTION] != null) {
      super.onEditorAction(actionCode);
      Script.callMethod(callbackProcs[CB_EDITOR_ACTION], "call" , actionCode);
    } else {
      super.onEditorAction(actionCode);
    }
  }

  public void onEndBatchEdit() {
    if (callbackProcs[CB_END_BATCH_EDIT] != null) {
      super.onEndBatchEdit();
      Script.callMethod(callbackProcs[CB_END_BATCH_EDIT], "call" );
    } else {
      super.onEndBatchEdit();
    }
  }

  public boolean onPrivateIMECommand(java.lang.String action, android.os.Bundle data) {
    if (callbackProcs[CB_PRIVATE_I_M_E_COMMAND] != null) {
      super.onPrivateIMECommand(action, data);
      return (Boolean) Script.callMethod(callbackProcs[CB_PRIVATE_I_M_E_COMMAND], "call" , new Object[]{action, data}, Boolean.class);
    } else {
      return super.onPrivateIMECommand(action, data);
    }
  }

  public void onSelectionChanged(int selStart, int selEnd) {
    if (callbackProcs[CB_SELECTION_CHANGED] != null) {
      super.onSelectionChanged(selStart, selEnd);
      Script.callMethod(callbackProcs[CB_SELECTION_CHANGED], "call" , new Object[]{selStart, selEnd});
    } else {
      super.onSelectionChanged(selStart, selEnd);
    }
  }

  public boolean onTextContextMenuItem(int id) {
    if (callbackProcs[CB_TEXT_CONTEXT_MENU_ITEM] != null) {
      super.onTextContextMenuItem(id);
      return (Boolean) Script.callMethod(callbackProcs[CB_TEXT_CONTEXT_MENU_ITEM], "call" , id, Boolean.class);
    } else {
      return super.onTextContextMenuItem(id);
    }
  }

  public void onDetachedFromWindow() {
    if (callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
      Script.callMethod(callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
    }
  }

  public void onMeasure(int arg0, int arg1) {
    if (callbackProcs[CB_MEASURE] != null) {
      Script.callMethod(callbackProcs[CB_MEASURE], "call" , new Object[]{arg0, arg1});
    }
  }

  public int[] onCreateDrawableState(int arg0) {
    if (callbackProcs[CB_CREATE_DRAWABLE_STATE] != null) {
      return (int[]) Script.callMethod(callbackProcs[CB_CREATE_DRAWABLE_STATE], "call" , arg0, int[].class);
    } else {
      return null;
    }
  }

  public void onFocusChanged(boolean arg0, int arg1, android.graphics.Rect arg2) {
    if (callbackProcs[CB_FOCUS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_FOCUS_CHANGED], "call" , new Object[]{arg0, arg1, arg2});
    }
  }

  public void onStartTemporaryDetach() {
    if (callbackProcs[CB_START_TEMPORARY_DETACH] != null) {
      Script.callMethod(callbackProcs[CB_START_TEMPORARY_DETACH], "call" );
    }
  }

  public void onFinishTemporaryDetach() {
    if (callbackProcs[CB_FINISH_TEMPORARY_DETACH] != null) {
      Script.callMethod(callbackProcs[CB_FINISH_TEMPORARY_DETACH], "call" );
    }
  }

  public void onWindowFocusChanged(boolean arg0) {
    if (callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , arg0);
    }
  }

  public void onVisibilityChanged(android.view.View arg0, int arg1) {
    if (callbackProcs[CB_VISIBILITY_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_VISIBILITY_CHANGED], "call" , new Object[]{arg0, arg1});
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

  public boolean onKeyShortcut(int arg0, android.view.KeyEvent arg1) {
    if (callbackProcs[CB_KEY_SHORTCUT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_SHORTCUT], "call" , new Object[]{arg0, arg1}, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onCheckIsTextEditor() {
    if (callbackProcs[CB_CHECK_IS_TEXT_EDITOR] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_CHECK_IS_TEXT_EDITOR], "call" , Boolean.class);
    } else {
      return false;
    }
  }

  public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo arg0) {
    if (callbackProcs[CB_CREATE_INPUT_CONNECTION] != null) {
      return (android.view.inputmethod.InputConnection) Script.callMethod(callbackProcs[CB_CREATE_INPUT_CONNECTION], "call" , arg0, android.view.inputmethod.InputConnection.class);
    } else {
      return null;
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu arg0) {
    if (callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
      Script.callMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , arg0);
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent arg0) {
    if (callbackProcs[CB_TRACKBALL_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_TRACKBALL_EVENT], "call" , arg0, Boolean.class);
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

  public void onDraw(android.graphics.Canvas arg0) {
    if (callbackProcs[CB_DRAW] != null) {
      Script.callMethod(callbackProcs[CB_DRAW], "call" , arg0);
    }
  }

  public void onAttachedToWindow() {
    if (callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
      Script.callMethod(callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
    }
  }

  public boolean onSetAlpha(int arg0) {
    if (callbackProcs[CB_SET_ALPHA] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_SET_ALPHA], "call" , arg0, Boolean.class);
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

  public void onCommitCorrection(android.view.inputmethod.CorrectionInfo arg0) {
    if (callbackProcs[CB_COMMIT_CORRECTION] != null) {
      Script.callMethod(callbackProcs[CB_COMMIT_CORRECTION], "call" , arg0);
    }
  }

  public boolean onGenericMotionEvent(android.view.MotionEvent arg0) {
    if (callbackProcs[CB_GENERIC_MOTION_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_GENERIC_MOTION_EVENT], "call" , arg0, Boolean.class);
    } else {
      return false;
    }
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

  public void onFinishInflate() {
    if (callbackProcs[CB_FINISH_INFLATE] != null) {
      super.onFinishInflate();
      Script.callMethod(callbackProcs[CB_FINISH_INFLATE], "call" );
    } else {
      super.onFinishInflate();
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

  public void onScrollChanged(int l, int t, int oldl, int oldt) {
    if (callbackProcs[CB_SCROLL_CHANGED] != null) {
      super.onScrollChanged(l, t, oldl, oldt);
      Script.callMethod(callbackProcs[CB_SCROLL_CHANGED], "call" , new Object[]{l, t, oldl, oldt});
    } else {
      super.onScrollChanged(l, t, oldl, oldt);
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

  public void onWindowVisibilityChanged(int visibility) {
    if (callbackProcs[CB_WINDOW_VISIBILITY_CHANGED] != null) {
      super.onWindowVisibilityChanged(visibility);
      Script.callMethod(callbackProcs[CB_WINDOW_VISIBILITY_CHANGED], "call" , visibility);
    } else {
      super.onWindowVisibilityChanged(visibility);
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

  public void onDisplayHint(int hint) {
    if (callbackProcs[CB_DISPLAY_HINT] != null) {
      Script.callMethod(callbackProcs[CB_DISPLAY_HINT], "call" , hint);
    }
  }

  public boolean onFilterTouchEventForSecurity(android.view.MotionEvent event) {
    if (callbackProcs[CB_FILTER_TOUCH_EVENT_FOR_SECURITY] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_FILTER_TOUCH_EVENT_FOR_SECURITY], "call" , event, Boolean.class);
    } else {
      return false;
    }
  }

  public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    if (callbackProcs[CB_OVER_SCROLLED] != null) {
      Script.callMethod(callbackProcs[CB_OVER_SCROLLED], "call" , new Object[]{scrollX, scrollY, clampedX, clampedY});
    }
  }

}
