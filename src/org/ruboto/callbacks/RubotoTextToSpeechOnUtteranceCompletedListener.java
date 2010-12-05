package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoTextToSpeechOnUtteranceCompletedListener implements android.speech.tts.TextToSpeech.OnUtteranceCompletedListener {
  private Ruby __ruby__;

  public static final int CB_UTTERANCE_COMPLETED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onUtteranceCompleted(java.lang.String utteranceId) {
    if (callbackProcs[CB_UTTERANCE_COMPLETED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_UTTERANCE_COMPLETED], "call" , JavaUtil.convertJavaToRuby(getRuby(), utteranceId));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
