package org.ruboto.callbacks;

import org.ruboto.Script;

public class RubotoOnJetEventListener implements android.media.JetPlayer.OnJetEventListener {

  public static final int CB_JET_EVENT = 0;
  public static final int CB_JET_NUM_QUEUED_SEGMENT_UPDATE = 1;
  public static final int CB_JET_PAUSE_UPDATE = 2;
  public static final int CB_JET_USER_ID_UPDATE = 3;

    private Object[] callbackProcs = new Object[4];



  public void setCallbackProc(int id, Object obj) {
    callbackProcs[id] = obj;
  }
	
  public void onJetEvent(android.media.JetPlayer player, short segment, byte track, byte channel, byte controller, byte value) {
    if (callbackProcs != null && callbackProcs[CB_JET_EVENT] != null) {
      Script.callMethod(callbackProcs[CB_JET_EVENT], "call" , new Object[]{player, segment, track, channel, controller, value});
    }
  }

  public void onJetNumQueuedSegmentUpdate(android.media.JetPlayer player, int nbSegments) {
    if (callbackProcs != null && callbackProcs[CB_JET_NUM_QUEUED_SEGMENT_UPDATE] != null) {
      Script.callMethod(callbackProcs[CB_JET_NUM_QUEUED_SEGMENT_UPDATE], "call" , new Object[]{player, nbSegments});
    }
  }

  public void onJetPauseUpdate(android.media.JetPlayer player, int paused) {
    if (callbackProcs != null && callbackProcs[CB_JET_PAUSE_UPDATE] != null) {
      Script.callMethod(callbackProcs[CB_JET_PAUSE_UPDATE], "call" , new Object[]{player, paused});
    }
  }

  public void onJetUserIdUpdate(android.media.JetPlayer player, int userId, int repeatCount) {
    if (callbackProcs != null && callbackProcs[CB_JET_USER_ID_UPDATE] != null) {
      Script.callMethod(callbackProcs[CB_JET_USER_ID_UPDATE], "call" , new Object[]{player, userId, repeatCount});
    }
  }

}
