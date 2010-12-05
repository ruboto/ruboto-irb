package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoGLSurfaceViewRenderer implements android.opengl.GLSurfaceView.Renderer {
  private Ruby __ruby__;

  public static final int CB_DRAW_FRAME = 0;
  public static final int CB_SURFACE_CHANGED = 1;
  public static final int CB_SURFACE_CREATED = 2;
  private IRubyObject[] callbackProcs = new IRubyObject[3];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl) {
    if (callbackProcs[CB_DRAW_FRAME] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DRAW_FRAME], "call" , JavaUtil.convertJavaToRuby(getRuby(), gl));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 gl, int width, int height) {
    if (callbackProcs[CB_SURFACE_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SURFACE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), gl), JavaUtil.convertJavaToRuby(getRuby(), width), JavaUtil.convertJavaToRuby(getRuby(), height));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
    if (callbackProcs[CB_SURFACE_CREATED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SURFACE_CREATED], "call" , JavaUtil.convertJavaToRuby(getRuby(), gl), JavaUtil.convertJavaToRuby(getRuby(), config));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
