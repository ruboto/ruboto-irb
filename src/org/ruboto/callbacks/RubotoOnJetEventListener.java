package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnJetEventListener implements android.media.JetPlayer.OnJetEventListener {
  private Ruby __ruby__;

  public static final int CB_JET_EVENT = 0;
  public static final int CB_JET_NUM_QUEUED_SEGMENT_UPDATE = 1;
  public static final int CB_JET_PAUSE_UPDATE = 2;
  public static final int CB_JET_USER_ID_UPDATE = 3;
  private IRubyObject[] callbackProcs = new IRubyObject[4];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onJetEvent(android.media.JetPlayer player, short segment, byte track, byte channel, byte controller, byte value) {
    if (callbackProcs[CB_JET_EVENT] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), player), JavaUtil.convertJavaToRuby(getRuby(), segment), JavaUtil.convertJavaToRuby(getRuby(), track), JavaUtil.convertJavaToRuby(getRuby(), channel), JavaUtil.convertJavaToRuby(getRuby(), controller), JavaUtil.convertJavaToRuby(getRuby(), value)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_JET_EVENT], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onJetNumQueuedSegmentUpdate(android.media.JetPlayer player, int nbSegments) {
    if (callbackProcs[CB_JET_NUM_QUEUED_SEGMENT_UPDATE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_JET_NUM_QUEUED_SEGMENT_UPDATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), player), JavaUtil.convertJavaToRuby(getRuby(), nbSegments));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onJetPauseUpdate(android.media.JetPlayer player, int paused) {
    if (callbackProcs[CB_JET_PAUSE_UPDATE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_JET_PAUSE_UPDATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), player), JavaUtil.convertJavaToRuby(getRuby(), paused));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onJetUserIdUpdate(android.media.JetPlayer player, int userId, int repeatCount) {
    if (callbackProcs[CB_JET_USER_ID_UPDATE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_JET_USER_ID_UPDATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), player), JavaUtil.convertJavaToRuby(getRuby(), userId), JavaUtil.convertJavaToRuby(getRuby(), repeatCount));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
