package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnDoubleTapListener implements android.view.GestureDetector.OnDoubleTapListener {
  private Ruby __ruby__;

  public static final int CB_DOUBLE_TAP = 0;
  public static final int CB_DOUBLE_TAP_EVENT = 1;
  public static final int CB_SINGLE_TAP_CONFIRMED = 2;
  private IRubyObject[] callbackProcs = new IRubyObject[3];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onDoubleTap(android.view.MotionEvent e) {
    if (callbackProcs[CB_DOUBLE_TAP] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DOUBLE_TAP], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean onDoubleTapEvent(android.view.MotionEvent e) {
    if (callbackProcs[CB_DOUBLE_TAP_EVENT] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DOUBLE_TAP_EVENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
    if (callbackProcs[CB_SINGLE_TAP_CONFIRMED] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SINGLE_TAP_CONFIRMED], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }
}
