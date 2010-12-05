package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSensorEventListener implements android.hardware.SensorEventListener {
  private Ruby __ruby__;

  public static final int CB_ACCURACY_CHANGED = 0;
  public static final int CB_SENSOR_CHANGED = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    if (callbackProcs[CB_ACCURACY_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ACCURACY_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), sensor), JavaUtil.convertJavaToRuby(getRuby(), accuracy));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onSensorChanged(android.hardware.SensorEvent event) {
    if (callbackProcs[CB_SENSOR_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SENSOR_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), event));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
