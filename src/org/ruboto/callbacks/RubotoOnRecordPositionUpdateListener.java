package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnRecordPositionUpdateListener implements android.media.AudioRecord.OnRecordPositionUpdateListener {

  public static final int CB_MARKER_REACHED = 0;
  public static final int CB_PERIODIC_NOTIFICATION = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onMarkerReached(android.media.AudioRecord recorder) {
    if (callbackProcs[CB_MARKER_REACHED] != null) {
      Script.callMethod(callbackProcs[CB_MARKER_REACHED], "call" , recorder);
    }
  }

  public void onPeriodicNotification(android.media.AudioRecord recorder) {
    if (callbackProcs[CB_PERIODIC_NOTIFICATION] != null) {
      Script.callMethod(callbackProcs[CB_PERIODIC_NOTIFICATION], "call" , recorder);
    }
  }

}
