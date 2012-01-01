package org.ruboto;

import org.ruboto.Script;
import java.io.IOException;
import android.app.ProgressDialog;

public class RubotoService extends android.app.Service {
  private String scriptName;
  private String remoteVariable = "";
  public Object[] args;

  public static final int CB_BIND = 0;
  public static final int CB_CONFIGURATION_CHANGED = 1;
  public static final int CB_DESTROY = 2;
  public static final int CB_LOW_MEMORY = 3;
  public static final int CB_REBIND = 4;
  public static final int CB_UNBIND = 5;
  public static final int CB_START_COMMAND = 6;

  private Object[] callbackProcs = new Object[7];

  public void setCallbackProc(int id, Object obj) {
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

    if (Script.setUpJRuby(this)) {
        Script.defineGlobalVariable("$context", this);
        Script.defineGlobalVariable("$service", this);

        try {
            if (scriptName != null) {
                new Script(scriptName).execute();
            } else {
                Script.execute("$service.initialize_ruboto");
                Script.execute("$service.on_create");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    } else {
      // FIXME(uwe):  What to do if the Ruboto Core plarform cannot be found?
    }
  }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public android.os.IBinder onBind(android.content.Intent intent) {
    if (callbackProcs[CB_BIND] != null) {
      return (android.os.IBinder) Script.callMethod(callbackProcs[CB_BIND], "call" , intent, android.os.IBinder.class);
    } else {
      return null;
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
      super.onConfigurationChanged(newConfig);
      Script.callMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
    } else {
      super.onConfigurationChanged(newConfig);
    }
  }

  public void onDestroy() {
    if (callbackProcs[CB_DESTROY] != null) {
      super.onDestroy();
      Script.callMethod(callbackProcs[CB_DESTROY], "call" );
    } else {
      super.onDestroy();
    }
  }

  public void onLowMemory() {
    if (callbackProcs[CB_LOW_MEMORY] != null) {
      super.onLowMemory();
      Script.callMethod(callbackProcs[CB_LOW_MEMORY], "call" );
    } else {
      super.onLowMemory();
    }
  }

  public void onRebind(android.content.Intent intent) {
    if (callbackProcs[CB_REBIND] != null) {
      super.onRebind(intent);
      Script.callMethod(callbackProcs[CB_REBIND], "call" , intent);
    } else {
      super.onRebind(intent);
    }
  }

  public boolean onUnbind(android.content.Intent intent) {
    if (callbackProcs[CB_UNBIND] != null) {
      super.onUnbind(intent);
      return (Boolean) Script.callMethod(callbackProcs[CB_UNBIND], "call" , intent, Boolean.class);
    } else {
      return super.onUnbind(intent);
    }
  }

  public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    if (callbackProcs[CB_START_COMMAND] != null) {
      super.onStartCommand(intent, flags, startId);
      return (Integer) Script.callMethod(callbackProcs[CB_START_COMMAND], "call" , new Object[]{intent, flags, startId}, Integer.class);
    } else {
      return super.onStartCommand(intent, flags, startId);
    }
  }

}


