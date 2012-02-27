package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnScaleGestureListener implements android.view.ScaleGestureDetector.OnScaleGestureListener {

  public static final int CB_SCALE = 0;
  public static final int CB_SCALE_BEGIN = 1;
  public static final int CB_SCALE_END = 2;

    private Object[] callbackProcs = new Object[3];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onScale(android.view.ScaleGestureDetector detector) {
    if (callbackProcs != null && callbackProcs[CB_SCALE] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_SCALE], "call" , detector, Boolean.class);
    } else {
      return false;
    }
  }

  public boolean onScaleBegin(android.view.ScaleGestureDetector detector) {
    if (callbackProcs != null && callbackProcs[CB_SCALE_BEGIN] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_SCALE_BEGIN], "call" , detector, Boolean.class);
    } else {
      return false;
    }
  }

  public void onScaleEnd(android.view.ScaleGestureDetector detector) {
    if (callbackProcs != null && callbackProcs[CB_SCALE_END] != null) {
      Script.callMethod(callbackProcs[CB_SCALE_END], "call" , detector);
    }
  }

}
