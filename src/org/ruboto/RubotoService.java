package org.ruboto;

import org.ruboto.Script;
import java.io.IOException;

public class RubotoService extends android.app.Service {
  private String scriptName;
  public Object[] args;
  private Object rubyInstance;

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
	
  public void setScriptName(String name){
    scriptName = name;
  }

  /****************************************************************************************
   * 
   *  Activity Lifecycle: onCreate
   */
	
  @Override
  public void onCreate() {
	System.out.println("RubotoService.onCreate()");
    args = new Object[0];

    super.onCreate();

    if (JRubyAdapter.setUpJRuby(this)) {
        // TODO(uwe):  Only needed for non-class-based definitions
        // Can be removed if we stop supporting non-class-based definitions
    	JRubyAdapter.defineGlobalVariable("$context", this);
    	JRubyAdapter.defineGlobalVariable("$service", this);
    	// TODO end

        try {
            if (scriptName != null) {
                String rubyClassName = Script.toCamelCase(scriptName);
                System.out.println("Looking for Ruby class: " + rubyClassName);
                Object rubyClass = JRubyAdapter.get(rubyClassName);
                if (rubyClass == null) {
                    System.out.println("Loading script: " + scriptName);
                    JRubyAdapter.exec(new Script(scriptName).getContents());
                    rubyClass = JRubyAdapter.get(rubyClassName);
                }
                if (rubyClass != null) {
                    System.out.println("Instanciating Ruby class: " + rubyClassName);
                    rubyInstance = JRubyAdapter.callMethod(rubyClass, "new", this, Object.class);
                    JRubyAdapter.callMethod(rubyInstance, "on_create");
                }
            } else {
            	JRubyAdapter.execute("$service.initialize_ruboto");
            	JRubyAdapter.execute("$service.on_create");
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
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_bind"}, Boolean.class)) {
      return (android.os.IBinder) JRubyAdapter.callMethod(rubyInstance, "on_bind" , intent, android.os.IBinder.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onBind"}, Boolean.class)) {
        return (android.os.IBinder) JRubyAdapter.callMethod(rubyInstance, "onBind" , intent, android.os.IBinder.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_BIND] != null) {
          return (android.os.IBinder) JRubyAdapter.callMethod(callbackProcs[CB_BIND], "call" , intent, android.os.IBinder.class);
        } else {
          return null;
        }
      }
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_configuration_changed"}, Boolean.class)) {
      super.onConfigurationChanged(newConfig);
      JRubyAdapter.callMethod(rubyInstance, "on_configuration_changed" , newConfig);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onConfigurationChanged"}, Boolean.class)) {
        super.onConfigurationChanged(newConfig);
        JRubyAdapter.callMethod(rubyInstance, "onConfigurationChanged" , newConfig);
      } else {
        if (callbackProcs != null && callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
          super.onConfigurationChanged(newConfig);
          JRubyAdapter.callMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
        } else {
          super.onConfigurationChanged(newConfig);
        }
      }
    }
  }

  public void onDestroy() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_destroy"}, Boolean.class)) {
      super.onDestroy();
      JRubyAdapter.callMethod(rubyInstance, "on_destroy" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onDestroy"}, Boolean.class)) {
        super.onDestroy();
        JRubyAdapter.callMethod(rubyInstance, "onDestroy" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_DESTROY] != null) {
          super.onDestroy();
          JRubyAdapter.callMethod(callbackProcs[CB_DESTROY], "call" );
        } else {
          super.onDestroy();
        }
      }
    }
  }

  public void onLowMemory() {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_low_memory"}, Boolean.class)) {
      super.onLowMemory();
      JRubyAdapter.callMethod(rubyInstance, "on_low_memory" );
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onLowMemory"}, Boolean.class)) {
        super.onLowMemory();
        JRubyAdapter.callMethod(rubyInstance, "onLowMemory" );
      } else {
        if (callbackProcs != null && callbackProcs[CB_LOW_MEMORY] != null) {
          super.onLowMemory();
          JRubyAdapter.callMethod(callbackProcs[CB_LOW_MEMORY], "call" );
        } else {
          super.onLowMemory();
        }
      }
    }
  }

  public void onRebind(android.content.Intent intent) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_rebind"}, Boolean.class)) {
      super.onRebind(intent);
      JRubyAdapter.callMethod(rubyInstance, "on_rebind" , intent);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onRebind"}, Boolean.class)) {
        super.onRebind(intent);
        JRubyAdapter.callMethod(rubyInstance, "onRebind" , intent);
      } else {
        if (callbackProcs != null && callbackProcs[CB_REBIND] != null) {
          super.onRebind(intent);
          JRubyAdapter.callMethod(callbackProcs[CB_REBIND], "call" , intent);
        } else {
          super.onRebind(intent);
        }
      }
    }
  }

  public boolean onUnbind(android.content.Intent intent) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_unbind"}, Boolean.class)) {
      super.onUnbind(intent);
      return (Boolean) JRubyAdapter.callMethod(rubyInstance, "on_unbind" , intent, Boolean.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onUnbind"}, Boolean.class)) {
        super.onUnbind(intent);
        return (Boolean) JRubyAdapter.callMethod(rubyInstance, "onUnbind" , intent, Boolean.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_UNBIND] != null) {
          super.onUnbind(intent);
          return (Boolean) JRubyAdapter.callMethod(callbackProcs[CB_UNBIND], "call" , intent, Boolean.class);
        } else {
          return super.onUnbind(intent);
        }
      }
    }
  }

  public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"on_start_command"}, Boolean.class)) {
      super.onStartCommand(intent, flags, startId);
      return (Integer) JRubyAdapter.callMethod(rubyInstance, "on_start_command" , new Object[]{intent, flags, startId}, Integer.class);
    } else {
      if (rubyInstance != null && JRubyAdapter.callMethod(rubyInstance, "respond_to?" , new Object[]{"onStartCommand"}, Boolean.class)) {
        super.onStartCommand(intent, flags, startId);
        return (Integer) JRubyAdapter.callMethod(rubyInstance, "onStartCommand" , new Object[]{intent, flags, startId}, Integer.class);
      } else {
        if (callbackProcs != null && callbackProcs[CB_START_COMMAND] != null) {
          super.onStartCommand(intent, flags, startId);
          return (Integer) JRubyAdapter.callMethod(callbackProcs[CB_START_COMMAND], "call" , new Object[]{intent, flags, startId}, Integer.class);
        } else {
          return super.onStartCommand(intent, flags, startId);
        }
      }
    }
  }

}


