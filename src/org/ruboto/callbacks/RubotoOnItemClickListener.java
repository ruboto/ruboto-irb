package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
  private Ruby __ruby__;

  public static final int CB_ITEM_CLICK = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
    if (callbackProcs[CB_ITEM_CLICK] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), parent), JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), position), JavaUtil.convertJavaToRuby(getRuby(), id)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ITEM_CLICK], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
