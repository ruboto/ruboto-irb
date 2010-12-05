package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnClickListener implements android.view.View.OnClickListener {
  private Ruby __ruby__;

  public static final int CB_CLICK = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onClick(android.view.View v) {
    if (callbackProcs[CB_CLICK] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CLICK], "call" , JavaUtil.convertJavaToRuby(getRuby(), v));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
