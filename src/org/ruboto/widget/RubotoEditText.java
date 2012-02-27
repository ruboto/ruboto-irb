package org.ruboto.widget;

import org.ruboto.Script;

public class RubotoEditText extends android.widget.EditText {

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
  public static final int CB_ANIMATION_END = 11;
  public static final int CB_ANIMATION_START = 12;
  public static final int CB_ATTACHED_TO_WINDOW = 13;
  public static final int CB_CREATE_CONTEXT_MENU = 14;
  public static final int CB_CREATE_DRAWABLE_STATE = 15;
  public static final int CB_DETACHED_FROM_WINDOW = 16;
  public static final int CB_DRAW = 17;
  public static final int CB_FINISH_INFLATE = 18;
  public static final int CB_FOCUS_CHANGED = 19;
  public static final int CB_KEY_DOWN = 20;
  public static final int CB_KEY_MULTIPLE = 21;
  public static final int CB_KEY_SHORTCUT = 22;
  public static final int CB_KEY_UP = 23;
  public static final int CB_LAYOUT = 24;
  public static final int CB_MEASURE = 25;
  public static final int CB_SCROLL_CHANGED = 26;
  public static final int CB_SET_ALPHA = 27;
  public static final int CB_SIZE_CHANGED = 28;
  public static final int CB_TOUCH_EVENT = 29;
  public static final int CB_TRACKBALL_EVENT = 30;
  public static final int CB_WINDOW_FOCUS_CHANGED = 31;
  public static final int CB_WINDOW_VISIBILITY_CHANGED = 32;
  public static final int CB_CHECK_IS_TEXT_EDITOR = 33;
  public static final int CB_CREATE_INPUT_CONNECTION = 34;
  public static final int CB_FINISH_TEMPORARY_DETACH = 35;
  public static final int CB_KEY_PRE_IME = 36;
  public static final int CB_START_TEMPORARY_DETACH = 37;
  public static final int CB_KEY_LONG_PRESS = 38;

    private Object[] callbackProcs = new Object[39];

  public  RubotoEditText(android.content.Context context) {
    super(context);
  }

  public  RubotoEditText(android.content.Context context, android.util.AttributeSet attrs) {
    super(context, attrs);
  }

  public  RubotoEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onPreDraw() {
    if (callbackProcs != null && callbackProcs[CB_PRE_DRAW] != null) {
      super.onPreDraw();
      return (Boolean) Script.callMethod(callbackProcs[CB_PRE_DRAW], "call" , Boolean.class);
    } else {
      return super.onPreDraw();
    }
  }

  public void onRestoreInstanceState(android.os.Parcelable state) {
    if (callbackProcs != null && callbackProcs[CB_RESTORE_INSTANCE_STATE] != null) {
      super.onRestoreInstanceState(state);
      Script.callMethod(callbackProcs[CB_RESTORE_INSTANCE_STATE], "call" , state);
    } else {
      super.onRestoreInstanceState(state);
    }
  }

  public android.os.Parcelable onSaveInstanceState() {
    if (callbackProcs != null && callbackProcs[CB_SAVE_INSTANCE_STATE] != null) {
      super.onSaveInstanceState();
      return (android.os.Parcelable) Script.callMethod(callbackProcs[CB_SAVE_INSTANCE_STATE], "call" , android.os.Parcelable.class);
    } else {
      return super.onSaveInstanceState();
    }
  }

  public void onTextChanged(java.lang.CharSequence text, int start, int before, int after) {
    if (callbackProcs != null && callbackProcs[CB_TEXT_CHANGED] != null) {
      super.onTextChanged(text, start, before, after);
      Script.callMethod(callbackProcs[CB_TEXT_CHANGED], "call" , new Object[]{text, start, before, after});
    } else {
      super.onTextChanged(text, start, before, after);
    }
  }

  public void onBeginBatchEdit() {
    if (callbackProcs != null && callbackProcs[CB_BEGIN_BATCH_EDIT] != null) {
      super.onBeginBatchEdit();
      Script.callMethod(callbackProcs[CB_BEGIN_BATCH_EDIT], "call" );
    } else {
      super.onBeginBatchEdit();
    }
  }

