package org.ruboto.callbacks;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class RubotoPendingIntentOnFinished implements android.app.PendingIntent.OnFinished {
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
	
  public void onSendFinished(android.app.PendingIntent pendingIntent, android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras) {
    if (callbackProcs[CB_SEND_FINISHED] != null) {
      try {
        IRubyObject[] args = {JavaUtil.convertJavaToRuby(getRuby(), pendingIntent), JavaUtil.convertJavaToRuby(getRuby(), intent), JavaUtil.convertJavaToRuby(getRuby(), resultCode), JavaUtil.convertJavaToRuby(getRuby(), resultData), JavaUtil.convertJavaToRuby(getRuby(), resultExtras)};
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_SEND_FINISHED], "call" , args);
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    }
  }
}
