package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoOnAudioFocusChangeListener implements android.media.AudioManager.OnAudioFocusChangeListener {
  private Ruby __ruby__;

  public static final int CB_AUDIO_FOCUS_CHANGE = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onAudioFocusChange(int focusChange) {
    if (callbackProcs[CB_AUDIO_FOCUS_CHANGE] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_AUDIO_FOCUS_CHANGE], "call" , JavaUtil.convertJavaToRuby(getRuby(), focusChange));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
