package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoEGLContextFactory implements android.opengl.GLSurfaceView.EGLContextFactory {
  private Ruby __ruby__;

  public static final int CB_CREATE_CONTEXT = 0;
  public static final int CB_DESTROY_CONTEXT = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public javax.microedition.khronos.egl.EGLContext createContext(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLConfig eglConfig) {
    if (callbackProcs[CB_CREATE_CONTEXT] != null) {
      try {
        return (javax.microedition.khronos.egl.EGLContext)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_CONTEXT], "call" , JavaUtil.convertJavaToRuby(getRuby(), egl), JavaUtil.convertJavaToRuby(getRuby(), display), JavaUtil.convertJavaToRuby(getRuby(), eglConfig)).toJava(javax.microedition.khronos.egl.EGLContext.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public void destroyContext(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLContext context) {
    if (callbackProcs[CB_DESTROY_CONTEXT] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DESTROY_CONTEXT], "call" , JavaUtil.convertJavaToRuby(getRuby(), egl), JavaUtil.convertJavaToRuby(getRuby(), display), JavaUtil.convertJavaToRuby(getRuby(), context));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
