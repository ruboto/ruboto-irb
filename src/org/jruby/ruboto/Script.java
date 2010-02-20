package org.jruby.ruboto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.exceptions.RaiseException;
import org.jruby.javasupport.JavaUtil;
import org.jruby.parser.EvalStaticScope;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.scope.ManyVarsDynamicScope;

public class Script {
	public static final String SCRIPTS_DIR = "/sdcard/jruby";
	
	private static final int STATE_EMPTY = 1;
	private static final int STATE_ON_DISK = 2;
	private static final int STATE_IN_MEMORY = 3;
	private static final int STATE_IN_MEMORY_DIRTY = 4;
	
	private String name = null;
	private static RubyInstanceConfig config;
	private static Ruby ruby;
	private static DynamicScope scope;
	private static PrintStream textViewStream;
	private static Boolean initialized = false;
	
	private String contents = null;
	private Integer state = null;
	
	/*************************************************************************************************
	 * 
	 * Static Methods: JRuby Execution 
	 */
	
	public static boolean initialized() {
		return initialized;
	}

	public static void setUpJRuby(OutputStream out){
		if (ruby == null) {
			config = new RubyInstanceConfig();
			config.setCompileMode(RubyInstanceConfig.CompileMode.OFF);
			// TODO: Change
			config.setJRubyHome("/data/app/se.kth.pascalc.JRubyAndroid.apk");
		
	        config.setLoader(Script.class.getClassLoader());
			textViewStream = new PrintStream(out);
	        config.setOutput(textViewStream);

	        /* Set up Ruby environment */
	        ruby = Ruby.newInstance(config);
	        
	        ThreadContext context = ruby.getCurrentContext();
	        DynamicScope currentScope = context.getCurrentScope();
	        scope = new ManyVarsDynamicScope(new EvalStaticScope(currentScope.getStaticScope()), currentScope);
	        initialized = true;
	    }
	}
	
	public static String execute(String code) {
		if (!initialized) return null;
		try {
			return ruby.evalScriptlet(code, scope).inspect().asJavaString();
        } catch (RaiseException re) {                
            re.printStackTrace(textViewStream);
            return null;
        }
	}
	
	public static void defineGlobalConstant(String name, Object object) {
        ruby.defineGlobalConstant(name, JavaUtil.convertJavaToRuby(ruby, object));
	}

	/*************************************************************************************************
	 * 
	 * Static Methods: Scripts List 
	 */

	public static ArrayList<String> list() throws SecurityException {
		return Script.list(new ArrayList<String>());
	}
	
	public static ArrayList<String> list(ArrayList<String> list) throws SecurityException {
		File scriptsDir = new File(SCRIPTS_DIR);
		
		/* Create directory if it doesn't exist */
		if (!scriptsDir.exists()) scriptsDir.mkdir();
		
		String[] scriptsArray = scriptsDir.list(new FilenameFilter() {
			public boolean accept(File dir, String fname){
				return fname.endsWith(".rb");
			}
		});
		
		list.clear();
		for(int i = 0; i < scriptsArray.length; i++){
			list.add(scriptsArray[i]);
		}
		return list;
	}
	
	/*************************************************************************************************
	 * 
	 * Constructors
	 */

	public Script(String name) {
		this(name, null);
	}

	public Script(String name, String contents) {
		this.name = name;
		this.contents = contents;
		File file = new File(SCRIPTS_DIR + "/" + name);
		
		if (contents == null && !file.exists()) {
			state = STATE_EMPTY;
			contents = "";
		} else if (contents == null && file.exists()) {
			state = STATE_ON_DISK;
		} else if (contents != null) {
			state = STATE_IN_MEMORY_DIRTY;
		} else {
			// TODO: Exception
		}
	}
	
	/*************************************************************************************************
	 * 
	 * Attribute Access
	 */

	public String getName() {
		return name;
	}

	public Script setName(String name) {
		this.name = name;
		state = STATE_IN_MEMORY_DIRTY;
		// TODO: Other states possible
		return this;
	}

	public String getContents() throws IOException {
		if (state == STATE_ON_DISK) {
			BufferedReader buffer = new BufferedReader(new FileReader(SCRIPTS_DIR + "/" + name));
			StringBuilder source = new StringBuilder();
			while(true) {
				String line = buffer.readLine();
				if (line == null) break;
				source.append(line + "\n");
			}
			buffer.close();
			contents = source.toString();
			state = STATE_IN_MEMORY;
		}
		return contents;
	}

	public Script setContents(String contents) {
		if (contents == null || contents == "") {
			this.contents = "";
		} else {
			this.contents = contents;
		}
		state = STATE_IN_MEMORY_DIRTY;
		return this;
	}

	/*************************************************************************************************
	 * 
	 * Script Actions
	 */

	public void save() throws IOException {
		if (state != STATE_ON_DISK) {
			BufferedWriter buffer = new BufferedWriter(new FileWriter(SCRIPTS_DIR + "/" + name));
			buffer.write(contents);
			buffer.close();
			state = STATE_IN_MEMORY;
		}
	}

	public String execute() throws IOException {
		return Script.execute(getContents());
	}
	
	public boolean delete() {
		return (new File(SCRIPTS_DIR + "/" + name)).delete();
	}
}
