package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoSurfaceHolderCallback implements android.view.SurfaceHolder.Callback {

  public static final int CB_SURFACE_CHANGED = 0;
  public static final int CB_SURFACE_CREATED = 1;
  public static final int CB_SURFACE_DESTROYED = 2;

    private Object[] callbackProcs = new Object[3];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
    if (callbackProcs != null && callbackProcs[CB_SURFACE_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_SURFACE_CHANGED], "call" , new Object[]{holder, format, width, height});
    }
  }

  public void surfaceCreated(android.view.SurfaceHolder holder) {
    if (callbackProcs != null && callbackProcs[CB_SURFACE_CREATED] != null) {
      Script.callMethod(callbackProcs[CB_SURFACE_CREATED], "call" , holder);
    }
  }

  public void surfaceDestroyed(android.view.SurfaceHolder holder) {
    if (callbackProcs != null && callbackProcs[CB_SURFACE_DESTROYED] != null) {
      Script.callMethod(callbackProcs[CB_SURFACE_DESTROYED], "call" , holder);
    }
  }

}
