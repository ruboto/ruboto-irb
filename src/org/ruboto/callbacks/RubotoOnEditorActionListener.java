package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnEditorActionListener implements android.widget.TextView.OnEditorActionListener {
  private Ruby __ruby__;

  public static final int CB_EDITOR_ACTION = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
    if (callbackProcs[CB_EDITOR_ACTION] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_EDITOR_ACTION], "call" , JavaUtil.convertJavaToRuby(getRuby(), v), JavaUtil.convertJavaToRuby(getRuby(), actionId), JavaUtil.convertJavaToRuby(getRuby(), event)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }
}
