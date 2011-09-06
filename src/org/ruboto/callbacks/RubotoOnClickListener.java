package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnClickListener implements android.view.View.OnClickListener {

  public static final int CB_CLICK = 0;

    private Object[] callbackProcs = new Object[1];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onClick(android.view.View v) {
    if (callbackProcs[CB_CLICK] != null) {
      Script.callMethod(callbackProcs[CB_CLICK], "call" , v);
    }
  }

}
