package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoSQLiteCursorFactory implements android.database.sqlite.SQLiteDatabase.CursorFactory {
  private Ruby __ruby__;

  public static final int CB_NEW_CURSOR = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public android.database.Cursor newCursor(android.database.sqlite.SQLiteDatabase db, android.database.sqlite.SQLiteCursorDriver masterQuery, java.lang.String editTable, android.database.sqlite.SQLiteQuery query) {
    if (callbackProcs[CB_NEW_CURSOR] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), db), JavaUtil.convertJavaToRuby(getRuby(), masterQuery), JavaUtil.convertJavaToRuby(getRuby(), editTable), JavaUtil.convertJavaToRuby(getRuby(), query)};
        return (android.database.Cursor)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_NEW_CURSOR], "call" , args).toJava(android.database.Cursor.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }
}
