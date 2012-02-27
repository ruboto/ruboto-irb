package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoEGLWindowSurfaceFactory implements android.opengl.GLSurfaceView.EGLWindowSurfaceFactory {

  public static final int CB_CREATE_WINDOW_SURFACE = 0;
  public static final int CB_DESTROY_SURFACE = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public javax.microedition.khronos.egl.EGLSurface createWindowSurface(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLConfig config, java.lang.Object nativeWindow) {
    if (callbackProcs != null && callbackProcs[CB_CREATE_WINDOW_SURFACE] != null) {
      return (javax.microedition.khronos.egl.EGLSurface) Script.callMethod(callbackProcs[CB_CREATE_WINDOW_SURFACE], "call" , new Object[]{egl, display, config, nativeWindow}, javax.microedition.khronos.egl.EGLSurface.class);
    } else {
      return null;
    }
  }

  public void destroySurface(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLSurface surface) {
    if (callbackProcs != null && callbackProcs[CB_DESTROY_SURFACE] != null) {
      Script.callMethod(callbackProcs[CB_DESTROY_SURFACE], "call" , new Object[]{egl, display, surface});
    }
  }

}
