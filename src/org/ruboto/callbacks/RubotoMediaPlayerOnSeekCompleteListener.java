package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoMediaPlayerOnSeekCompleteListener implements android.media.MediaPlayer.OnSeekCompleteListener {
  private Ruby __ruby__;

  public static final int CB_SEEK_COMPLETE = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onSeekComplete(android.media.MediaPlayer mp) {
    if (callbackProcs[CB_SEEK_COMPLETE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SEEK_COMPLETE], "call" , JavaUtil.convertJavaToRuby(getRuby(), mp));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
