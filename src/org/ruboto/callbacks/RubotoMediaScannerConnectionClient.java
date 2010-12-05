package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoMediaScannerConnectionClient implements android.media.MediaScannerConnection.MediaScannerConnectionClient {
  private Ruby __ruby__;

  public static final int CB_MEDIA_SCANNER_CONNECTED = 0;
  public static final int CB_SCAN_COMPLETED = 1;
  private IRubyObject[] callbackProcs = new IRubyObject[2];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onMediaScannerConnected() {
    if (callbackProcs[CB_MEDIA_SCANNER_CONNECTED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_MEDIA_SCANNER_CONNECTED], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onScanCompleted(java.lang.String path, android.net.Uri uri) {
    if (callbackProcs[CB_SCAN_COMPLETED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SCAN_COMPLETED], "call" , JavaUtil.convertJavaToRuby(getRuby(), path), JavaUtil.convertJavaToRuby(getRuby(), uri));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
