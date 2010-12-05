package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoEGLConfigChooser implements android.opengl.GLSurfaceView.EGLConfigChooser {
  private Ruby __ruby__;

  public static final int CB_CHOOSE_CONFIG = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public javax.microedition.khronos.egl.EGLConfig chooseConfig(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display) {
    if (callbackProcs[CB_CHOOSE_CONFIG] != null) {
      try {
        return (javax.microedition.khronos.egl.EGLConfig)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CHOOSE_CONFIG], "call" , JavaUtil.convertJavaToRuby(getRuby(), egl), JavaUtil.convertJavaToRuby(getRuby(), display)).toJava(javax.microedition.khronos.egl.EGLConfig.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }
}
