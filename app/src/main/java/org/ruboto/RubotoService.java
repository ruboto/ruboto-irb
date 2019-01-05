package org.ruboto;

import org.ruboto.Script;
import org.ruboto.ScriptLoader;
import java.io.IOException;

public class RubotoService extends android.app.Service implements org.ruboto.RubotoComponent {
    /**
     * Called at the start of onCreate() to prepare the Activity.
     */
    private void preOnCreate() {
        if (!getClass().getSimpleName().equals("RubotoService")) {
          System.out.println("RubotoService onCreate(): " + getClass().getName());
          getScriptInfo().setRubyClassName(getClass().getSimpleName());
        }
    }

    private void preOnStartCommand(android.content.Intent intent) {
        if (getClass().getSimpleName().equals("RubotoService")) {
          System.out.println("RubotoService onStartCommand(): " + getClass().getName());
          scriptInfo.setFromIntent(intent);
        }
    }

    private void preOnBind(android.content.Intent intent) {
        if (getClass().getSimpleName().equals("RubotoService")) {
          System.out.println("RubotoService onBind(): " + getClass().getName());
          scriptInfo.setFromIntent(intent);
        }
    }

    private final ScriptInfo scriptInfo = new ScriptInfo();
    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    /****************************************************************************************
     *
     *  Generated Methods
     */

  public android.os.IBinder onBind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) return null;
    preOnBind(intent);
if (JRubyAdapter.isInitialized() && scriptInfo.isReadyToLoad()) {
        ScriptLoader.loadScript(this);
    } else {
        return null;
    }

    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return null;
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBind}")) {
      return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "onBind", intent);
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_bind}")) {
        return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "on_bind", intent);
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_bind}")) {
          return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "on_bind", intent);
        } else {
          return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "onBind", intent);
        }
      }
    }
  }

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

  public void onCreate() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onCreate(); return;}
    preOnCreate();
if (JRubyAdapter.isInitialized() && scriptInfo.isReadyToLoad()) {
        ScriptLoader.loadScript(this);
    } else {
        {super.onCreate(); return;}
    }

    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onCreate(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onCreate}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onCreate");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_create}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_create");
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_create}")) {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_create");
        } else {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onCreate");
        }
      }
    }
  }

  public void onDestroy() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onDestroy(); return;}
    if (!JRubyAdapter.isInitialized()) {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onDestroy");
      {super.onDestroy(); return;}
    }
    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) {super.onDestroy(); return;}
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDestroy}")) {
      JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDestroy");
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_destroy}")) {
        JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_destroy");
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_destroy}")) {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_destroy");
        } else {
          JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDestroy");
        }
      }
    }
    ScriptLoader.unloadScript(this);
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

  public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onStartCommand(intent, flags, startId);
    preOnStartCommand(intent);
if (JRubyAdapter.isInitialized() && scriptInfo.isReadyToLoad()) {
        ScriptLoader.loadScript(this);
    } else {
        return super.onStartCommand(intent, flags, startId);
    }

    String rubyClassName = scriptInfo.getRubyClassName();
    if (rubyClassName == null) return super.onStartCommand(intent, flags, startId);
    if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStartCommand}")) {
      return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "onStartCommand", new Object[]{intent, flags, startId});
    } else {
      if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start_command}")) {
        return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "on_start_command", new Object[]{intent, flags, startId});
      } else {
        if ((Boolean)JRubyAdapter.runScriptlet(rubyClassName + ".instance_methods(true).any?{|m| m.to_sym == :on_start_command}")) {
          return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "on_start_command", new Object[]{intent, flags, startId});
        } else {
          return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "onStartCommand", new Object[]{intent, flags, startId});
        }
      }
    }
  }

}