  public void onCommitCompletion(android.view.inputmethod.CompletionInfo text) {
    if (callbackProcs != null && callbackProcs[CB_COMMIT_COMPLETION] != null) {
      super.onCommitCompletion(text);
      Script.callMethod(callbackProcs[CB_COMMIT_COMPLETION], "call" , text);
    } else {
      super.onCommitCompletion(text);
    }
  }

  public void onEditorAction(int actionCode) {
    if (callbackProcs != null && callbackProcs[CB_EDITOR_ACTION] != null) {
      super.onEditorAction(actionCode);
      Script.callMethod(callbackProcs[CB_EDITOR_ACTION], "call" , actionCode);
    } else {
      super.onEditorAction(actionCode);
    }
  }

  public void onEndBatchEdit() {
    if (callbackProcs != null && callbackProcs[CB_END_BATCH_EDIT] != null) {
      super.onEndBatchEdit();
      Script.callMethod(callbackProcs[CB_END_BATCH_EDIT], "call" );
    } else {
      super.onEndBatchEdit();
    }
  }

  public boolean onPrivateIMECommand(java.lang.String action, android.os.Bundle data) {
    if (callbackProcs != null && callbackProcs[CB_PRIVATE_I_M_E_COMMAND] != null) {
      super.onPrivateIMECommand(action, data);
      return (Boolean) Script.callMethod(callbackProcs[CB_PRIVATE_I_M_E_COMMAND], "call" , new Object[]{action, data}, Boolean.class);
    } else {
      return super.onPrivateIMECommand(action, data);
    }
  }

  public void onSelectionChanged(int selStart, int selEnd) {
    if (callbackProcs != null && callbackProcs[CB_SELECTION_CHANGED] != null) {
      super.onSelectionChanged(selStart, selEnd);
      Script.callMethod(callbackProcs[CB_SELECTION_CHANGED], "call" , new Object[]{selStart, selEnd});
    } else {
      super.onSelectionChanged(selStart, selEnd);
    }
  }

  public boolean onTextContextMenuItem(int id) {
    if (callbackProcs != null && callbackProcs[CB_TEXT_CONTEXT_MENU_ITEM] != null) {
      super.onTextContextMenuItem(id);
      return (Boolean) Script.callMethod(callbackProcs[CB_TEXT_CONTEXT_MENU_ITEM], "call" , id, Boolean.class);
    } else {
      return super.onTextContextMenuItem(id);
    }
  }

  public void onAnimationEnd() {
    if (callbackProcs != null && callbackProcs[CB_ANIMATION_END] != null) {
      super.onAnimationEnd();
      Script.callMethod(callbackProcs[CB_ANIMATION_END], "call" );
    } else {
      super.onAnimationEnd();
    }
  }

  public void onAnimationStart() {
    if (callbackProcs != null && callbackProcs[CB_ANIMATION_START] != null) {
      super.onAnimationStart();
      Script.callMethod(callbackProcs[CB_ANIMATION_START], "call" );
    } else {
      super.onAnimationStart();
    }
  }

  public void onAttachedToWindow() {
    if (callbackProcs != null && callbackProcs[CB_ATTACHED_TO_WINDOW] != null) {
      super.onAttachedToWindow();
      Script.callMethod(callbackProcs[CB_ATTACHED_TO_WINDOW], "call" );
    } else {
      super.onAttachedToWindow();
    }
  }

  public void onCreateContextMenu(android.view.ContextMenu menu) {
    if (callbackProcs != null && callbackProcs[CB_CREATE_CONTEXT_MENU] != null) {
      super.onCreateContextMenu(menu);
      Script.callMethod(callbackProcs[CB_CREATE_CONTEXT_MENU], "call" , menu);
    } else {
      super.onCreateContextMenu(menu);
    }
  }

