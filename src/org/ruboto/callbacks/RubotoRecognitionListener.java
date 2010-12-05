package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoRecognitionListener implements android.speech.RecognitionListener {
  private Ruby __ruby__;

  public static final int CB_BEGINNING_OF_SPEECH = 0;
  public static final int CB_BUFFER_RECEIVED = 1;
  public static final int CB_END_OF_SPEECH = 2;
  public static final int CB_ERROR = 3;
  public static final int CB_EVENT = 4;
  public static final int CB_PARTIAL_RESULTS = 5;
  public static final int CB_READY_FOR_SPEECH = 6;
  public static final int CB_RESULTS = 7;
  public static final int CB_RMS_CHANGED = 8;
  private IRubyObject[] callbackProcs = new IRubyObject[9];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onBeginningOfSpeech() {
    if (callbackProcs[CB_BEGINNING_OF_SPEECH] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_BEGINNING_OF_SPEECH], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onBufferReceived(byte[] buffer) {
    if (callbackProcs[CB_BUFFER_RECEIVED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_BUFFER_RECEIVED], "call" , JavaUtil.convertJavaToRuby(getRuby(), buffer));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onEndOfSpeech() {
    if (callbackProcs[CB_END_OF_SPEECH] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_END_OF_SPEECH], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onError(int error) {
    if (callbackProcs[CB_ERROR] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_ERROR], "call" , JavaUtil.convertJavaToRuby(getRuby(), error));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onEvent(int eventType, android.os.Bundle params) {
    if (callbackProcs[CB_EVENT] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_EVENT], "call" , JavaUtil.convertJavaToRuby(getRuby(), eventType), JavaUtil.convertJavaToRuby(getRuby(), params));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onPartialResults(android.os.Bundle partialResults) {
    if (callbackProcs[CB_PARTIAL_RESULTS] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PARTIAL_RESULTS], "call" , JavaUtil.convertJavaToRuby(getRuby(), partialResults));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onReadyForSpeech(android.os.Bundle params) {
    if (callbackProcs[CB_READY_FOR_SPEECH] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_READY_FOR_SPEECH], "call" , JavaUtil.convertJavaToRuby(getRuby(), params));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onResults(android.os.Bundle results) {
    if (callbackProcs[CB_RESULTS] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_RESULTS], "call" , JavaUtil.convertJavaToRuby(getRuby(), results));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }

  public void onRmsChanged(float rmsdB) {
    if (callbackProcs[CB_RMS_CHANGED] != null) {
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_RMS_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), rmsdB));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
