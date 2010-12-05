package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnGesturingListener implements android.gesture.GestureOverlayView.OnGesturingListener {
  private Ruby __ruby__;

  public static final int CB_GESTURING_ENDED = 0;
  public static final int CB_GESTURING_STARTED = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onGesturingEnded(android.gesture.GestureOverlayView overlay) {
    if (callbackProcs[CB_GESTURING_ENDED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURING_ENDED], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onGesturingStarted(android.gesture.GestureOverlayView overlay) {
    if (callbackProcs[CB_GESTURING_STARTED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GESTURING_STARTED], "call" , JavaUtil.convertJavaToRuby(getRuby(), overlay));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
