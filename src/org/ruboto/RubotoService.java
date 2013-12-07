package org.ruboto;

import org.ruboto.Script;
import org.ruboto.ScriptLoader;
import java.io.IOException;

public class RubotoService extends android.app.Service implements org.ruboto.RubotoComponent {
    private final ScriptInfo scriptInfo = new ScriptInfo();

    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    /****************************************************************************************
     *
     *  Service Lifecycle: onCreate
     */
    @Override
    public void onCreate() {
      System.out.println("RubotoService onCreate(): " + getClass().getName());

      if (ScriptLoader.isCalledFromJRuby()) {
        super.onCreate();
        return;
      }

      if (JRubyAdapter.isInitialized() && scriptInfo.isReadyToLoad()) {
  	    ScriptLoader.loadScript(this);
      } else {
        super.onCreate();
      }
    }

  // FIXME(uwe):  Revert to generate these methods.
  @Override
  public int onStartCommand(android.content.Intent intent, int flags, int startId) {
	  if (ScriptLoader.isCalledFromJRuby()) return super.onStartCommand(intent, flags, startId);

    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onStartCommand");
      return super.onStartCommand(intent, flags, startId);
    }
	
    if (JRubyAdapter.isInitialized() && !scriptInfo.isLoaded()) {
      scriptInfo.setFromIntent(intent);
 	    ScriptLoader.loadScript(this);
    }
	  
	  String rubyClassName = scriptInfo.getRubyClassName();
	  if (rubyClassName == null) return super.onStartCommand(intent, flags, startId);
	  if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start_command}")) {
        return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "on_start_command", new Object[]{intent, flags, startId});
      } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStartCommand}")) {
        return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "onStartCommand", new Object[]{intent, flags, startId});
      } else {
        return super.onStartCommand(intent, flags, startId);
      }
    }
  }

  // FIXME(uwe):  Revert to generate these methods.
  @Override
  public android.os.IBinder onBind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) return null;
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onBind");
      return null;
    }

    if (JRubyAdapter.isInitialized() && !scriptInfo.isLoaded()) {
      scriptInfo.setFromIntent(intent);
      ScriptLoader.loadScript(this);
    }
      
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return null;
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_bind}")) {
      return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "on_bind", intent);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBind}")) {
        return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "onBind", intent);
      } else {
        return null;
      }
    }
  }

    public void onDestroy() {
        if (ScriptLoader.isCalledFromJRuby()) {
            super.onDestroy();
            return;
        }
        if (!JRubyAdapter.isInitialized()) {
            Log.i("Method called before JRuby runtime was initialized: RubotoActivity#onDestroy");
            super.onDestroy();
            return;
        }
        String rubyClassName = scriptInfo.getRubyClassName();
        if (rubyClassName == null) {
            super.onDestroy();
            return;
        }
        ScriptLoader.callOnDestroy(this);
    }


  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onConfigurationChanged(newConfig); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onConfigurationChanged");
      {super.onConfigurationChanged(newConfig); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onConfigurationChanged(newConfig); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onConfigurationChanged}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onConfigurationChanged", newConfig);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_configuration_changed}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_configuration_changed", newConfig);
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_configuration_changed}")) {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_configuration_changed", newConfig);
        } else {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onConfigurationChanged", newConfig);
        }
      }
    }
  }

  public void onLowMemory() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onLowMemory(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onLowMemory");
      {super.onLowMemory(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onLowMemory(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onLowMemory}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onLowMemory");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_low_memory}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_low_memory");
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_low_memory}")) {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_low_memory");
        } else {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onLowMemory");
        }
      }
    }
  }

  public void onRebind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onRebind(intent); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onRebind");
      {super.onRebind(intent); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onRebind(intent); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRebind}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRebind", intent);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_rebind}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_rebind", intent);
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_rebind}")) {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_rebind", intent);
        } else {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRebind", intent);
        }
      }
    }
  }

  public boolean onUnbind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onUnbind(intent);
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onUnbind");
      return super.onUnbind(intent);
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onUnbind(intent);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUnbind}")) {
      return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onUnbind", intent);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_unbind}")) {
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_unbind", intent);
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_unbind}")) {
          return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_unbind", intent);
        } else {
          return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onUnbind", intent);
        }
      }
    }
  }

}
