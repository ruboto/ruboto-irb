package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoMediaPlayerOnBufferingUpdateListener implements android.media.MediaPlayer.OnBufferingUpdateListener {
  private Ruby __ruby__;

  public static final int CB_BUFFERING_UPDATE = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onBufferingUpdate(android.media.MediaPlayer mp, int percent) {
    if (callbackProcs[CB_BUFFERING_UPDATE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_BUFFERING_UPDATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), mp), JavaUtil.convertJavaToRuby(getRuby(), percent));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