  public int[] onCreateDrawableState(int extraSpace) {
    if (callbackProcs != null && callbackProcs[CB_CREATE_DRAWABLE_STATE] != null) {
      super.onCreateDrawableState(extraSpace);
      return (int[]) Script.callMethod(callbackProcs[CB_CREATE_DRAWABLE_STATE], "call" , extraSpace, int[].class);
    } else {
      return super.onCreateDrawableState(extraSpace);
    }
  }

  public void onDetachedFromWindow() {
    if (callbackProcs != null && callbackProcs[CB_DETACHED_FROM_WINDOW] != null) {
      super.onDetachedFromWindow();
      Script.callMethod(callbackProcs[CB_DETACHED_FROM_WINDOW], "call" );
    } else {
      super.onDetachedFromWindow();
    }
  }

  public void onDraw(android.graphics.Canvas canvas) {
    if (callbackProcs != null && callbackProcs[CB_DRAW] != null) {
      super.onDraw(canvas);
      Script.callMethod(callbackProcs[CB_DRAW], "call" , canvas);
    } else {
      super.onDraw(canvas);
    }
  }

  public void onFinishInflate() {
    if (callbackProcs != null && callbackProcs[CB_FINISH_INFLATE] != null) {
      super.onFinishInflate();
      Script.callMethod(callbackProcs[CB_FINISH_INFLATE], "call" );
    } else {
      super.onFinishInflate();
    }
  }

