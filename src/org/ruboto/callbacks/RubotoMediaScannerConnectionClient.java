package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoMediaScannerConnectionClient implements android.media.MediaScannerConnection.MediaScannerConnectionClient {

  public static final int CB_MEDIA_SCANNER_CONNECTED = 0;
  public static final int CB_SCAN_COMPLETED = 1;

    private Object[] callbackProcs = new Object[2];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onMediaScannerConnected() {
    if (callbackProcs[CB_MEDIA_SCANNER_CONNECTED] != null) {
      Script.callMethod(callbackProcs[CB_MEDIA_SCANNER_CONNECTED], "call" );
    }
  }

  public void onScanCompleted(java.lang.String path, android.net.Uri uri) {
    if (callbackProcs[CB_SCAN_COMPLETED] != null) {
      Script.callMethod(callbackProcs[CB_SCAN_COMPLETED], "call" , new Object[]{path, uri});
    }
  }

}
