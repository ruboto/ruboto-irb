package org.ruboto;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;
import java.io.IOException;
import android.app.ProgressDialog;

public abstract class RubotoBroadcastReceiver extends android.content.BroadcastReceiver {
  private Ruby __ruby__;
  private String scriptName;
  private String remoteVariable = "";
  public Object[] args;


  private IRubyObject[] callbackProcs = new IRubyObject[0];

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();

    if (__ruby__ == null) {
      Script.setUpJRuby(null);
      __ruby__ = Script.getRuby();
    }

    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public RubotoBroadcastReceiver setRemoteVariable(String var) {
    remoteVariable = ((var == null) ? "" : (var + "."));
    return this;
  }

  public void setScriptName(String name){
    scriptName = name;
  }

  /****************************************************************************************
   * 
   *  Activity Lifecycle: onCreate
   */
	
  @Override
  public void onReceive(android.content.Context arg0, android.content.Intent arg1) {
    args = new Object[2];
    args[0] = arg0;
    args[1] = arg1;

    getRuby();

    Script.defineGlobalVariable("$broadcast_receiver", this);
    Script.defineGlobalVariable("$broadcast_context", arg0);
    Script.defineGlobalVariable("$broadcast_intent", arg1);

    try {
      new Script(scriptName).execute();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  /****************************************************************************************
   * 
   *  Generated Methods
   */


}	


