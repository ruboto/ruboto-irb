package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnHierarchyChangeListener implements android.view.ViewGroup.OnHierarchyChangeListener {
  private Ruby __ruby__;

  public static final int CB_CHILD_VIEW_ADDED = 0;
  public static final int CB_CHILD_VIEW_REMOVED = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onChildViewAdded(android.view.View parent, android.view.View child) {
    if (callbackProcs[CB_CHILD_VIEW_ADDED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CHILD_VIEW_ADDED], "call" , JavaUtil.convertJavaToRuby(getRuby(), parent), JavaUtil.convertJavaToRuby(getRuby(), child));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onChildViewRemoved(android.view.View parent, android.view.View child) {
    if (callbackProcs[CB_CHILD_VIEW_REMOVED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CHILD_VIEW_REMOVED], "call" , JavaUtil.convertJavaToRuby(getRuby(), parent), JavaUtil.convertJavaToRuby(getRuby(), child));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
