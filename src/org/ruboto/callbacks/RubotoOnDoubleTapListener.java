package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnDoubleTapListener implements android.view.GestureDetector.OnDoubleTapListener {

  public static final int CB_DOUBLE_TAP = 0;
  public static final int CB_DOUBLE_TAP_EVENT = 1;
  public static final int CB_SINGLE_TAP_CONFIRMED = 2;

    private Object[] callbackProcs = new Object[3];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onDoubleTap(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_DOUBLE_TAP] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_DOUBLE_TAP], "call" , e, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onDoubleTapEvent(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_DOUBLE_TAP_EVENT] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_DOUBLE_TAP_EVENT], "call" , e, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
    if (callbackProcs != null && callbackProcs[CB_SINGLE_TAP_CONFIRMED] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_SINGLE_TAP_CONFIRMED], "call" , e, Boolean.class);
    } else {
      return false;
    }
  }

}
