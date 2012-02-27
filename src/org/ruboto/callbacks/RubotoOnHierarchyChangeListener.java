package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnHierarchyChangeListener implements android.view.ViewGroup.OnHierarchyChangeListener {

  public static final int CB_CHILD_VIEW_ADDED = 0;
  public static final int CB_CHILD_VIEW_REMOVED = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onChildViewAdded(android.view.View parent, android.view.View child) {
    if (callbackProcs != null && callbackProcs[CB_CHILD_VIEW_ADDED] != null) {
      Script.callMethod(callbackProcs[CB_CHILD_VIEW_ADDED], "call" , new Object[]{parent, child});
    }
  }

  public void onChildViewRemoved(android.view.View parent, android.view.View child) {
    if (callbackProcs != null && callbackProcs[CB_CHILD_VIEW_REMOVED] != null) {
      Script.callMethod(callbackProcs[CB_CHILD_VIEW_REMOVED], "call" , new Object[]{parent, child});
    }
  }

}
