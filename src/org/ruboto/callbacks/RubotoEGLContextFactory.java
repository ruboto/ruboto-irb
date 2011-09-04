package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoEGLContextFactory implements android.opengl.GLSurfaceView.EGLContextFactory {

  public static final int CB_CREATE_CONTEXT = 0;
  public static final int CB_DESTROY_CONTEXT = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public javax.microedition.khronos.egl.EGLContext createContext(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLConfig eglConfig) {
    if (callbackProcs[CB_CREATE_CONTEXT] != null) {
      return (javax.microedition.khronos.egl.EGLContext) Script.callMethod(callbackProcs[CB_CREATE_CONTEXT], "call" , new Object[]{egl, display, eglConfig}, javax.microedition.khronos.egl.EGLContext.class);
    } else {
      return null;
    }
  }

  public void destroyContext(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLContext context) {
    if (callbackProcs[CB_DESTROY_CONTEXT] != null) {
      Script.callMethod(callbackProcs[CB_DESTROY_CONTEXT], "call" , new Object[]{egl, display, context});
    }
  }

}
