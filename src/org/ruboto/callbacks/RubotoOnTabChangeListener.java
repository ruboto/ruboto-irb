package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnTabChangeListener implements android.widget.TabHost.OnTabChangeListener {
  private Ruby __ruby__;

  public static final int CB_TAB_CHANGED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onTabChanged(java.lang.String tabId) {
    if (callbackProcs[CB_TAB_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_TAB_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), tabId));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
