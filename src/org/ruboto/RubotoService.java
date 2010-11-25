package org.ruboto;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;
import java.io.IOException;
import android.app.ProgressDialog;

public abstract class RubotoService extends android.app.Service {
  private Ruby __ruby__;
  private String scriptName;
  private String remoteVariable = "";
  public Object[] args;

  public static final int CB_BIND = 0;
  public static final int CB_CONFIGURATION_CHANGED = 1;
  public static final int CB_DESTROY = 2;
  public static final int CB_LOW_MEMORY = 3;
  public static final int CB_REBIND = 4;
  public static final int CB_START = 5;
  public static final int CB_UNBIND = 6;
  public static final int CB_START_COMMAND = 7;
  private IRubyObject[] callbackProcs = new IRubyObject[8];

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
	
  public RubotoService setRemoteVariable(String var) {
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
  public void onCreate() {
    args = new Object[0];

    super.onCreate();

    getRuby();

    Script.defineGlobalVariable("$service", this);

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

  public android.os.IBinder onBind(android.content.Intent intent) {
    if (callbackProcs[CB_BIND] != null) {
      try {
        return (android.os.IBinder)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_BIND], "call" , JavaUtil.convertJavaToRuby(getRuby(), intent)).toJava(android.os.IBinder.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
      super.onConfigurationChanged(newConfig);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_CONFIGURATION_CHANGED], "call" , JavaUtil.convertJavaToRuby(getRuby(), newConfig));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onConfigurationChanged(newConfig);
    }
  }

  public void onDestroy() {
    if (callbackProcs[CB_DESTROY] != null) {
      super.onDestroy();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DESTROY], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onDestroy();
    }
  }

  public void onLowMemory() {
    if (callbackProcs[CB_LOW_MEMORY] != null) {
      super.onLowMemory();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_LOW_MEMORY], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onLowMemory();
    }
  }

  public void onRebind(android.content.Intent intent) {
    if (callbackProcs[CB_REBIND] != null) {
      super.onRebind(intent);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_REBIND], "call" , JavaUtil.convertJavaToRuby(getRuby(), intent));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onRebind(intent);
    }
  }

  public void onStart(android.content.Intent intent, int startId) {
    if (callbackProcs[CB_START] != null) {
      super.onStart(intent, startId);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_START], "call" , JavaUtil.convertJavaToRuby(getRuby(), intent), JavaUtil.convertJavaToRuby(getRuby(), startId));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onStart(intent, startId);
    }
  }

  public boolean onUnbind(android.content.Intent intent) {
    if (callbackProcs[CB_UNBIND] != null) {
      super.onUnbind(intent);
      try {
        return (Boolean)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_UNBIND], "call" , JavaUtil.convertJavaToRuby(getRuby(), intent)).toJava(boolean.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return false;
      }
    } else {
      return super.onUnbind(intent);
    }
  }

  public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    if (callbackProcs[CB_START_COMMAND] != null) {
      try {
        return (Integer)RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_START_COMMAND], "call" , JavaUtil.convertJavaToRuby(getRuby(), intent), JavaUtil.convertJavaToRuby(getRuby(), flags), JavaUtil.convertJavaToRuby(getRuby(), startId)).toJava(int.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return 0;
      }
    } else {
      return 0;
    }
  }
}	


