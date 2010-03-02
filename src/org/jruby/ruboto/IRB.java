package org.jruby.ruboto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class IRB extends Activity implements OnItemClickListener {
    public static final String TAG = "Ruboto-IRB";
    
    private TabHost tabs;
    private final Handler handler = new Handler();

    /* IRB_Tab Elements */
    public static TextView currentIrbOutput;
    private TextView irbOutput;
    private HistoryEditText irbInput;

    /* Edit_Tab Elements */
    private EditText sourceEditor;
    private TextView fnameTextView;
    private Script currentScript;

    /* Script_Tab Elements */
    private ArrayAdapter<String> adapter;
    private List<String> scripts;

    /* Tab identifiers */
    private static final int IRB_TAB = 0;
    private static final int EDITOR_TAB = 1;
    private static final int SCRIPTS_TAB = 2;

    /* Menu option identifiers */
    private static final int SAVE_MENU = 1;
    private static final int RUN_MENU = 2;
    private static final int NEW_MENU = 3;
    private static final int HISTORY_MENU = 4;
    private static final int RESCAN_MENU = 5;

    /* Context menu option identifiers for script list */
    private static final int EDIT_MENU = 10;
    private static final int EXECUTE_MENU = 11;
    private static final int DELETE_MENU = 12;

    private static final String DEMO_SCRIPTS = "demo-scripts";
    
    /*********************************************************************************************
     *
     * Setup
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
                
        irbSetUp();
        checkSDCard();
        editorSetUp();
        scriptsListSetUp();
        setUpJRuby();
    }

    private void irbSetUp() {
        tabs.addTab(tabs.newTabSpec("tag1")
                .setContent(R.id.tab1)
                .setIndicator(getString(R.string.IRB_Tab)));

        irbInput = (HistoryEditText) findViewById(R.id.irb_edittext);
        irbOutput = (TextView) findViewById(R.id.irb_textview);
        irbOutput.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        currentIrbOutput = irbOutput;

        irbInput.setLineListener(new HistoryEditText.LineListener() {
            public void onNewLine(String rubyCode) {
                irbOutput.append(rubyCode + "\n");
                String inspected = Script.execute(rubyCode);
                irbOutput.append("=> " + inspected + "\n");
                irbOutput.append(">> ");
                irbInput.setText("");
            }
        });
    }

    private void editorSetUp() {
        tabs.addTab(tabs.newTabSpec("tag2")
                .setContent(R.id.tab2)
                .setIndicator(getString(R.string.Editor_Tab)));

        sourceEditor = (EditText) findViewById(R.id.source_editor);
        fnameTextView = (TextView) findViewById(R.id.fname_textview);
        editScript(Script.UNTITLED_RB, false);
    }

    private void scriptsListSetUp() {
        tabs.addTab(tabs.newTabSpec("tag3")
                .setContent(R.id.tab3)
                .setIndicator(getString(R.string.Scripts_Tab)));

        ListView scriptsList = (ListView) findViewById(R.id.scripts_listview);

        scripts = Script.list();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scripts);
        scriptsList.setAdapter(adapter);
        TextView emptyView = new TextView(this);
        emptyView.setText(R.string.No_scripts);
        scriptsList.setEmptyView(emptyView);
        scriptsList.setOnItemClickListener(this);
        registerForContextMenu(scriptsList);
    }

    /* Initializes jruby in its own thread */
    private void setUpJRuby() {
        if (!Script.initialized()) {
            irbOutput.append("Initializing JRuby...");
            new Thread("JRuby-init") {
                public void run() {
                    // try to avoid ANR's
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    Script.setUpJRuby(new PrintStream(new OutputStream() {
                        @Override
                        public void write(int arg0) throws IOException {
                            IRB.appendToIRB(Character.toString((char) arg0));
                        }
                    }));
                    handler.post(notifyComplete);
                }
            }.start();
        } else {
            notifyComplete.run();
        }
    }

    /* Called when jruby finishes loading */
    protected final Runnable notifyComplete = new Runnable() {
        public void run() {
            Script.defineGlobalVariable("$activity", IRB.this);
            irbOutput.append("Done\n>>");
        }
    };

    /* Static method needed to get to the current irbOutput after the Activity reloads */
    public static void appendToIRB(String string) {
        currentIrbOutput.append(string);
    }

    /*********************************************************************************************
     *
     * Saving and recalling state
     */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence("irbOutput", irbOutput.getText());
        irbInput.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("tab", tabs.getCurrentTab());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("irbOutput"))
            irbOutput.setText(savedInstanceState.getCharSequence("irbOutput"));
        irbInput.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("tab")) tabs.setCurrentTab(savedInstanceState.getInt("tab"));
    }

    /*********************************************************************************************
     *
     * Menu Setup
     */

    /* Set up context menus */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SAVE_MENU, 0, R.string.Menu_save);
        menu.add(0, RUN_MENU, 0, R.string.Menu_run);
        menu.add(0, NEW_MENU, 0, R.string.Menu_new);
        menu.add(0, HISTORY_MENU, 0, R.string.Menu_history);
        menu.add(0, RESCAN_MENU, 0, R.string.Menu_rescan);
        return true;
    }

    /* Called when a menu item clicked */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case SAVE_MENU:
                saveEditorScript();
                return true;
            case RUN_MENU:
                runEditorScript();
                return true;
            case NEW_MENU:
                editScript(Script.UNTITLED_RB, true);
                return true;
            case HISTORY_MENU:
                editScript(new Script(Script.UNTITLED_RB, irbInput.getHistoryString()), true);
                return true;
            case RESCAN_MENU:
                scanScripts();
                tabs.setCurrentTab(SCRIPTS_TAB);
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    /* Set up context menus for script list */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_MENU, 0, R.string.Menu_edit);
        menu.add(0, EXECUTE_MENU, 0, R.string.Menu_run);
        menu.add(0, DELETE_MENU, 0, R.string.Menu_delete);
    }

    /* Called when an entry in the Script List is long clicked */
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_MENU:
                editScript(scripts.get(((AdapterContextMenuInfo) item.getMenuInfo()).position), true);
                return true;
            case EXECUTE_MENU:
                executeScript(scripts.get(((AdapterContextMenuInfo) item.getMenuInfo()).position));
                return true;
            case DELETE_MENU:
                comfirmDelete(scripts.get(((AdapterContextMenuInfo) item.getMenuInfo()).position));
                return true;
            default:
                return false;
        }
    }

    /* Called when an entry in the Script List is clicked */
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        editScript(scripts.get(pos), true);
    }

    /************************************************************************************
     *
     * Script actions
     */

    /* Reload the list of scripts */
    private void scanScripts() {
        try {
            scripts = Script.list(scripts);
            adapter.notifyDataSetChanged();
        }
        catch (SecurityException se) {
            Toast.makeText(this, "Could not create " + Script.SCRIPTS_DIR, Toast.LENGTH_SHORT);
        }
    }

    /* Run the script currently in the editor */
    private void runEditorScript() {
        try {
            irbOutput.append("[Running editor script (" + currentScript.getName() + ")]\n");
            String inspected = currentScript.setContents(sourceEditor.getText().toString()).execute();
            irbOutput.append("=> " + inspected + "\n>> ");
            tabs.setCurrentTab(IRB_TAB);
        }
        catch (IOException e) {
            Toast.makeText(this, "Could not execute script", Toast.LENGTH_SHORT).show();
        }
    }

    /* Save the script currently in the editor */
    private void saveEditorScript() {
        try {
            currentScript
                    .setName(fnameTextView.getText().toString())
                    .setContents(sourceEditor.getText().toString())
                    .save();
            scanScripts();
            Toast.makeText(this, "Saved " + currentScript.getName(), Toast.LENGTH_SHORT).show();
            tabs.setCurrentTab(SCRIPTS_TAB);
        }
        catch (IOException e) {
            Toast.makeText(this, "Could not write " + currentScript.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    /* Load script into editor and switch to editor view */
    private void editScript(Script script, Boolean switchTab) {
        try {
            currentScript = script;
            fnameTextView.setText(script.getName());
            sourceEditor.setText(script.getContents());
            if (switchTab) tabs.setCurrentTab(EDITOR_TAB);
        } catch (IOException e) {
            Toast.makeText(this, "Could not open " + script.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void editScript(String name, Boolean switchTab) {
        editScript(new Script(name), switchTab);
    }

    /* Execute the script and switch to IRB tab*/
    private void executeScript(String name) {
        try {
            irbOutput.append("[Running " + name + "]\n");
            irbOutput.append("=> " + (new Script(name).execute()) + "\n>> ");
            tabs.setCurrentTab(IRB_TAB);
        }
        catch (IOException e) {
            Toast.makeText(this, "Could not open " + name, Toast.LENGTH_SHORT).show();
        }
    }

    /* Delete script and reload scripts list */
    private void deleteScript(String fname) {
        if (new Script(fname).delete()) {
            Toast.makeText(this, fname + " deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Could not delete " + fname, Toast.LENGTH_SHORT).show();
        }
        scanScripts();
    }

    /************************************************************************************
     *
     * Context menu for scripts list.
     * Options: Edit, Execute, Delete
     */
    private void comfirmDelete(final String fname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete " + fname + "?")
                .setCancelable(false)
                .setPositiveButton(R.string.Delete_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteScript(fname);
                    }
                })
                .setNegativeButton(R.string.Delete_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    /*********************************************************************************************
     *
     * Activity Results: Make activity result available to Ruby
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Script.defineGlobalVariable("$last_activity_result", new ActivityResult(requestCode, resultCode, data));
    }

    public static class ActivityResult {
        public int requestCode, resultCode;
        public Intent data;

        public ActivityResult(int req, int res, Intent dat) {
            requestCode = req;
            resultCode = res;
            data = dat;
        }
    }

    private void checkSDCard() {
        if (!Script.isSDCardAvailable()) {
            appendToIRB("No SD card found. Loading/Saving disabled.\n");
        } else {            
            if (!Script.SCRIPTS_DIR_FILE.exists()) {
                // on first install init directory + copy sample scripts
                copyDemoScripts(DEMO_SCRIPTS, Script.SCRIPTS_DIR_FILE);                
            }
        }
    }
        
    private void copyDemoScripts(String from, File to) {                        
        if (!to.mkdirs()) {
            Log.e(TAG, "error creating script directory " + to);
            return;
        }                                    
                
        try {
            byte[] buffer = new byte[8192];        
            for (String f : getAssets().list(from)) {
                File dest = new File(to, f);
                
                if (dest.exists()) 
                    continue;
                
                Log.d(TAG, "copying file " + f);                    
                                
                InputStream is = getAssets().open(from+ "/" +f);                    
                OutputStream fos = new BufferedOutputStream(new FileOutputStream(dest));    

                int n;
                while ((n = is.read(buffer, 0, buffer.length)) != -1)
                    fos.write(buffer, 0, n);                
                
                is.close();
                fos.close();                
            }
        } catch (IOException iox) {
            Log.e(TAG, "error copying demo scripts", iox);     
        }
    }    
}
