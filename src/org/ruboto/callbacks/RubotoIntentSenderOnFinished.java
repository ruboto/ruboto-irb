package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoIntentSenderOnFinished implements android.content.IntentSender.OnFinished {
  private Ruby __ruby__;

  public static final int CB_SEND_FINISHED = 0;
  private IRubyObject[] callbackProcs = new IRubyObject[1];



  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onSendFinished(android.content.IntentSender IntentSender, android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras) {
    if (callbackProcs[CB_SEND_FINISHED] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), IntentSender), JavaUtil.convertJavaToRuby(getRuby(), intent), JavaUtil.convertJavaToRuby(getRuby(), resultCode), JavaUtil.convertJavaToRuby(getRuby(), resultData), JavaUtil.convertJavaToRuby(getRuby(), resultExtras)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SEND_FINISHED], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
