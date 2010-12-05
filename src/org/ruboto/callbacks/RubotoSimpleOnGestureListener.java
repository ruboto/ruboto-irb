package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSimpleOnGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {
  private Ruby __ruby__;

  public static final int CB_DOWN = 0;
  public static final int CB_FLING = 1;
  public static final int CB_LONG_PRESS = 2;
  public static final int CB_SCROLL = 3;
  public static final int CB_SHOW_PRESS = 4;
  public static final int CB_SINGLE_TAP_UP = 5;
  public static final int CB_DOUBLE_TAP = 6;
  public static final int CB_DOUBLE_TAP_EVENT = 7;
  public static final int CB_SINGLE_TAP_CONFIRMED = 8;
  private IRubyObject[] callbackProcs = new IRubyObject[9];

  public  RubotoSimpleOnGestureListener() {
    super();
  }

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onDown(android.view.MotionEvent e) {
    if (callbackProcs[CB_DOWN] != null) {
      super.onDown(e);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DOWN], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onDown(e);
    }
  }

  public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
    if (callbackProcs[CB_FLING] != null) {
      super.onFling(e1, e2, velocityX, velocityY);
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), e1), JavaUtil.convertJavaToRuby(getRuby(), e2), JavaUtil.convertJavaToRuby(getRuby(), velocityX), JavaUtil.convertJavaToRuby(getRuby(), velocityY)};
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_FLING], "call" , args).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onFling(e1, e2, velocityX, velocityY);
    }
  }

  public void onLongPress(android.view.MotionEvent e) {
    if (callbackProcs[CB_LONG_PRESS] != null) {
      super.onLongPress(e);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_LONG_PRESS], "call" , JavaUtil.convertJavaToRuby(getRuby(), e));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onLongPress(e);
    }
  }

  public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
    if (callbackProcs[CB_SCROLL] != null) {
      super.onScroll(e1, e2, distanceX, distanceY);
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), e1), JavaUtil.convertJavaToRuby(getRuby(), e2), JavaUtil.convertJavaToRuby(getRuby(), distanceX), JavaUtil.convertJavaToRuby(getRuby(), distanceY)};
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SCROLL], "call" , args).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onScroll(e1, e2, distanceX, distanceY);
    }
  }

  public void onShowPress(android.view.MotionEvent e) {
    if (callbackProcs[CB_SHOW_PRESS] != null) {
      super.onShowPress(e);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SHOW_PRESS], "call" , JavaUtil.convertJavaToRuby(getRuby(), e));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onShowPress(e);
    }
  }

  public boolean onSingleTapUp(android.view.MotionEvent e) {
    if (callbackProcs[CB_SINGLE_TAP_UP] != null) {
      super.onSingleTapUp(e);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SINGLE_TAP_UP], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onSingleTapUp(e);
    }
  }

  public boolean onDoubleTap(android.view.MotionEvent e) {
    if (callbackProcs[CB_DOUBLE_TAP] != null) {
      super.onDoubleTap(e);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DOUBLE_TAP], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onDoubleTap(e);
    }
  }

  public boolean onDoubleTapEvent(android.view.MotionEvent e) {
    if (callbackProcs[CB_DOUBLE_TAP_EVENT] != null) {
      super.onDoubleTapEvent(e);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DOUBLE_TAP_EVENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onDoubleTapEvent(e);
    }
  }

  public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
    if (callbackProcs[CB_SINGLE_TAP_CONFIRMED] != null) {
      super.onSingleTapConfirmed(e);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SINGLE_TAP_CONFIRMED], "call" , JavaUtil.convertJavaToRuby(getRuby(), e)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onSingleTapConfirmed(e);
    }
  }
}