  public void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect previouslyFocusedRect) {
    if (callbackProcs != null && callbackProcs[CB_FOCUS_CHANGED] != null) {
      super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
      Script.callMethod(callbackProcs[CB_FOCUS_CHANGED], "call" , new Object[]{gainFocus, direction, previouslyFocusedRect});
    } else {
      super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }
  }

  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs != null && callbackProcs[CB_KEY_DOWN] != null) {
      super.onKeyDown(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_DOWN], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
    if (callbackProcs != null && callbackProcs[CB_KEY_MULTIPLE] != null) {
      super.onKeyMultiple(keyCode, repeatCount, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_MULTIPLE], "call" , new Object[]{keyCode, repeatCount, event}, Boolean.class);
    } else {
      return super.onKeyMultiple(keyCode, repeatCount, event);
    }
  }

  public boolean onKeyShortcut(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs != null && callbackProcs[CB_KEY_SHORTCUT] != null) {
      super.onKeyShortcut(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_SHORTCUT], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyShortcut(keyCode, event);
    }
  }

  public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs != null && callbackProcs[CB_KEY_UP] != null) {
      super.onKeyUp(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_UP], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyUp(keyCode, event);
    }
  }

  public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (callbackProcs != null && callbackProcs[CB_LAYOUT] != null) {
      super.onLayout(changed, left, top, right, bottom);
      Script.callMethod(callbackProcs[CB_LAYOUT], "call" , new Object[]{changed, left, top, right, bottom});
    } else {
      super.onLayout(changed, left, top, right, bottom);
    }
  }

  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (callbackProcs != null && callbackProcs[CB_MEASURE] != null) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      Script.callMethod(callbackProcs[CB_MEASURE], "call" , new Object[]{widthMeasureSpec, heightMeasureSpec});
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  public void onScrollChanged(int l, int t, int oldl, int oldt) {
    if (callbackProcs != null && callbackProcs[CB_SCROLL_CHANGED] != null) {
      super.onScrollChanged(l, t, oldl, oldt);
      Script.callMethod(callbackProcs[CB_SCROLL_CHANGED], "call" , new Object[]{l, t, oldl, oldt});
    } else {
      super.onScrollChanged(l, t, oldl, oldt);
    }
  }

  public boolean onSetAlpha(int alpha) {
    if (callbackProcs != null && callbackProcs[CB_SET_ALPHA] != null) {
      super.onSetAlpha(alpha);
      return (Boolean) Script.callMethod(callbackProcs[CB_SET_ALPHA], "call" , alpha, Boolean.class);
    } else {
      return super.onSetAlpha(alpha);
    }
  }

  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    if (callbackProcs != null && callbackProcs[CB_SIZE_CHANGED] != null) {
      super.onSizeChanged(w, h, oldw, oldh);
      Script.callMethod(callbackProcs[CB_SIZE_CHANGED], "call" , new Object[]{w, h, oldw, oldh});
    } else {
      super.onSizeChanged(w, h, oldw, oldh);
    }
  }

  public boolean onTouchEvent(android.view.MotionEvent event) {
    if (callbackProcs != null && callbackProcs[CB_TOUCH_EVENT] != null) {
      super.onTouchEvent(event);
      return (Boolean) Script.callMethod(callbackProcs[CB_TOUCH_EVENT], "call" , event, Boolean.class);
    } else {
      return super.onTouchEvent(event);
    }
  }

  public boolean onTrackballEvent(android.view.MotionEvent event) {
    if (callbackProcs != null && callbackProcs[CB_TRACKBALL_EVENT] != null) {
      super.onTrackballEvent(event);
      return (Boolean) Script.callMethod(callbackProcs[CB_TRACKBALL_EVENT], "call" , event, Boolean.class);
    } else {
      return super.onTrackballEvent(event);
    }
  }

  public void onWindowFocusChanged(boolean hasWindowFocus) {
    if (callbackProcs != null && callbackProcs[CB_WINDOW_FOCUS_CHANGED] != null) {
      super.onWindowFocusChanged(hasWindowFocus);
      Script.callMethod(callbackProcs[CB_WINDOW_FOCUS_CHANGED], "call" , hasWindowFocus);
    } else {
      super.onWindowFocusChanged(hasWindowFocus);
    }
  }

  public void onWindowVisibilityChanged(int visibility) {
    if (callbackProcs != null && callbackProcs[CB_WINDOW_VISIBILITY_CHANGED] != null) {
      super.onWindowVisibilityChanged(visibility);
      Script.callMethod(callbackProcs[CB_WINDOW_VISIBILITY_CHANGED], "call" , visibility);
    } else {
      super.onWindowVisibilityChanged(visibility);
    }
  }

  public boolean onCheckIsTextEditor() {
    if (callbackProcs != null && callbackProcs[CB_CHECK_IS_TEXT_EDITOR] != null) {
      super.onCheckIsTextEditor();
      return (Boolean) Script.callMethod(callbackProcs[CB_CHECK_IS_TEXT_EDITOR], "call" , Boolean.class);
    } else {
      return super.onCheckIsTextEditor();
    }
  }

  public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo outAttrs) {
    if (callbackProcs != null && callbackProcs[CB_CREATE_INPUT_CONNECTION] != null) {
      super.onCreateInputConnection(outAttrs);
      return (android.view.inputmethod.InputConnection) Script.callMethod(callbackProcs[CB_CREATE_INPUT_CONNECTION], "call" , outAttrs, android.view.inputmethod.InputConnection.class);
    } else {
      return super.onCreateInputConnection(outAttrs);
    }
  }

  public void onFinishTemporaryDetach() {
    if (callbackProcs != null && callbackProcs[CB_FINISH_TEMPORARY_DETACH] != null) {
      super.onFinishTemporaryDetach();
      Script.callMethod(callbackProcs[CB_FINISH_TEMPORARY_DETACH], "call" );
    } else {
      super.onFinishTemporaryDetach();
    }
  }

  public boolean onKeyPreIme(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs != null && callbackProcs[CB_KEY_PRE_IME] != null) {
      super.onKeyPreIme(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_PRE_IME], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyPreIme(keyCode, event);
    }
  }

  public void onStartTemporaryDetach() {
    if (callbackProcs != null && callbackProcs[CB_START_TEMPORARY_DETACH] != null) {
      super.onStartTemporaryDetach();
      Script.callMethod(callbackProcs[CB_START_TEMPORARY_DETACH], "call" );
    } else {
      super.onStartTemporaryDetach();
    }
  }

  public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
    if (callbackProcs != null && callbackProcs[CB_KEY_LONG_PRESS] != null) {
      super.onKeyLongPress(keyCode, event);
      return (Boolean) Script.callMethod(callbackProcs[CB_KEY_LONG_PRESS], "call" , new Object[]{keyCode, event}, Boolean.class);
    } else {
      return super.onKeyLongPress(keyCode, event);
    }
  }

}
