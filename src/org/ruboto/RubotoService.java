package org.ruboto;

import org.ruboto.Script;
import java.io.IOException;

public class RubotoService extends android.app.Service {
  public static final int CB_BIND = 0;
  public static final int CB_CONFIGURATION_CHANGED = 1;
  public static final int CB_DESTROY = 2;
  public static final int CB_LOW_MEMORY = 3;
  public static final int CB_REBIND = 4;
  public static final int CB_UNBIND = 5;
  public static final int CB_START_COMMAND = 6;

    private String rubyClassName;
    private String scriptName;
    private Object rubyInstance;
    private Object[] callbackProcs = new Object[7];
    public Object[] args;

    public void setCallbackProc(int id, Object obj) {
      callbackProcs[id] = obj;
    }
	
    public void setScriptName(String name){
        scriptName = name;
    }

    /****************************************************************************************
     *
     *  Service Lifecycle: onCreate
     */

    @Override
    public void onCreate() {
        // Return if we are called from JRuby to avoid infinite recursion.
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(StackTraceElement e : stackTraceElements){
            if (e.getClassName().equals("java.lang.reflect.Method") && e.getMethodName().equals("invokeNative")) {
                return;
            }
            if (e.getClassName().equals("android.app.ActivityThread") && e.getMethodName().equals("handleCreateService")) {
                break;
            }
        }

	    System.out.println("RubotoService.onCreate()");
        args = new Object[0];

        super.onCreate();

        if (JRubyAdapter.setUpJRuby(this)) {
            rubyInstance = this;

            // TODO(uwe):  Only needed for non-class-based definitions
            // Can be removed if we stop supporting non-class-based definitions
    	    JRubyAdapter.defineGlobalVariable("$context", this);
    	    JRubyAdapter.defineGlobalVariable("$service", this);
    	    // TODO end

            try {
                if (scriptName != null) {
                    String rubyClassName = Script.toCamelCase(scriptName);
                    System.out.println("Looking for Ruby class: " + rubyClassName);
                    Object rubyClass = null;
                    String script = new Script(scriptName).getContents();
                    if (script.matches("(?s).*class " + rubyClassName + ".*")) {
                        if (!rubyClassName.equals(getClass().getSimpleName())) {
                            System.out.println("Script defines methods on meta class");
                            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
                            if (JRubyAdapter.isJRubyPreOneSeven() || JRubyAdapter.isRubyOneEight()) {
                                JRubyAdapter.put("$java_instance", this);
                                JRubyAdapter.put(rubyClassName, JRubyAdapter.runScriptlet("class << $java_instance; self; end"));
                            } else if (JRubyAdapter.isJRubyOneSeven() && JRubyAdapter.isRubyOneNine()) {
                                JRubyAdapter.put(rubyClassName, JRubyAdapter.runRubyMethod(this, "singleton_class"));
                            } else {
                                throw new RuntimeException("Unknown JRuby/Ruby version: " + JRubyAdapter.get("JRUBY_VERSION") + "/" + JRubyAdapter.get("RUBY_VERSION"));
                            }
                        }
                    } else {
                        rubyClass = JRubyAdapter.get(rubyClassName);
                    }
                    if (rubyClass == null) {
                        System.out.println("Loading script: " + scriptName);
                        if (script.matches("(?s).*class " + rubyClassName + ".*")) {
                            System.out.println("Script contains class definition");
                            if (rubyClassName.equals(getClass().getSimpleName())) {
                                System.out.println("Script has separate Java class");
                                JRubyAdapter.put(rubyClassName, JRubyAdapter.runScriptlet("Java::" + getClass().getName()));
                            }
                            // System.out.println("Set class: " + JRubyAdapter.get(rubyClassName));
                        }
                        JRubyAdapter.setScriptFilename(scriptName);
                        JRubyAdapter.runScriptlet(script);
                        rubyClass = JRubyAdapter.get(rubyClassName);
                    }
                    if (rubyClass != null) {
                        System.out.println("Call on_create on: " + this);
                        // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
                        if (JRubyAdapter.isJRubyPreOneSeven()) {
                            JRubyAdapter.put("$ruby_instance", this);
                            JRubyAdapter.runScriptlet("$ruby_instance.on_create");
                        } else if (JRubyAdapter.isJRubyOneSeven()) {
                            JRubyAdapter.runRubyMethod(this, "on_create");
                        } else {
                            throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
                        }
                    }
                } else {
                    // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
                    if (JRubyAdapter.isJRubyPreOneSeven()) {
            	        JRubyAdapter.runScriptlet("$service.initialize_ruboto");
            	        JRubyAdapter.runScriptlet("$service.on_create");
                    } else if (JRubyAdapter.isJRubyOneSeven()) {
            	        JRubyAdapter.runRubyMethod(this, "initialize_ruboto");
                        JRubyAdapter.runRubyMethod(this, "on_create", args[0]);
                    } else {
                        throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            	    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            // FIXME(uwe):  What to do if the Ruboto Core platform cannot be found?
        }
    }

  /****************************************************************************************
   * 
   *  Generated Methods
   */

  public android.os.IBinder onBind(android.content.Intent intent) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_BIND] != null) {
        return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, callbackProcs[CB_BIND], "call" , intent);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_bind}")) {
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (android.os.IBinder) JRubyAdapter.runScriptlet("$ruby_instance.on_bind($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, rubyInstance, "on_bind", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onBind}")) {
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (android.os.IBinder) JRubyAdapter.runScriptlet("$ruby_instance.onBind($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (android.os.IBinder) JRubyAdapter.runRubyMethod(android.os.IBinder.class, rubyInstance, "onBind", intent);
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
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_CONFIGURATION_CHANGED] != null) {
        super.onConfigurationChanged(newConfig);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_CONFIGURATION_CHANGED], "call" , newConfig);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_configuration_changed}")) {
          super.onConfigurationChanged(newConfig);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_newConfig", newConfig);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_configuration_changed($arg_newConfig)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_configuration_changed", newConfig);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onConfigurationChanged}")) {
            super.onConfigurationChanged(newConfig);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_newConfig", newConfig);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onConfigurationChanged($arg_newConfig)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onConfigurationChanged", newConfig);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onConfigurationChanged(newConfig);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onConfigurationChanged");
      super.onConfigurationChanged(newConfig);
    }
  }

  public void onDestroy() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_DESTROY] != null) {
        super.onDestroy();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_DESTROY], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_destroy}")) {
          super.onDestroy();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_destroy()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_destroy");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onDestroy}")) {
            super.onDestroy();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onDestroy()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onDestroy");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onDestroy();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onDestroy");
      super.onDestroy();
    }
  }

  public void onLowMemory() {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_LOW_MEMORY] != null) {
        super.onLowMemory();
        JRubyAdapter.runRubyMethod(callbackProcs[CB_LOW_MEMORY], "call" );
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_low_memory}")) {
          super.onLowMemory();
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_low_memory()");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_low_memory");
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onLowMemory}")) {
            super.onLowMemory();
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onLowMemory()");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onLowMemory");
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onLowMemory();
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onLowMemory");
      super.onLowMemory();
    }
  }

  public void onRebind(android.content.Intent intent) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_REBIND] != null) {
        super.onRebind(intent);
        JRubyAdapter.runRubyMethod(callbackProcs[CB_REBIND], "call" , intent);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_rebind}")) {
          super.onRebind(intent);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            JRubyAdapter.runScriptlet("$ruby_instance.on_rebind($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              JRubyAdapter.runRubyMethod(rubyInstance, "on_rebind", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onRebind}")) {
            super.onRebind(intent);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              JRubyAdapter.runScriptlet("$ruby_instance.onRebind($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                JRubyAdapter.runRubyMethod(rubyInstance, "onRebind", intent);
              } else {
                throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
              }
            }
          } else {
            super.onRebind(intent);
          }
        }
      }
    } else {
      Log.i("Method called before JRuby runtime was initialized: RubotoService#onRebind");
      super.onRebind(intent);
    }
  }

  public boolean onUnbind(android.content.Intent intent) {
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_UNBIND] != null) {
        super.onUnbind(intent);
        return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, callbackProcs[CB_UNBIND], "call" , intent);
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_unbind}")) {
          super.onUnbind(intent);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.on_unbind($arg_intent)");
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "on_unbind", intent);
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onUnbind}")) {
            super.onUnbind(intent);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Boolean) JRubyAdapter.runScriptlet("$ruby_instance.onUnbind($arg_intent)");
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Boolean) JRubyAdapter.runRubyMethod(Boolean.class, rubyInstance, "onUnbind", intent);
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
    if (JRubyAdapter.isInitialized()) {
      if (callbackProcs != null && callbackProcs[CB_START_COMMAND] != null) {
        super.onStartCommand(intent, flags, startId);
        return (Integer) JRubyAdapter.runRubyMethod(Integer.class, callbackProcs[CB_START_COMMAND], "call" , new Object[]{intent, flags, startId});
      } else {
        String rubyClassName = Script.toCamelCase(scriptName);
        if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :on_start_command}")) {
          super.onStartCommand(intent, flags, startId);
          // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
          if (JRubyAdapter.isJRubyPreOneSeven()) {
            JRubyAdapter.put("$arg_intent", intent);
            JRubyAdapter.put("$arg_flags", flags);
            JRubyAdapter.put("$arg_startId", startId);
            JRubyAdapter.put("$ruby_instance", rubyInstance);
            return (Integer) ((Number)JRubyAdapter.runScriptlet("$ruby_instance.on_start_command($arg_intent, $arg_flags, $arg_startId)")).intValue();
          } else {
            if (JRubyAdapter.isJRubyOneSeven()) {
              return (Integer) JRubyAdapter.runRubyMethod(Integer.class, rubyInstance, "on_start_command", new Object[]{intent, flags, startId});
            } else {
              throw new RuntimeException("Unknown JRuby version: " + JRubyAdapter.get("JRUBY_VERSION"));
            }
          }
        } else {
          if ((Boolean)JRubyAdapter.runScriptlet("defined?(" + rubyClassName + ") == 'constant' && " + rubyClassName + ".instance_methods(false).any?{|m| m.to_sym == :onStartCommand}")) {
            super.onStartCommand(intent, flags, startId);
            // FIXME(uwe): Simplify when we stop support for RubotoCore 0.4.7
            if (JRubyAdapter.isJRubyPreOneSeven()) {
              JRubyAdapter.put("$arg_intent", intent);
              JRubyAdapter.put("$arg_flags", flags);
              JRubyAdapter.put("$arg_startId", startId);
              JRubyAdapter.put("$ruby_instance", rubyInstance);
              return (Integer) ((Number)JRubyAdapter.runScriptlet("$ruby_instance.onStartCommand($arg_intent, $arg_flags, $arg_startId)")).intValue();
            } else {
              if (JRubyAdapter.isJRubyOneSeven()) {
                return (Integer) JRubyAdapter.runRubyMethod(Integer.class, rubyInstance, "onStartCommand", new Object[]{intent, flags, startId});
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
