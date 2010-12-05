package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSQLiteTransactionListener implements android.database.sqlite.SQLiteTransactionListener {
  private Ruby __ruby__;

  public static final int CB_BEGIN = 0;
  public static final int CB_COMMIT = 1;
  public static final int CB_ROLLBACK = 2;
  private IRubyObject[] callbackProcs = new IRubyObject[3];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onBegin() {
    if (callbackProcs[CB_BEGIN] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_BEGIN], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onCommit() {
    if (callbackProcs[CB_COMMIT] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_COMMIT], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onRollback() {
    if (callbackProcs[CB_ROLLBACK] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ROLLBACK], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
