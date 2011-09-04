package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnGesturingListener implements android.gesture.GestureOverlayView.OnGesturingListener {

  public static final int CB_GESTURING_ENDED = 0;
  public static final int CB_GESTURING_STARTED = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onGesturingEnded(android.gesture.GestureOverlayView overlay) {
    if (callbackProcs[CB_GESTURING_ENDED] != null) {
      Script.callMethod(callbackProcs[CB_GESTURING_ENDED], "call" , overlay);
    }
  }

  public void onGesturingStarted(android.gesture.GestureOverlayView overlay) {
    if (callbackProcs[CB_GESTURING_STARTED] != null) {
      Script.callMethod(callbackProcs[CB_GESTURING_STARTED], "call" , overlay);
    }
  }

}
