package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnScanCompletedListener implements android.media.MediaScannerConnection.OnScanCompletedListener {
  private Ruby __ruby__;

  public static final int CB_SCAN_COMPLETED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
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
