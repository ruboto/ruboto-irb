package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

  public static final int CB_CREATE = 0;
  public static final int CB_OPEN = 1;
  public static final int CB_UPGRADE = 2;
  public static final int CB_DOWNGRADE = 3;

    private Object[] callbackProcs = new Object[4];

  public  RubotoSQLiteOpenHelper(android.content.Context context, java.lang.String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onCreate(android.database.sqlite.SQLiteDatabase db) {
    if (callbackProcs[CB_CREATE] != null) {
      Script.callMethod(callbackProcs[CB_CREATE], "call" , db);
    }
  }

  public void onOpen(android.database.sqlite.SQLiteDatabase db) {
    if (callbackProcs[CB_OPEN] != null) {
      super.onOpen(db);
      Script.callMethod(callbackProcs[CB_OPEN], "call" , db);
    } else {
      super.onOpen(db);
    }
  }

  public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
    if (callbackProcs[CB_UPGRADE] != null) {
      Script.callMethod(callbackProcs[CB_UPGRADE], "call" , new Object[]{db, oldVersion, newVersion});
    }
  }

  public void onDowngrade(android.database.sqlite.SQLiteDatabase arg0, int arg1, int arg2) {
    if (callbackProcs[CB_DOWNGRADE] != null) {
      Script.callMethod(callbackProcs[CB_DOWNGRADE], "call" , new Object[]{arg0, arg1, arg2});
    }
  }

}
