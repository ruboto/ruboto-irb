package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoSensorEventListener implements android.hardware.SensorEventListener {

  public static final int CB_ACCURACY_CHANGED = 0;
  public static final int CB_SENSOR_CHANGED = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    if (callbackProcs[CB_ACCURACY_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_ACCURACY_CHANGED], "call" , new Object[]{sensor, accuracy});
    }
  }

  public void onSensorChanged(android.hardware.SensorEvent event) {
    if (callbackProcs[CB_SENSOR_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_SENSOR_CHANGED], "call" , event);
    }
  }

}
