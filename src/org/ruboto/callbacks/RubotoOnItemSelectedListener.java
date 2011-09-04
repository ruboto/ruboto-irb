package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

  public static final int CB_ITEM_SELECTED = 0;
  public static final int CB_NOTHING_SELECTED = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
    if (callbackProcs[CB_ITEM_SELECTED] != null) {
      Script.callMethod(callbackProcs[CB_ITEM_SELECTED], "call" , new Object[]{parent, view, position, id});
    }
  }

  public void onNothingSelected(android.widget.AdapterView<?> parent) {
    if (callbackProcs[CB_NOTHING_SELECTED] != null) {
      Script.callMethod(callbackProcs[CB_NOTHING_SELECTED], "call" , parent);
    }
  }

}
