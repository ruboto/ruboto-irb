package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnKeyguardExitResult implements android.app.KeyguardManager.OnKeyguardExitResult {
  private Ruby __ruby__;

  public static final int CB_KEYGUARD_EXIT_RESULT = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onKeyguardExitResult(boolean success) {
    if (callbackProcs[CB_KEYGUARD_EXIT_RESULT] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_KEYGUARD_EXIT_RESULT], "call" , JavaUtil.convertJavaToRuby(getRuby(), success));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
