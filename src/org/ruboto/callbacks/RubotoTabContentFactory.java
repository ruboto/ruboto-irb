package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoTabContentFactory implements android.widget.TabHost.TabContentFactory {
  private Ruby __ruby__;

  public static final int CB_CREATE_TAB_CONTENT = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public android.view.View createTabContent(java.lang.String tag) {
    if (callbackProcs[CB_CREATE_TAB_CONTENT] != null) {
      try {
        return (android.view.View)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE_TAB_CONTENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), tag)).toJava(android.view.View.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }
}
