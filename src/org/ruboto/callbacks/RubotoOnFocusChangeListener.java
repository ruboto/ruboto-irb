package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnFocusChangeListener implements android.view.View.OnFocusChangeListener {
  private Ruby __ruby__;

  public static final int CB_FOCUS_CHANGE = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onFocusChange(android.view.View v, boolean hasFocus) {
    if (callbackProcs[CB_FOCUS_CHANGE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_FOCUS_CHANGE], "call" , JavaUtil.convertJavaToRuby(getRuby(), v), JavaUtil.convertJavaToRuby(getRuby(), hasFocus));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
