package org.ruboto;

import org.ruboto.Script;
import org.ruboto.ScriptLoader;
import java.io.IOException;

public class RubotoService extends android.app.Service implements org.ruboto.RubotoComponent {
  public static final int CB_BIND = 0;
  public static final int CB_CONFIGURATION_CHANGED = 1;
  public static final int CB_DESTROY = 2;
  public static final int CB_LOW_MEMORY = 3;
  public static final int CB_REBIND = 4;
  public static final int CB_UNBIND = 5;
  public static final int CB_START_COMMAND = 6;

    private final ScriptInfo scriptInfo = new ScriptInfo(7);

    public ScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    /****************************************************************************************
     *
     *  Service Lifecycle: onCreate
     */

    // FIXME(uwe):  Only used for block based primary activities.  Remove if we remove support for such.
	public void onCreateSuper() {
	    super.onCreate();
	}

    @Override
    public void onCreate() {
        if (ScriptLoader.isCalledFromJRuby()) {
            super.onCreate();
            return;
        }
	    System.out.println("RubotoService.onCreate()");

        if (JRubyAdapter.setUpJRuby(this)) {
            // TODO(uwe):  Only needed for non-class-based definitions
            // Can be removed if we stop supporting non-class-based definitions
    	    JRubyAdapter.defineGlobalVariable("$context", this);
    	    JRubyAdapter.defineGlobalVariable("$service", this);
    	    // TODO end

            ScriptLoader.loadScript(this);
        } else {
            // FIXME(uwe):  What to do if the Ruboto Core platform cannot be found?
        }
    }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public android.os.IBinder onBind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) return null;
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_BIND] != null) {
        return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getCallbackProcs()[CB_BIND], "call" , intent);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_bind}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (android.os.IBinder) JRubyAdapter.runScriptlet("$ruby_instance.on_bind($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "on_bind", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBind}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (android.os.IBinder) JRubyAdapter.runScriptlet("$ruby_instance.onBind($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, scriptInfo.getRubyInstance(), "onBind", intent);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return null;
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onBind");
      return null;
    }
  }

  public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onConfigurationChanged(newConfig); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_CONFIGURATION_CHANGED] != null) {
        super.onConfigurationChanged(newConfig);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_CONFIGURATION_CHANGED], "call" , newConfig);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_configuration_changed}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_newConfig", newConfig);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_configuration_changed($arg_newConfig)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_configuration_changed", newConfig);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onConfigurationChanged}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_newConfig", newConfig);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onConfigurationChanged($arg_newConfig)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onConfigurationChanged", newConfig);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onConfigurationChanged(newConfig); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onConfigurationChanged");
      {super.onConfigurationChanged(newConfig); return;}
    }
  }

  public void onDestroy() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onDestroy(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_DESTROY] != null) {
        super.onDestroy();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_DESTROY], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_destroy}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_destroy()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_destroy");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDestroy}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onDestroy()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onDestroy");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onDestroy(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onDestroy");
      {super.onDestroy(); return;}
    }
  }

  public void onLowMemory() {
    if (ScriptLoader.isCalledFromJRuby()) {super.onLowMemory(); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_LOW_MEMORY] != null) {
        super.onLowMemory();
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_LOW_MEMORY], "call" );
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_low_memory}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_low_memory()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_low_memory");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onLowMemory}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onLowMemory()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onLowMemory");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onLowMemory(); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onLowMemory");
      {super.onLowMemory(); return;}
    }
  }

  public void onRebind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) {super.onRebind(intent); return;}
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_REBIND] != null) {
        super.onRebind(intent);
        JRubyAdapter.runRubyMethod(scriptInfo.getCallbackProcs()[CB_REBIND], "call" , intent);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_rebind}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            JRubyAdapter.runScriptlet("$ruby_instance.on_rebind($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "on_rebind", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRebind}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              JRubyAdapter.runScriptlet("$ruby_instance.onRebind($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(scriptInfo.getRubyInstance(), "onRebind", intent);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            {super.onRebind(intent); return;}
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onRebind");
      {super.onRebind(intent); return;}
    }
  }

  public boolean onUnbind(android.content.Intent intent) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onUnbind(intent);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_UNBIND] != null) {
        super.onUnbind(intent);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getCallbackProcs()[CB_UNBIND], "call" , intent);
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_unbind}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_unbind($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "on_unbind", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUnbind}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onUnbind($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, scriptInfo.getRubyInstance(), "onUnbind", intent);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onUnbind(intent);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onUnbind");
      return super.onUnbind(intent);
    }
  }

  public int onStartCommand(android.content.Intent intent, int flags, int startId) {
    if (ScriptLoader.isCalledFromJRuby()) return super.onStartCommand(intent, flags, startId);
    if (JRubyAdapter.isInitialized()) {
      if (scriptInfo.getCallbackProcs() != null && scriptInfo.getCallbackProcs()[CB_START_COMMAND] != null) {
        super.onStartCommand(intent, flags, startId);
        return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getCallbackProcs()[CB_START_COMMAND], "call" , new Object[]{intent, flags, startId});
      } else {
        String rubyClassName = scriptInfo.getRubyClassName();
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start_command}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$arg_flags", flags);
            JRubyAdapter.put("$arg_startId", startId);
            JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
            return (Integer) ((Number)JRubyAdapter.runScriptlet("$ruby_instance.on_start_command($arg_intent, $arg_flags, $arg_startId)")).intValue();
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "on_start_command", new Object[]{intent, flags, startId});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStartCommand}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$arg_flags", flags);
              JRubyAdapter.put("$arg_startId", startId);
              JRubyAdapter.put("$ruby_instance", scriptInfo.getRubyInstance());
              return (Integer) ((Number)JRubyAdapter.runScriptlet("$ruby_instance.onStartCommand($arg_intent, $arg_flags, $arg_startId)")).intValue();
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Integer) JRubyAdapter.runRubyMethod(Integer.class, scriptInfo.getRubyInstance(), "onStartCommand", new Object[]{intent, flags, startId});
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            return super.onStartCommand(intent, flags, startId);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onStartCommand");
      return super.onStartCommand(intent, flags, startId);
    }
  }

}
