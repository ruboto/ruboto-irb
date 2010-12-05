package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoTextToSpeechOnInitListener implements android.speech.tts.TextToSpeech.OnInitListener {
  private Ruby __ruby__;

  public static final int CB_INIT = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onInit(int status) {
    if (callbackProcs[CB_INIT] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_INIT], "call" , JavaUtil.convertJavaToRuby(getRuby(), status));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
