package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoLocationListener implements android.location.LocationListener {

  public static final int CB_LOCATION_CHANGED = 0;
  public static final int CB_PROVIDER_DISABLED = 1;
  public static final int CB_PROVIDER_ENABLED = 2;
  public static final int CB_STATUS_CHANGED = 3;

    private Object[] callbackProcs = new Object[4];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onLocationChanged(android.location.Location location) {
    if (callbackProcs[CB_LOCATION_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_LOCATION_CHANGED], "call" , location);
    }
  }

  public void onProviderDisabled(java.lang.String provider) {
    if (callbackProcs[CB_PROVIDER_DISABLED] != null) {
      Script.callMethod(callbackProcs[CB_PROVIDER_DISABLED], "call" , provider);
    }
  }

  public void onProviderEnabled(java.lang.String provider) {
    if (callbackProcs[CB_PROVIDER_ENABLED] != null) {
      Script.callMethod(callbackProcs[CB_PROVIDER_ENABLED], "call" , provider);
    }
  }

  public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) {
    if (callbackProcs[CB_STATUS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_STATUS_CHANGED], "call" , new Object[]{provider, status, extras});
    }
  }

}
