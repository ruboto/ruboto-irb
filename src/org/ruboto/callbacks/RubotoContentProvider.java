package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoContentProvider extends android.content.ContentProvider {
  private Ruby __ruby__;

  public static final int CB_DELETE = 0;
  public static final int CB_GET_TYPE = 1;
  public static final int CB_INSERT = 2;
  public static final int CB_CREATE = 3;
  public static final int CB_QUERY = 4;
  public static final int CB_UPDATE = 5;
  private IRubyObject[] callbackProcs = new IRubyObject[6];

  public  RubotoContentProvider() {
    super();
  }

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
    if (callbackProcs[CB_DELETE] != null) {
      try {
        return (Integer)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DELETE], "call" , JavaUtil.convertJavaToRuby(getRuby(), uri), JavaUtil.convertJavaToRuby(getRuby(), selection), JavaUtil.convertJavaToRuby(getRuby(), selectionArgs)).toJava(int.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return 0;
      }
    } else {
      return 0;
    }
  }

  public java.lang.String getType(android.net.Uri uri) {
    if (callbackProcs[CB_GET_TYPE] != null) {
      try {
        return (java.lang.String)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_GET_TYPE], "call" , JavaUtil.convertJavaToRuby(getRuby(), uri)).toJava(java.lang.String.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
    if (callbackProcs[CB_INSERT] != null) {
      try {
        return (android.net.Uri)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_INSERT], "call" , JavaUtil.convertJavaToRuby(getRuby(), uri), JavaUtil.convertJavaToRuby(getRuby(), values)).toJava(android.net.Uri.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public boolean onCreate() {
    if (callbackProcs[CB_CREATE] != null) {
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CREATE], "call" ).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
    if (callbackProcs[CB_QUERY] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), uri), JavaUtil.convertJavaToRuby(getRuby(), projection), JavaUtil.convertJavaToRuby(getRuby(), selection), JavaUtil.convertJavaToRuby(getRuby(), selectionArgs), JavaUtil.convertJavaToRuby(getRuby(), sortOrder)};
        return (android.database.Cursor)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_QUERY], "call" , args).toJava(android.database.Cursor.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
    if (callbackProcs[CB_UPDATE] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), uri), JavaUtil.convertJavaToRuby(getRuby(), values), JavaUtil.convertJavaToRuby(getRuby(), selection), JavaUtil.convertJavaToRuby(getRuby(), selectionArgs)};
        return (Integer)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_UPDATE], "call" , args).toJava(int.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return 0;
      }
    } else {
      return 0;
    }
  }
}
