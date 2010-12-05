package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnRecordPositionUpdateListener implements android.media.AudioRecord.OnRecordPositionUpdateListener {
  private Ruby __ruby__;

  public static final int CB_MARKER_REACHED = 0;
  public static final int CB_PERIODIC_NOTIFICATION = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onMarkerReached(android.media.AudioRecord recorder) {
    if (callbackProcs[CB_MARKER_REACHED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_MARKER_REACHED], "call" , JavaUtil.convertJavaToRuby(getRuby(), recorder));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onPeriodicNotification(android.media.AudioRecord recorder) {
    if (callbackProcs[CB_PERIODIC_NOTIFICATION] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PERIODIC_NOTIFICATION], "call" , JavaUtil.convertJavaToRuby(getRuby(), recorder));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
