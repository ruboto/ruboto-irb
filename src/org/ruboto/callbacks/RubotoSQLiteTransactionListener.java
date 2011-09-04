package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoSQLiteTransactionListener implements android.database.sqlite.SQLiteTransactionListener {

  public static final int CB_BEGIN = 0;
  public static final int CB_COMMIT = 1;
  public static final int CB_ROLLBACK = 2;

    private Object[] callbackProcs = new Object[3];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onBegin() {
    if (callbackProcs[CB_BEGIN] != null) {
      Script.callMethod(callbackProcs[CB_BEGIN], "call" );
    }
  }

  public void onCommit() {
    if (callbackProcs[CB_COMMIT] != null) {
      Script.callMethod(callbackProcs[CB_COMMIT], "call" );
    }
  }

  public void onRollback() {
    if (callbackProcs[CB_ROLLBACK] != null) {
      Script.callMethod(callbackProcs[CB_ROLLBACK], "call" );
    }
  }

}
