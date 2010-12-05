package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoPhoneStateListener extends android.telephony.PhoneStateListener {
  private Ruby __ruby__;

  public static final int CB_CALL_FORWARDING_INDICATOR_CHANGED = 0;
  public static final int CB_CALL_STATE_CHANGED = 1;
  public static final int CB_CELL_LOCATION_CHANGED = 2;
  public static final int CB_DATA_ACTIVITY = 3;
  public static final int CB_DATA_CONNECTION_STATE_CHANGED = 4;
  public static final int CB_MESSAGE_WAITING_INDICATOR_CHANGED = 5;
  public static final int CB_SERVICE_STATE_CHANGED = 6;
  public static final int CB_SIGNAL_STRENGTH_CHANGED = 7;
  public static final int CB_SIGNAL_STRENGTHS_CHANGED = 8;
  private IRubyObject[] callbackProcs = new IRubyObject[10];

  public  RubotoPhoneStateListener() {
    super();
  }

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onCallForwardingIndicatorChanged(boolean cfi) {
    if (callbackProcs[CB_CALL_FORWARDING_INDICATOR_CHANGED] != null) {
      super.onCallForwardingIndicatorChanged(cfi);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CALL_FORWARDING_INDICATOR_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), cfi));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onCallForwardingIndicatorChanged(cfi);
    }
  }

  public void onCallStateChanged(int state, java.lang.String incomingNumber) {
    if (callbackProcs[CB_CALL_STATE_CHANGED] != null) {
      super.onCallStateChanged(state, incomingNumber);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CALL_STATE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), state), JavaUtil.convertJavaToRuby(getRuby(), incomingNumber));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onCallStateChanged(state, incomingNumber);
    }
  }

  public void onCellLocationChanged(android.telephony.CellLocation location) {
    if (callbackProcs[CB_CELL_LOCATION_CHANGED] != null) {
      super.onCellLocationChanged(location);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CELL_LOCATION_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), location));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onCellLocationChanged(location);
    }
  }

  public void onDataActivity(int direction) {
    if (callbackProcs[CB_DATA_ACTIVITY] != null) {
      super.onDataActivity(direction);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DATA_ACTIVITY], "call" , JavaUtil.convertJavaToRuby(getRuby(), direction));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onDataActivity(direction);
    }
  }

  public void onDataConnectionStateChanged(int state) {
    if (callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED] != null) {
      super.onDataConnectionStateChanged(state);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), state));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onDataConnectionStateChanged(state);
    }
  }

  public void onMessageWaitingIndicatorChanged(boolean mwi) {
    if (callbackProcs[CB_MESSAGE_WAITING_INDICATOR_CHANGED] != null) {
      super.onMessageWaitingIndicatorChanged(mwi);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_MESSAGE_WAITING_INDICATOR_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), mwi));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onMessageWaitingIndicatorChanged(mwi);
    }
  }

  public void onServiceStateChanged(android.telephony.ServiceState serviceState) {
    if (callbackProcs[CB_SERVICE_STATE_CHANGED] != null) {
      super.onServiceStateChanged(serviceState);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SERVICE_STATE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), serviceState));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onServiceStateChanged(serviceState);
    }
  }

  public void onSignalStrengthChanged(int asu) {
    if (callbackProcs[CB_SIGNAL_STRENGTH_CHANGED] != null) {
      super.onSignalStrengthChanged(asu);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SIGNAL_STRENGTH_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), asu));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onSignalStrengthChanged(asu);
    }
  }

  public void onDataConnectionStateChanged(int state, int networkType) {
    if (callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DATA_CONNECTION_STATE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), state), JavaUtil.convertJavaToRuby(getRuby(), networkType));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
    if (callbackProcs[CB_SIGNAL_STRENGTHS_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SIGNAL_STRENGTHS_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), signalStrength));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
