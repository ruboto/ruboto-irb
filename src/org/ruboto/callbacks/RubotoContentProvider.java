package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoContentProvider extends android.content.ContentProvider {

  public static final int CB_DELETE = 0;
  public static final int CB_GET_TYPE = 1;
  public static final int CB_INSERT = 2;
  public static final int CB_CREATE = 3;
  public static final int CB_QUERY = 4;
  public static final int CB_UPDATE = 5;

    private Object[] callbackProcs = new Object[6];

  public  RubotoContentProvider() {
    super();
  }

  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
    if (callbackProcs != null && callbackProcs[CB_DELETE] != null) {
      return (Integer) Script.callMethod(callbackProcs[CB_DELETE], "call" , new Object[]{uri, selection, selectionArgs}, Integer.class);
    } else {
      return 0;
    }
  }

  public java.lang.String getType(android.net.Uri uri) {
    if (callbackProcs != null && callbackProcs[CB_GET_TYPE] != null) {
      return (java.lang.String) Script.callMethod(callbackProcs[CB_GET_TYPE], "call" , uri, java.lang.String.class);
    } else {
      return null;
    }
  }

  public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
    if (callbackProcs != null && callbackProcs[CB_INSERT] != null) {
      return (android.net.Uri) Script.callMethod(callbackProcs[CB_INSERT], "call" , new Object[]{uri, values}, android.net.Uri.class);
    } else {
      return null;
    }
  }

  public boolean onCreate() {
    if (callbackProcs != null && callbackProcs[CB_CREATE] != null) {
      return (Boolean) Script.callMethod(callbackProcs[CB_CREATE], "call" , Boolean.class);
    } else {
      return false;
    }
  }

  public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
    if (callbackProcs != null && callbackProcs[CB_QUERY] != null) {
      return (android.database.Cursor) Script.callMethod(callbackProcs[CB_QUERY], "call" , new Object[]{uri, projection, selection, selectionArgs, sortOrder}, android.database.Cursor.class);
    } else {
      return null;
    }
  }

  public int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
    if (callbackProcs != null && callbackProcs[CB_UPDATE] != null) {
      return (Integer) Script.callMethod(callbackProcs[CB_UPDATE], "call" , new Object[]{uri, values, selection, selectionArgs}, Integer.class);
    } else {
      return 0;
    }
  }

}
