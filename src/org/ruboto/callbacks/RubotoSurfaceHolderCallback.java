package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSurfaceHolderCallback implements android.view.SurfaceHolder.Callback {
  private Ruby __ruby__;

  public static final int CB_SURFACE_CHANGED = 0;
  public static final int CB_SURFACE_CREATED = 1;
  public static final int CB_SURFACE_DESTROYED = 2;
  private IRubyObject[] callbackProcs = new IRubyObject[3];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void surfaceChanged(android.view.SurfaceHolder holder, int format, int width, int height) {
    if (callbackProcs[CB_SURFACE_CHANGED] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), holder), JavaUtil.convertJavaToRuby(getRuby(), format), JavaUtil.convertJavaToRuby(getRuby(), width), JavaUtil.convertJavaToRuby(getRuby(), height)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SURFACE_CHANGED], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void surfaceCreated(android.view.SurfaceHolder holder) {
    if (callbackProcs[CB_SURFACE_CREATED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SURFACE_CREATED], "call" , JavaUtil.convertJavaToRuby(getRuby(), holder));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void surfaceDestroyed(android.view.SurfaceHolder holder) {
    if (callbackProcs[CB_SURFACE_DESTROYED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SURFACE_DESTROYED], "call" , JavaUtil.convertJavaToRuby(getRuby(), holder));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
