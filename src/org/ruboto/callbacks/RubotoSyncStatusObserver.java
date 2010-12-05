package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSyncStatusObserver implements android.content.SyncStatusObserver {
  private Ruby __ruby__;

  public static final int CB_STATUS_CHANGED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onStatusChanged(int which) {
    if (callbackProcs[CB_STATUS_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_STATUS_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), which));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
