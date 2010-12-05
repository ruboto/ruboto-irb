package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoGLWrapper implements android.opengl.GLSurfaceView.GLWrapper {
  private Ruby __ruby__;

  public static final int CB_WRAP = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public javax.microedition.khronos.opengles.GL wrap(javax.microedition.khronos.opengles.GL gl) {
    if (callbackProcs[CB_WRAP] != null) {
      try {
        return (javax.microedition.khronos.opengles.GL)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_WRAP], "call" , JavaUtil.convertJavaToRuby(getRuby(), gl)).toJava(javax.microedition.khronos.opengles.GL.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }
}
