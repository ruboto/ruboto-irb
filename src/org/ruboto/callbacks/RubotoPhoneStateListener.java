package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoPhoneStateListener extends android.telephony.PhoneStateListener {

  public static final int CB_CALL_FORWARDING_INDICATOR_CHANGED = 0;
  public static final int CB_CALL_STATE_CHANGED = 1;
  public static final int CB_CELL_LOCATION_CHANGED = 2;
  public static final int CB_DATA_ACTIVITY = 3;
  public static final int CB_DATA_CONNECTION_STATE_CHANGED = 4;
  public static final int CB_MESSAGE_WAITING_INDICATOR_CHANGED = 5;
  public static final int CB_SERVICE_STATE_CHANGED = 6;
  public static final int CB_SIGNAL_STRENGTH_CHANGED = 7;
  public static final int CB_SIGNAL_STRENGTHS_CHANGED = 8;

    private Object[] callbackProcs = new Object[10];

  public  RubotoPhoneStateListener() {
    super();
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onCallForwardingIndicatorChanged(boolean cfi) {
    if (callbackProcs[CB_CALL_FORWARDING_INDICATOR_CHANGED] != null) {
      super.onCallForwardingIndicatorChanged(cfi);
      Script.callMethod(callbackProcs[CB_CALL_FORWARDING_INDICATOR_CHANGED], "call" , cfi);
    } else {
      super.onCallForwardingIndicatorChanged(cfi);
    }
  }

  public void onCallStateChanged(int state, java.lang.String incomingNumber) {
    if (callbackProcs[CB_CALL_STATE_CHANGED] != null) {
      super.onCallStateChanged(state, incomingNumber);
      Script.callMethod(callbackProcs[CB_CALL_STATE_CHANGED], "call" , new Object[]{state, incomingNumber});
    } else {
      super.onCallStateChanged(state, incomingNumber);
    }
  }

  public void onCellLocationChanged(android.telephony.CellLocation location) {
    if (callbackProcs[CB_CELL_LOCATION_CHANGED] != null) {
      super.onCellLocationChanged(location);
      Script.callMethod(callbackProcs[CB_CELL_LOCATION_CHANGED], "call" , location);
    } else {
      super.onCellLocationChanged(location);
    }
  }

  public void onDataActivity(int direction) {
    if (callbackProcs[CB_DATA_ACTIVITY] != null) {
      super.onDataActivity(direction);
      Script.callMethod(callbackProcs[CB_DATA_ACTIVITY], "call" , direction);
    } else {
      super.onDataActivity(direction);
    }
  }

  public void onDataConnectionStateChanged(int state) {
    if (callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED] != null) {
      super.onDataConnectionStateChanged(state);
      Script.callMethod(callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED], "call" , state);
    } else {
      super.onDataConnectionStateChanged(state);
    }
  }

  public void onMessageWaitingIndicatorChanged(boolean mwi) {
    if (callbackProcs[CB_MESSAGE_WAITING_INDICATOR_CHANGED] != null) {
      super.onMessageWaitingIndicatorChanged(mwi);
      Script.callMethod(callbackProcs[CB_MESSAGE_WAITING_INDICATOR_CHANGED], "call" , mwi);
    } else {
      super.onMessageWaitingIndicatorChanged(mwi);
    }
  }

  public void onServiceStateChanged(android.telephony.ServiceState serviceState) {
    if (callbackProcs[CB_SERVICE_STATE_CHANGED] != null) {
      super.onServiceStateChanged(serviceState);
      Script.callMethod(callbackProcs[CB_SERVICE_STATE_CHANGED], "call" , serviceState);
    } else {
      super.onServiceStateChanged(serviceState);
    }
  }

  public void onSignalStrengthChanged(int asu) {
    if (callbackProcs[CB_SIGNAL_STRENGTH_CHANGED] != null) {
      super.onSignalStrengthChanged(asu);
      Script.callMethod(callbackProcs[CB_SIGNAL_STRENGTH_CHANGED], "call" , asu);
    } else {
      super.onSignalStrengthChanged(asu);
    }
  }

  public void onDataConnectionStateChanged(int state, int networkType) {
    if (callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED], "call" , new Object[]{state, networkType});
    }
  }

  public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
    if (callbackProcs[CB_SIGNAL_STRENGTHS_CHANGED] != null) {
      Script.callMethod(callbackProcs[CB_SIGNAL_STRENGTHS_CHANGED], "call" , signalStrength);
    }
  }

}
