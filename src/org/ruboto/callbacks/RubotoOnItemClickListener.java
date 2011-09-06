package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

  public static final int CB_ITEM_CLICK = 0;

    private Object[] callbackProcs = new Object[1];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
    if (callbackProcs[CB_ITEM_CLICK] != null) {
      Script.callMethod(callbackProcs[CB_ITEM_CLICK], "call" , new Object[]{parent, view, position, id});
    }
  }

}
