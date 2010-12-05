package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoDialogOnDismissListener implements android.content.DialogInterface.OnDismissListener {
  private Ruby __ruby__;

  public static final int CB_DISMISS = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onDismiss(android.content.DialogInterface dialog) {
    if (callbackProcs[CB_DISMISS] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DISMISS], "call" , JavaUtil.convertJavaToRuby(getRuby(), dialog));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
