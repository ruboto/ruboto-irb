package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnGestureListener implements android.gesture.GestureOverlayView.OnGestureListener {
  private Ruby __ruby__;

  public static final int CB_GESTURE = 0;
  public static final int CB_GESTURE_CANCELLED = 1;
  public static final int CB_GESTURE_ENDED = 2;
  public static final int CB_GESTURE_STARTED = 3;
  private IRubyObject[] callbackProcs = new IRubyObject[4];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onGesture(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs[CB_GESTURE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURE], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay), JavaUtil.convertJavaToRuby(getRuby(), event));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onGestureCancelled(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs[CB_GESTURE_CANCELLED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURE_CANCELLED], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay), JavaUtil.convertJavaToRuby(getRuby(), event));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onGestureEnded(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs[CB_GESTURE_ENDED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURE_ENDED], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay), JavaUtil.convertJavaToRuby(getRuby(), event));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onGestureStarted(android.gesture.GestureOverlayView overlay, android.view.MotionEvent event) {
    if (callbackProcs[CB_GESTURE_STARTED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURE_STARTED], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay), JavaUtil.convertJavaToRuby(getRuby(), event));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
