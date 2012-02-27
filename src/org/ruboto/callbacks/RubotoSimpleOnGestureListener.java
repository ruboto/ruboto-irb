package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoSimpleOnGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

  public static final int CB_DOWN = 0;
  public static final int CB_FLING = 1;
  public static final int CB_LONG_PRESS = 2;
  public static final int CB_SCROLL = 3;
  public static final int CB_SHOW_PRESS = 4;
  public static final int CB_SINGLE_TAP_UP = 5;
  public static final int CB_DOUBLE_TAP = 6;
  public static final int CB_DOUBLE_TAP_EVENT = 7;
  public static final int CB_SINGLE_TAP_CONFIRMED = 8;

    private Object[] callbackProcs = new Object[9];

  public  RubotoSimpleOnGestureListener() {
    super();
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onDown(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_DOWN] != null) {
      super.onDown(e);
      return (Boolean) Script.callMethod(callbackProcs[CB_DOWN], "call" , e, Boolean.class);
    } else {
      return super.onDown(e);
    }
  }

  public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
    if (callbackProcs != null && callbackProcs[CB_FLING] != null) {
      super.onFling(e1, e2, velocityX, velocityY);
      return (Boolean) Script.callMethod(callbackProcs[CB_FLING], "call" , new Object[]{e1, e2, velocityX, velocityY}, Boolean.class);
    } else {
      return super.onFling(e1, e2, velocityX, velocityY);
    }
  }

  public void onLongPress(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_LONG_PRESS] != null) {
      super.onLongPress(e);
      Script.callMethod(callbackProcs[CB_LONG_PRESS], "call" , e);
    } else {
      super.onLongPress(e);
    }
  }

  public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
    if (callbackProcs != null && callbackProcs[CB_SCROLL] != null) {
      super.onScroll(e1, e2, distanceX, distanceY);
      return (Boolean) Script.callMethod(callbackProcs[CB_SCROLL], "call" , new Object[]{e1, e2, distanceX, distanceY}, Boolean.class);
    } else {
      return super.onScroll(e1, e2, distanceX, distanceY);
    }
  }

  public void onShowPress(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_SHOW_PRESS] != null) {
      super.onShowPress(e);
      Script.callMethod(callbackProcs[CB_SHOW_PRESS], "call" , e);
    } else {
      super.onShowPress(e);
    }
  }

  public boolean onSingleTapUp(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_SINGLE_TAP_UP] != null) {
      super.onSingleTapUp(e);
      return (Boolean) Script.callMethod(callbackProcs[CB_SINGLE_TAP_UP], "call" , e, Boolean.class);
    } else {
      return super.onSingleTapUp(e);
    }
  }

  public boolean onDoubleTap(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_DOUBLE_TAP] != null) {
      super.onDoubleTap(e);
      return (Boolean) Script.callMethod(callbackProcs[CB_DOUBLE_TAP], "call" , e, Boolean.class);
    } else {
      return super.onDoubleTap(e);
    }
  }

  public boolean onDoubleTapEvent(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_DOUBLE_TAP_EVENT] != null) {
      super.onDoubleTapEvent(e);
      return (Boolean) Script.callMethod(callbackProcs[CB_DOUBLE_TAP_EVENT], "call" , e, Boolean.class);
    } else {
      return super.onDoubleTapEvent(e);
    }
  }

  public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_SINGLE_TAP_CONFIRMED] != null) {
      super.onSingleTapConfirmed(e);
      return (Boolean) Script.callMethod(callbackProcs[CB_SINGLE_TAP_CONFIRMED], "call" , e, Boolean.class);
    } else {
      return super.onSingleTapConfirmed(e);
    }
  }

}
