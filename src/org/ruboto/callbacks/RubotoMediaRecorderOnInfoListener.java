package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoMediaRecorderOnInfoListener implements android.media.MediaRecorder.OnInfoListener {
  private Ruby __ruby__;

  public static final int CB_INFO = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onInfo(android.media.MediaRecorder mr, int what, int extra) {
    if (callbackProcs[CB_INFO] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_INFO], "call" , JavaUtil.convertJavaToRuby(getRuby(), mr), JavaUtil.convertJavaToRuby(getRuby(), what), JavaUtil.convertJavaToRuby(getRuby(), extra));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
