package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoEGLWindowSurfaceFactory implements android.opengl.GLSurfaceView.EGLWindowSurfaceFactory {
  private Ruby __ruby__;

  public static final int CB_CREATE_WINDOW_SURFACE = 0;
  public static final int CB_DESTROY_SURFACE = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public javax.microedition.khronos.egl.EGLSurface createWindowSurface(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLConfig config, java.lang.Object nativeWindow) {
    if (callbackProcs[CB_CREATE_WINDOW_SURFACE] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), egl), JavaUtil.convertJavaToRuby(getRuby(), display), JavaUtil.convertJavaToRuby(getRuby(), config), JavaUtil.convertJavaToRuby(getRuby(), nativeWindow)};
        return (javax.microedition.khronos.egl.EGLSurface)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_WINDOW_SURFACE], "call" , args).toJava(javax.microedition.khronos.egl.EGLSurface.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public void destroySurface(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLSurface surface) {
    if (callbackProcs[CB_DESTROY_SURFACE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DESTROY_SURFACE], "call" , JavaUtil.convertJavaToRuby(getRuby(), egl), JavaUtil.convertJavaToRuby(getRuby(), display), JavaUtil.convertJavaToRuby(getRuby(), surface));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
