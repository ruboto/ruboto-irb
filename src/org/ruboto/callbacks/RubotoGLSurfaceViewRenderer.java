package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoGLSurfaceViewRenderer implements android.opengl.GLSurfaceView.Renderer {

  public static final int CB_DRAW_FRAME = 0;
  public static final int CB_SURFACE_CHANGED = 1;
  public static final int CB_SURFACE_CREATED = 2;

    private Object[] callbackProcs = new Object[3];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl) {
    if (callbackProcs != null && callbackProcs[CB_DRAW_FRAME] != null) {
      Script.callMethod(callbackProcs[CB_DRAW_FRAME], "call" , gl);
    }
  }

  public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 gl, int width, int height) {
    if (callbackProcs != null && callbackProcs[CB_SURFACE_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_SURFACE_CHANGED], "call" , new Object[]{gl, width, height});
    }
  }

  public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
    if (callbackProcs != null && callbackProcs[CB_SURFACE_CREATED] != null) {
      Script.callMethod(callbackProcs[CB_SURFACE_CREATED], "call" , new Object[]{gl, config});
    }
  }

}
