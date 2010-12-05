package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnGesturePerformedListener implements android.gesture.GestureOverlayView.OnGesturePerformedListener {
  private Ruby __ruby__;

  public static final int CB_GESTURE_PERFORMED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onGesturePerformed(android.gesture.GestureOverlayView overlay, android.gesture.Gesture gesture) {
    if (callbackProcs[CB_GESTURE_PERFORMED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURE_PERFORMED], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay), JavaUtil.convertJavaToRuby(getRuby(), gesture));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
