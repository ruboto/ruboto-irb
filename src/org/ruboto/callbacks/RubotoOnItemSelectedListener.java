package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
  private Ruby __ruby__;

  public static final int CB_ITEM_SELECTED = 0;
  public static final int CB_NOTHING_SELECTED = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
    if (callbackProcs[CB_ITEM_SELECTED] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), parent), JavaUtil.convertJavaToRuby(getRuby(), view), JavaUtil.convertJavaToRuby(getRuby(), position), JavaUtil.convertJavaToRuby(getRuby(), id)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ITEM_SELECTED], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onNothingSelected(android.widget.AdapterView<?> parent) {
    if (callbackProcs[CB_NOTHING_SELECTED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_NOTHING_SELECTED], "call" , JavaUtil.convertJavaToRuby(getRuby(), parent));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
