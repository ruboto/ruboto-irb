package org.ruboto.irb;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import org.ruboto.JRubyAdapter;
import org.ruboto.Script;

public class IRBScript extends Script {
    public static final String UNTITLED_RB = "untitled.rb";
    private static long scriptsDirModified = 0;

    private static final FilenameFilter RUBY_FILES = new FilenameFilter() {
        public boolean accept(File dir, String fname) {
            return fname.endsWith(".rb");
        }
    };

    private String contents = null;

    /*************************************************************************************************
     *
     * Static Methods: Scripts List
     */

    public static boolean scriptsDirChanged() {
    	return scriptsDirModified != Script.getDirFile().lastModified();
    }

    public static List<String> list() throws SecurityException {
        return IRBScript.list(new ArrayList<String>());
    }

    public static List<String> list(List<String> list) throws SecurityException {
    	scriptsDirModified = Script.getDirFile().lastModified();
        list.clear();
        String[] tmpList = Script.getDirFile().list(RUBY_FILES);
        Arrays.sort(tmpList, 0, tmpList.length, String.CASE_INSENSITIVE_ORDER);
        list.addAll(Arrays.asList(tmpList));
        return list;
    }

    /*************************************************************************************************
    *
    * Constructors
    */

    public IRBScript(String name) {
        this(name, null);
    }

    public IRBScript(String name, String contents) {
        super(name);
        this.contents = contents;
    }

    /* Create a Script from a URL */
    public static IRBScript fromURL(String url) {
    	try {
            String [] temp = url.split("/");
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpGet get = new HttpGet(url);
        	BasicResponseHandler handler = new BasicResponseHandler();
        	return new IRBScript(temp[temp.length -1], client.execute(get, handler));
    	}
    	catch (Throwable t) {
    		return null;
    	}
    }

    /*************************************************************************************************
     *
     * Attribute Access
     */

    public IRBScript setContents(String contents) {
        if (contents == null || contents.equals("")) {
            this.contents = "";
        } else {
            this.contents = contents;
        }
        return this;
    }

    public String getContents() throws IOException {
        if (this.contents == null) {
            BufferedReader buffer = new BufferedReader(new FileReader(getFile()), 8192);
            StringBuilder source = new StringBuilder();
            while (true) {
                String line = buffer.readLine();
			    if (line == null) {
				    break;
			    }
                source.append(line).append("\n");
            }
            buffer.close();
            this.contents = source.toString();
        }
        return this.contents;
    }

    /*************************************************************************************************
     *
     * Script Actions
     */

    public void save() throws IOException {
        BufferedWriter buffer = new BufferedWriter(new FileWriter(getFile()));
        buffer.write(contents);
        buffer.close();
    }

    public String execute() throws IOException {
    	JRubyAdapter.setScriptFilename(getName());
        return JRubyAdapter.execute(getContents());
    }

    public boolean delete() {
        return getFile().delete();
    }
}
