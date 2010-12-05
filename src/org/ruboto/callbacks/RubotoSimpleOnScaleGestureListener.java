package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSimpleOnScaleGestureListener extends android.view.ScaleGestureDetector.SimpleOnScaleGestureListener {
  private Ruby __ruby__;

  public static final int CB_SCALE = 0;
  public static final int CB_SCALE_BEGIN = 1;
  public static final int CB_SCALE_END = 2;
  private IRubyObject[] callbackProcs = new IRubyObject[3];

  public  RubotoSimpleOnScaleGestureListener() {
  }

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onScale(android.view.ScaleGestureDetector detector) {
    if (callbackProcs[CB_SCALE] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SCALE], "call" , JavaUtil.convertJavaToRuby(getRuby(), detector)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean onScaleBegin(android.view.ScaleGestureDetector detector) {
    if (callbackProcs[CB_SCALE_BEGIN] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SCALE_BEGIN], "call" , JavaUtil.convertJavaToRuby(getRuby(), detector)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public void onScaleEnd(android.view.ScaleGestureDetector detector) {
    if (callbackProcs[CB_SCALE_END] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SCALE_END], "call" , JavaUtil.convertJavaToRuby(getRuby(), detector));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
