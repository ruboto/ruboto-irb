package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnGestureListener implements android.gesture.GestureOverlayView.OnGestureListener {

  public static final int CB_GESTURE = 0;
  public static final int CB_GESTURE_CANCELLED = 1;
  public static final int CB_GESTURE_ENDED = 2;
  public static final int CB_GESTURE_STARTED = 3;

    private Object[] callbackProcs = new Object[4];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onGesture(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs != null && callbackProcs[CB_GESTURE] != null) {
      Script.callMethod(callbackProcs[CB_GESTURE], "call" , new Object[]{overlay, event});
    }
  }

  public void onGestureCancelled(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs != null && callbackProcs[CB_GESTURE_CANCELLED] != null) {
      Script.callMethod(callbackProcs[CB_GESTURE_CANCELLED], "call" , new Object[]{overlay, event});
    }
  }

  public void onGestureEnded(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs != null && callbackProcs[CB_GESTURE_ENDED] != null) {
      Script.callMethod(callbackProcs[CB_GESTURE_ENDED], "call" , new Object[]{overlay, event});
    }
  }

  public void onGestureStarted(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs != null && callbackProcs[CB_GESTURE_STARTED] != null) {
      Script.callMethod(callbackProcs[CB_GESTURE_STARTED], "call" , new Object[]{overlay, event});
    }
  }

}
