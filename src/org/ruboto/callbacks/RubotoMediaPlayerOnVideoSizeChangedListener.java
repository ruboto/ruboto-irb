package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoMediaPlayerOnVideoSizeChangedListener implements android.media.MediaPlayer.OnVideoSizeChangedListener {
  private Ruby __ruby__;

  public static final int CB_VIDEO_SIZE_CHANGED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onVideoSizeChanged(android.media.MediaPlayer mp, int width, int height) {
    if (callbackProcs[CB_VIDEO_SIZE_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_VIDEO_SIZE_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), mp), JavaUtil.convertJavaToRuby(getRuby(), width), JavaUtil.convertJavaToRuby(getRuby(), height));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
