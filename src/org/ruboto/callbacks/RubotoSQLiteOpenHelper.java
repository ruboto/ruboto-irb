package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {
  private Ruby __ruby__;

  public static final int CB_CREATE = 0;
  public static final int CB_OPEN = 1;
  public static final int CB_UPGRADE = 2;
  private IRubyObject[] callbackProcs = new IRubyObject[3];

  public  RubotoSQLiteOpenHelper(android.content.Context context, java.lang.String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onCreate(android.database.sqlite.SQLiteDatabase db) {
    if (callbackProcs[CB_CREATE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), db));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onOpen(android.database.sqlite.SQLiteDatabase db) {
    if (callbackProcs[CB_OPEN] != null) {
      super.onOpen(db);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_OPEN], "call" , JavaUtil.convertJavaToRuby(getRuby(), db));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onOpen(db);
    }
  }

  public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
    if (callbackProcs[CB_UPGRADE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_UPGRADE], "call" , JavaUtil.convertJavaToRuby(getRuby(), db), JavaUtil.convertJavaToRuby(getRuby(), oldVersion), JavaUtil.convertJavaToRuby(getRuby(), newVersion));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
