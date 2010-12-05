package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoNmeaListener implements android.location.GpsStatus.NmeaListener {
  private Ruby __ruby__;

  public static final int CB_NMEA_RECEIVED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onNmeaReceived(long timestamp, java.lang.String nmea) {
    if (callbackProcs[CB_NMEA_RECEIVED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_NMEA_RECEIVED], "call" , JavaUtil.convertJavaToRuby(getRuby(), timestamp), JavaUtil.convertJavaToRuby(getRuby(), nmea));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
