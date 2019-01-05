package org.ruboto;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JRubyAdapter {
    private static org.jruby.embed.ScriptingContainer ruby;
    private static boolean isDebugBuild = false;
    private static PrintStream output = null;
    private static boolean initialized = false;
    private static String localContextScope = "SINGLETON"; // FIXME(uwe):  Why not CONCURRENT ?  Help needed!
    private static String localVariableBehavior = "TRANSIENT";

    public static Object get(String name) {
        try {
            Method getMethod = ruby.getClass().getMethod("get", String.class);
            return getMethod.invoke(ruby, name);
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }
    }

    public static String getScriptFilename() {
        return (String) callScriptingContainerMethod(String.class, "getScriptFilename");
    }

    public static Object runRubyMethod(Object receiver, String methodName, Object... args) {
        try {
            Method m = ruby.getClass().getMethod("runRubyMethod", Class.class, Object.class, String.class, Object[].class);
            return m.invoke(ruby, Object.class, receiver, methodName, args);
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            printStackTrace(ite);
            if (isDebugBuild) {
                throw new RuntimeException(ite);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T runRubyMethod(Class<T> returnType, Object receiver, String methodName, Object... args) {
        try {
            Method m = ruby.getClass().getMethod("runRubyMethod", Class.class, Object.class, String.class, Object[].class);
            return (T) m.invoke(ruby, returnType, receiver, methodName, args);
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            printStackTrace(ite);
        }
        return null;
    }

    public static boolean isDebugBuild() {
        return isDebugBuild;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void put(String name, Object object) {
        ruby.put(name, object);
    }

    public static Object runScriptlet(String code) {
        return ruby.runScriptlet(code);
    }

    public static boolean setUpJRuby(Context appContext) {
        return setUpJRuby(appContext, output == null ? System.out : output);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static synchronized boolean setUpJRuby(Context appContext, PrintStream out) {
        if (!initialized) {
            Log.d("Max memory: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "MB");
            // BEGIN Ruboto HeapAlloc
            @SuppressWarnings("unused")
            byte[] arrayForHeapAllocation = new byte[100 * 1024 * 1024];
            arrayForHeapAllocation = null;
            // END Ruboto HeapAlloc
            Log.d("Memory allocation OK");
            setDebugBuild(appContext);
            Log.d("Setting up JRuby runtime (" + (isDebugBuild ? "DEBUG" : "RELEASE") + ")");

            setSystemProperties(appContext);

            try {
                String jrubyVersion = org.jruby.runtime.Constants.VERSION;
                System.out.println("JRuby version: " + jrubyVersion);

                //////////////////////////////////
                //
                // Set jruby.home
                //
                // final String jrubyHome = "jar:" + apkName + "!/jruby.home";

                // Log.i("Setting JRUBY_HOME: " + jrubyHome);
                // This needs to be set before the ScriptingContainer is initialized
                // System.setProperty("jruby.home", jrubyHome);

                //////////////////////////////////
                //
                // Determine Output
                //

                if (out != null) {
                    output = out;
                }

                //////////////////////////////////
                //
                // Disable rubygems
                //
                org.jruby.RubyInstanceConfig config = new org.jruby.RubyInstanceConfig();
                config.setDisableGems(true);

                ClassLoader classLoader = JRubyAdapter.class.getClassLoader();
                config.setLoader(classLoader);

                if (output != null) {
                    config.setOutput(output);
                    config.setError(output);
                }

                System.out.println("Ruby version: " + config.getCompatVersion());

                // This will become the global runtime and be used by our ScriptingContainer
                org.jruby.Ruby.newInstance(config);

                //////////////////////////////////
                //
                // Create the ScriptingContainer
                //
                ruby = new org.jruby.embed.ScriptingContainer(LocalContextScope.SINGLETON, LocalVariableBehavior.TRANSIENT);

                // FIXME(uwe): Write tutorial on profiling.
                // container.getProvider().getRubyInstanceConfig().setProfilingMode(mode);

                Thread.currentThread().setContextClassLoader(classLoader);

                String scriptsDir = scriptsDirName(appContext);
                addLoadPath(scriptsDir);
                if (appContext.getFilesDir() != null) {
                    String defaultCurrentDir = appContext.getFilesDir().getPath();
                    Log.d("Setting JRuby current directory to " + defaultCurrentDir);
                    callScriptingContainerMethod(Void.class, "setCurrentDirectory", defaultCurrentDir);
                } else {
                    Log.e("Unable to find app files dir!");
                    if (new File(scriptsDir).exists()) {
                        Log.d("Changing JRuby current directory to " + scriptsDir);
                        callScriptingContainerMethod(Void.class, "setCurrentDirectory", scriptsDir);
                    }
                }

                put("$package_name", appContext.getPackageName());

                runScriptlet("::RUBOTO_JAVA_PROXIES = {}");

                // TODO(uwe):  Add a way to display startup progress.
                put("$application_context", appContext.getApplicationContext());
                runScriptlet("begin\n  require 'environment'\nrescue LoadError => e\n  puts e\nend");

                initialized = true;
            } catch (IllegalArgumentException e) {
                handleInitException(e);
            } catch (SecurityException e) {
                handleInitException(e);
            }
        }
        return initialized;
    }

    private static void setSystemProperties(Context appContext) {
        System.setProperty("jruby.backtrace.style", "normal"); // normal raw full mri
        System.setProperty("jruby.bytecode.version", "1.6");
        // BEGIN Ruboto RubyVersion
        System.setProperty("jruby.compat.version", "RUBY1_9"); // RUBY1_9 is the default in JRuby 1.7
        // END Ruboto RubyVersion
        // System.setProperty("jruby.compile.backend", "DALVIK");
        System.setProperty("jruby.compile.mode", "OFF"); // OFF OFFIR JITIR? FORCE FORCEIR
        System.setProperty("jruby.interfaces.useProxy", "true");
        System.setProperty("jruby.ir.passes", "LocalOptimizationPass,DeadCodeElimination");
        System.setProperty("jruby.management.enabled", "false");
        System.setProperty("jruby.native.enabled", "false");
        System.setProperty("jruby.objectspace.enabled", "false");
        System.setProperty("jruby.rewrite.java.trace", "true");
        System.setProperty("jruby.thread.pooling", "true");

        // Uncomment these to debug/profile Ruby source loading
        // Analyse the output: grep "LoadService:   <-" | cut -f5 -d- | cut -c2- | cut -f1 -dm | awk '{total = total + $1}END{print total}'
        // System.setProperty("jruby.debug.loadService", "true");
        // System.setProperty("jruby.debug.loadService.timing", "true");

        // Used to enable JRuby to generate proxy classes
        System.setProperty("jruby.ji.proxyClassFactory", "org.ruboto.DalvikProxyClassFactory");
        System.setProperty("jruby.ji.upper.case.package.name.allowed", "true");
        System.setProperty("jruby.class.cache.path", appContext.getDir("dex", 0).getAbsolutePath());
        System.setProperty("java.io.tmpdir", appContext.getCacheDir().getAbsolutePath());
        System.setProperty("sun.arch.data.model", "64");
    }

    public static void setScriptFilename(String name) {
        callScriptingContainerMethod(Void.class, "setScriptFilename", name);
    }

    public static Boolean addLoadPath(String scriptsDir) {
        if (new File(scriptsDir).exists()) {
            Log.i("Added directory to load path: " + scriptsDir);
            Script.addDir(scriptsDir);
            runScriptlet("$:.unshift '" + scriptsDir + "' ; $:.uniq!");
            return true;
        } else {
            Log.i("Extra scripts dir not present: " + scriptsDir);
            return false;
        }
    }

    // Private methods

    @SuppressWarnings("unchecked")
    private static <T> T callScriptingContainerMethod(Class<T> returnType, String methodName, Object... args) {
        Class<?>[] argClasses = new Class[args.length];
        for (int i = 0; i < argClasses.length; i++) {
            argClasses[i] = args[i].getClass();
        }
        try {
            Method method = ruby.getClass().getMethod(methodName, argClasses);
            T result = (T) method.invoke(ruby, args);
            return result;
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            printStackTrace(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void handleInitException(Exception e) {
        Log.e("Exception starting JRuby");
        Log.e(e.getMessage() != null ? e.getMessage() : e.getClass().getName());
        e.printStackTrace();
        ruby = null;
    }

    // FIXME(uwe):  Remove when we stop supporting Ruby 1.8
    @Deprecated
    public static boolean isRubyOneEight() {
        return ((String) get("RUBY_VERSION")).startsWith("1.8.");
    }

    // FIXME(uwe):  Remove when we stop supporting Ruby 1.8
    @Deprecated
    public static boolean isRubyOneNine() {
        String rv = ((String) get("RUBY_VERSION"));
        return rv.startsWith("2.1.") || rv.startsWith("2.0.") || rv.startsWith("1.9.");
    }

    static void printStackTrace(Throwable t) {
        // TODO(uwe):  Simplify this when Issue #144 is resolved
        // TODO(scott):  printStackTrace is causing too many problems
        //try {
        //    t.printStackTrace(output);
        //} catch (NullPointerException npe) {
        // TODO(uwe): t.printStackTrace() should not fail
        System.err.println(t.getClass().getName() + ": " + t);
        for (StackTraceElement ste : t.getStackTrace()) {
            output.append(ste.toString() + "\n");
        }
        //}
    }

    private static String scriptsDirName(Context context) {
        File storageDir = null;
        if (isDebugBuild()) {
            storageDir = context.getExternalFilesDir(null);
            if (storageDir == null) {
                Log.e("Development mode active, but sdcard is not available.  Make sure you have added\n<uses-permission android:name='android.permission.WRITE_EXTERNAL_STORAGE' />\nto your AndroidManifest.xml file.");
                storageDir = context.getFilesDir();
            }
        } else {
            storageDir = context.getFilesDir();
        }
        File scriptsDir = new File(storageDir, "scripts");
        if ((!scriptsDir.exists() && !scriptsDir.mkdirs())) {
            Log.e("Unable to create the scripts dir.");
            scriptsDir = new File(context.getFilesDir(), "scripts");
        }
        return scriptsDir.getAbsolutePath();
    }

    private static void setDebugBuild(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            isDebugBuild = ((pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        } catch (NameNotFoundException e) {
            isDebugBuild = false;
        }
    }

    public static void setLocalContextScope(String val) {
        localContextScope = val;
    }

    public static void setLocalVariableBehavior(String val) {
        localVariableBehavior = val;
    }

    public static void setOutputStream(PrintStream out) {
        if (ruby == null) {
            output = out;
        } else {
            try {
                Method setOutputMethod = ruby.getClass().getMethod("setOutput", PrintStream.class);
                setOutputMethod.invoke(ruby, out);
                Method setErrorMethod = ruby.getClass().getMethod("setError", PrintStream.class);
                setErrorMethod.invoke(ruby, out);
            } catch (IllegalArgumentException e) {
                handleInitException(e);
            } catch (SecurityException e) {
                handleInitException(e);
            } catch (IllegalAccessException e) {
                handleInitException(e);
            } catch (InvocationTargetException e) {
                handleInitException(e);
            } catch (NoSuchMethodException e) {
                handleInitException(e);
            }
        }
    }

}
