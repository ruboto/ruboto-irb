package org.ruboto.irb;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.jruby.embed.io.WriterOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.Environment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View.OnClickListener;
import android.widget.Button;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.ruboto.JRubyAdapter;

public class IRB extends org.ruboto.EntryPointActivity implements OnItemClickListener,
		OnTabChangeListener {
	//////////////////////////////////////////////////////////////
	// Stores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;


	private Item[] fileList;
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;

	ListAdapter adapter1;
	//////////////////////////////////////////////////////////////
	public static final String TAG = "Ruboto-IRB";
	public static final String SDCARD_SCRIPTS_DIR = "/sdcard/jruby";

	
	private TabHost tabs;
	private TabWidget tabWidget;
	private final Handler handler = new Handler();
	private PrintStream printStream;

	/* IRB_Tab Elements */
	public static TextView currentIrbOutput;
	private TextView irbOutput;
	private HistoryEditText irbInput;

	/* Edit_Tab Elements */
	private LineNumberEditText sourceEditor;
	private TextView fnameTextView;
	private IRBScript currentScript;

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
	private static final int ABOUT_MENU = 5;
	private static final int RESCAN_MENU = 6;
	private static final int RELOAD_DEMOS_MENU = 7;
	private static final int CLEAR_IRB_MENU = 8;
	private static final int EDIT_IRB_MENU = 9;
	private static final int MAX_SCREEN_MENU = 10;
	private static final int LINE_NUMBERS_MENU = 11;
	private static final int GOTO_MENU = 12;
	private static final int IMPORT_MENU = 13;
    
	/* Context menu option identifiers for script list */
	private static final int EDIT_MENU = 20;
	private static final int EXECUTE_MENU = 21;
	private static final int DELETE_MENU = 22;
    private static final int SHARE_MENU = 23;
	private static final String DEMO_SCRIPTS = "demo-scripts";

	/*********************************************************************************************
	 *
	 * Setup
	 */

  // Needed because called during startup
  public void initializeRuboto() {
  }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JRubyAdapter.setLocalVariableBehavior("PERSISTENT");
		uiSetup();
	}

  protected void fireRubotoActivity() {
    if(appStarted) return;
    super.fireRubotoActivity();
		configScriptsDir(true);
  }

  public boolean rubotoAttachable() {
    return false;
  }

  private void uiSetup() {
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		if (prefs.getBoolean("HideTitle", false))
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (prefs.getBoolean("Fullscreen", false))
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setOnTabChangedListener(this);
		tabs.setup();

		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabWidget.setVisibility(prefs.getBoolean("HideTabs", false) ? View.GONE
				: View.VISIBLE);

		irbSetUp();

	  printStream = new PrintStream(
		  new WriterOutputStream(new Writer() {
			  @Override
			  public void write(final char[] chars,
					  final int start, final int length)
					  throws IOException {
				  IRB.this.runOnUiThread(new Runnable() {
					  public void run() {
						  IRB.appendToIRB(new String(chars,
								  start, length));
					  }
				  });
			  }

			  @Override
			  public void flush() throws IOException {
				  // no buffer
			  }

			  @Override
			  public void close() throws IOException {
				  // meaningless
			  }
		  }));

	    JRubyAdapter.setOutputStream(printStream);
        irbOutput.append(">> ");

		configScriptsDir(false);
		editorSetUp();
		scriptsListSetUp();

    autoLoadScript();
  }

	private void irbSetUp() {
		tabs.addTab(tabs
				.newTabSpec("irb")
				.setContent(R.id.tab1)
				.setIndicator(getString(R.string.IRB_Tab),
						getResources().getDrawable(R.drawable.ic_tab_irb)));

		irbInput = (HistoryEditText) findViewById(R.id.irb_edittext);
		irbOutput = (TextView) findViewById(R.id.irb_textview);
		irbOutput
				.setMovementMethod(new android.text.method.ScrollingMovementMethod());
		currentIrbOutput = irbOutput;

		irbInput.setLineListener(new HistoryEditText.LineListener() {
			public void onNewLine(String rubyCode) {
				irbOutput.append(rubyCode + "\n");
				try {
					irbOutput.append("=> ");
         	        JRubyAdapter.setScriptFilename("eval");
					irbOutput.append(JRubyAdapter.execute(rubyCode));
				} catch (RuntimeException e) {
          reportExecption(e);
				}
				irbOutput.append("\n>> ");
				irbInput.setText("");
			}
		});
	}

	private void editorSetUp() {
		tabs.addTab(tabs
				.newTabSpec("editor")
				.setContent(R.id.tab2)
				.setIndicator(getString(R.string.Editor_Tab),
						getResources().getDrawable(R.drawable.ic_tab_editor)));

		sourceEditor = (LineNumberEditText) findViewById(R.id.source_editor);
		fnameTextView = (TextView) findViewById(R.id.fname_textview);

		final android.view.GestureDetector gestureDetector = new android.view.GestureDetector(new FlingGestureListener());
		sourceEditor.setOnTouchListener(new android.view.View.OnTouchListener() {
			public boolean onTouch(android.view.View view,android.view.MotionEvent event) {
				  return gestureDetector.onTouchEvent(event);
			}
    });

		editScript(IRBScript.UNTITLED_RB, false);
	}

	private void scriptsListSetUp() {
		tabs.addTab(tabs
				.newTabSpec("scripts")
				.setContent(R.id.tab3)
				.setIndicator(getString(R.string.Scripts_Tab),
						getResources().getDrawable(R.drawable.ic_tab_scripts)));

		ListView scriptsList = (ListView) findViewById(R.id.scripts_listview);

		scripts = IRBScript.list();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, scripts);
		scriptsList.setAdapter(adapter);
		TextView emptyView = new TextView(this);
		emptyView.setText(R.string.No_scripts);
		scriptsList.setEmptyView(emptyView);
		scriptsList.setOnItemClickListener(this);
		registerForContextMenu(scriptsList);
	}

	/*
	 * Static method needed to get to the current irbOutput after the Activity
	 * reloads
	 */
	public static void appendToIRB(String string) {
		currentIrbOutput.append(string);
	}

	/* Loads the script specified in the Intent (if supplied) */
	public void autoLoadScript() {
		if (getIntent().getData() != null) {
			IRBScript script = IRBScript.fromURL(getIntent().getData()
					.toString());
			if (script != null)
				editScript(script, true);
		}
	}

	/* Common method for copying stacktrace to irbOutput */
	private void reportExecption(Exception e) {
		// TODO: Compact or highlight some levels
		for (java.lang.StackTraceElement ste : e.getStackTrace()) {
			irbOutput.append(ste.toString() + "\n");
		}
	}

	/*********************************************************************************************
	 *
	 * TabHost Listener
	 */

	public void onTabChanged(String tabId) {
		if (tabId.equals("scripts")) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(tabs.getWindowToken(), 0);
			if (IRBScript.scriptsDirChanged())
				scanScripts();
		}
	}

	/*********************************************************************************************
	 *
	 * Menu Setup
	 */

	/* Set up context menus */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_MENU, 0, R.string.Menu_save).setIcon(
				android.R.drawable.ic_menu_save);
		menu.add(0, RUN_MENU, 0, R.string.Menu_run).setIcon(
				R.drawable.ic_menu_play);
		menu.add(0, NEW_MENU, 0, R.string.Menu_new).setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(0, HISTORY_MENU, 0, R.string.Menu_history).setIcon(
				android.R.drawable.ic_menu_recent_history);
		menu.add(0, ABOUT_MENU, 0, R.string.Menu_about).setIcon(
				android.R.drawable.ic_menu_info_details);
//		menu.add(0, RESCAN_MENU, 0, R.string.Menu_rescan);
		menu.add(0, RELOAD_DEMOS_MENU, 0, R.string.Menu_reload);
		menu.add(0, CLEAR_IRB_MENU, 0, R.string.Menu_clear_irb);
		menu.add(0, EDIT_IRB_MENU, 0, R.string.Menu_edit_irb);
		menu.add(0, MAX_SCREEN_MENU, 0, R.string.Menu_max_screen);
		menu.add(0, LINE_NUMBERS_MENU, 0, R.string.Menu_line_numbers);
		menu.add(0, GOTO_MENU, 0, R.string.Menu_goto);
		menu.add(0, IMPORT_MENU, 0, R.string.Menu_import);
		return true;
	}

	/* Called when a menu item clicked */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		SharedPreferences prefs = null;
		SharedPreferences.Editor prefsEditor = null;

		switch (item.getItemId()) {
		case SAVE_MENU:
			saveEditorScript();
			return true;
		case RUN_MENU:
			runEditorScript();
			return true;
		case NEW_MENU:
			editScript(IRBScript.UNTITLED_RB, true);
			return true;
		case HISTORY_MENU:
			editScript(
					new IRBScript(IRBScript.UNTITLED_RB,
							irbInput.getHistoryString()), true);
			return true;
		case ABOUT_MENU:
			aboutDialog();
			return true;
		case RESCAN_MENU:
			scanScripts();
			tabs.setCurrentTab(SCRIPTS_TAB);
			return true;
		case RELOAD_DEMOS_MENU:
			Toast.makeText(this,
					recopyDemoScripts(DEMO_SCRIPTS, IRBScript.getDirFile()),
					Toast.LENGTH_SHORT).show();
			scanScripts();
			tabs.setCurrentTab(SCRIPTS_TAB);
			return true;
		case CLEAR_IRB_MENU:
			irbOutput.setText(">> ");
			tabs.setCurrentTab(IRB_TAB);
			return true;
		case EDIT_IRB_MENU:
			editScript(new IRBScript(IRBScript.UNTITLED_RB, irbOutput.getText()
					.toString()), true);
			tabs.setCurrentTab(EDITOR_TAB);
			return true;
		case MAX_SCREEN_MENU:
			prefs = getPreferences(Context.MODE_PRIVATE);
			prefsEditor = prefs.edit();

			tabWidget
					.setVisibility(tabWidget.getVisibility() == View.VISIBLE ? View.GONE
							: View.VISIBLE);
			if (prefs.getBoolean("Fullscreen", false)) {
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				getWindow()
						.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}

			prefsEditor.putBoolean("HideTabs",
					tabWidget.getVisibility() == View.GONE);
			prefsEditor.putBoolean("Fullscreen",
					!prefs.getBoolean("Fullscreen", false));
			prefsEditor.putBoolean("HideTitle",
					!prefs.getBoolean("HideTitle", false));
			prefsEditor.commit();

			displayDialog("Title Visibility Change",
					"Reorient the screen for this to take effect.", null, null);

			return true;
		case LINE_NUMBERS_MENU:
			sourceEditor.setShowLineNumbers(!sourceEditor.getShowLineNumbers());
			tabs.setCurrentTab(EDITOR_TAB);
			return true;
		case GOTO_MENU:
			tabs.setCurrentTab(EDITOR_TAB);
			gotoDialog();
			return true;
		case IMPORT_MENU:
			loadFileList();
			showDialog(DIALOG_LOAD_FILE);
			Log.d(TAG, path.getAbsolutePath());
		    return true; 
		     
		}

		return super.onMenuItemSelected(featureId, item);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String readfiles(String path)
	{
		String aBuffer = "";
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
          //  String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
   //         txtData.setText(aBuffer);
            myReader.close();
            Toast.makeText(getBaseContext(),
                    "Done reading SD 'mysdfile.txt'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
		
		
		return aBuffer;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void loadFileList() {
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();

				}
			};

			String[] fList = null;
			try{
				 fList = path.list(filter);
				 fileList = new Item[fList.length];
			}
			catch (Exception e) {
				Toast.makeText(IRB.this,"No SD card",1).show();
				return;
			}
			for (int i = 0; i < fList.length; i++) {
				fileList[i] = new Item(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList[i].file);
				} else {
					Log.d("FILE", fileList[i].file);
				}
			}

			if (!firstLvl) {
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		} else {
			Log.e(TAG, "path does not exist");
		}

		adapter1 = new ArrayAdapter<Item>(this,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				return view;
			}
		};

	}

	private class Item {
		public String file;
		public int icon;

		public Item(String file, Integer icon) {
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString() {
			return file;
		}
	}

	@Override
	public Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		if (fileList == null) {
			Log.e(TAG, "No files loaded");
			dialog = builder.create();
			return dialog;
		}

		switch (id) {
		case DIALOG_LOAD_FILE:
			builder.setTitle("Choose your file");
			builder.setAdapter(adapter1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					chosenFile = fileList[which].file;
					File sel = new File(path + "/" + chosenFile);
					if (sel.isDirectory()) {
						firstLvl = false;

						// Adds chosen directory to list
						str.add(chosenFile);
						fileList = null;
						path = new File(sel + "");

						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}

					// Checks if 'up' was clicked
					else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

						// present directory removed from list
						String s = str.remove(str.size() - 1);

						// path modified to exclude present directory
						path = new File(path.toString().substring(0,
								path.toString().lastIndexOf(s)));
						fileList = null;

						// if there are no more directories in the list, then
						// its the first level
						if (str.isEmpty()) {
							firstLvl = true;
						}
						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}
					// File picked
					else {
						int dot = chosenFile.lastIndexOf(".");
						  String ext = chosenFile.substring(dot + 1);
						  if(ext.equals("txt") || ext.equals("rb") || ext.equals("script") )
						  {
							  neweditScript(readfiles(path + "/" + chosenFile),true,chosenFile);
						  }
						  else
							  Toast.makeText(IRB.this,"Not supported extension type" + ext,1).show();
						
						//	finish();
					//	return;
					}

				}
			});
			break;
		}
		dialog = builder.show();
		return dialog;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* Set up context menus for script list */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, EDIT_MENU, 0, R.string.Menu_edit);
		menu.add(0, EXECUTE_MENU, 0, R.string.Menu_run);
		menu.add(0, DELETE_MENU, 0, R.string.Menu_delete);
		menu.add(0, SHARE_MENU, 0, R.string.Menu_share);
	}

	/* Called when an entry in the Script List is long clicked */
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case EDIT_MENU:
			editScript(
					scripts.get(((AdapterContextMenuInfo) item.getMenuInfo()).position),
					true);
			return true;
		case EXECUTE_MENU:
			executeScript(scripts.get(((AdapterContextMenuInfo) item
					.getMenuInfo()).position));
			return true;
		case DELETE_MENU:
			confirmDelete(scripts.get(((AdapterContextMenuInfo) item
					.getMenuInfo()).position));
			return true;
		case SHARE_MENU:
		    shareScript(scripts.get(((AdapterContextMenuInfo) item
					.getMenuInfo()).position));
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
			scripts = IRBScript.list(scripts);
			adapter.notifyDataSetChanged();
		} catch (SecurityException se) {
			Toast.makeText(this, "Could not create " + IRBScript.getDir(),
					Toast.LENGTH_SHORT);
		}
	}

	/* Run the script currently in the editor */
	private void runEditorScript() {
		irbOutput.append("[Running editor script (" + currentScript.getName()
				+ ")]\n");
		try {
			irbOutput.append("=> ");
     	JRubyAdapter.setScriptFilename(currentScript.getName());
			irbOutput.append(JRubyAdapter.execute(sourceEditor.getText()
					.toString()));
		} catch (RuntimeException e) {
			reportExecption(e);
		}
		irbOutput.append("\n>> ");

		tabs.setCurrentTab(IRB_TAB);
	}

	/* Save the script currently in the editor */
	private void saveEditorScript() {
		try {
      currentScript = new IRBScript(fnameTextView.getText().toString());
 		  currentScript.setContents(sourceEditor.getText().toString()).save();
			scanScripts();
			Toast.makeText(this, "Saved " + currentScript.getName(),
					Toast.LENGTH_SHORT).show();
			tabs.setCurrentTab(SCRIPTS_TAB);
		} catch (IOException e) {
			Toast.makeText(this, "Could not write " + currentScript.getName(),
					Toast.LENGTH_SHORT).show();
		}
	}

	/* Load script into editor and switch to editor view */
	private void editScript(IRBScript script, Boolean switchTab) {
		try {
			currentScript = script;
			fnameTextView.setText(script.getName());
			sourceEditor.setText(script.getFile().exists() ? script
					.getContents() : "");
			//Toast.makeText(IRB.this,script.getFile().exists() ? script.getContents() : "",1).show();		
			sourceEditor.scrollTo(0, 0);
			if (switchTab)
				tabs.setCurrentTab(EDITOR_TAB);
		} catch (IOException e) {
			Toast.makeText(this, "Could not open " + script.getName(),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void neweditScript(String content, Boolean switchTab,String ChosenFileName){
			fnameTextView.setText(ChosenFileName);
	         sourceEditor.setText(content);
			 sourceEditor.scrollTo(0,0);
			 if (switchTab)
				tabs.setCurrentTab(EDITOR_TAB);
	
	}

	private void editScript(String name, Boolean switchTab) {
		editScript(new IRBScript(name), switchTab);
	}

	/* Execute the script and switch to IRB tab */
	private void executeScript(String name) {
		try {
			irbOutput.append("[Running " + name + "]\n");
			irbOutput.append("=> ");
			irbOutput.append(new IRBScript(name).execute());
		} catch (IOException e) {
			Toast.makeText(this, "Could not open " + name, Toast.LENGTH_SHORT)
					.show();
		} catch (RuntimeException e) {
			reportExecption(e);
		}
		irbOutput.append("\n>> ");

		tabs.setCurrentTab(IRB_TAB);
	}

	/* Delete script and reload scripts list */
	private void deleteScript(String fname) {
		if (new IRBScript(fname).delete()) {
			Toast.makeText(this, fname + " deleted!", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(this, "Could not delete " + fname,
					Toast.LENGTH_SHORT).show();
		}
		scanScripts();
	}
	

	private void shareScript(IRBScript script) {
		Intent i = new Intent(this,net.android.facebook.TestConnect.class);
		try {
			currentScript = script;
//			ScriptName = script.getName();
//			ScriptContent=script.getFile().exists() ? script.getContents() : "";
			i.putExtra("code",script.getFile().exists() ? script.getContents() : "");
			i.putExtra("Sname",script.getName());
			startActivity(i);

		} catch (IOException e) {
			Toast.makeText(this, "Could not share " + script.getName(),
					Toast.LENGTH_SHORT).show();
		}
		
		
	}

	private void shareScript(String name) {
		shareScript(new IRBScript(name));
	}
	
	
	
	/************************************************************************************
	 *
	 * Dialogs
	 */

	/*
	 * Generic dialogs
	 */

	private void displayDialog(String title, Object messageOrView,
			DialogInterface.OnClickListener positive,
			DialogInterface.OnClickListener negative) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		if (messageOrView instanceof String) {
			builder.setMessage((String) messageOrView);
		} else {
			builder.setView((View) messageOrView);
		}
		if (positive == null) {
			builder.setPositiveButton(R.string.Dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
		} else {
			builder.setPositiveButton(R.string.Dialog_ok, positive);
		}
		if (negative != null) {
			builder.setNegativeButton(R.string.Dialog_cancel, negative);
		}
		builder.create().show();
	}

	/*
	 * Script deletion dialog
	 */
	private void confirmDelete(final String fname) {
		displayDialog("Confirm", "Delete " + fname + "?",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						deleteScript(fname);
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
	}

	/*
	 * About dialog
	 */

	private void aboutDialog() {
		ScrollView sv = new ScrollView(this);
		TextView tv = new TextView(this);
		tv.setPadding(5, 5, 5, 5);
		tv.setText(R.string.About_text);
		Linkify.addLinks(tv, Linkify.ALL);
		sv.addView(tv);

		displayDialog(getString(R.string.app_name) + " v "
				+ getString(R.string.version_name), sv, null, null);
	}

	/*
	 * Goto dialog
	 */

	private void gotoDialog() {
		final EditText et = new EditText(this);

		displayDialog("Go to", et, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					int i = Integer.valueOf(et.getText().toString());
					sourceEditor.scrollTo(0, (i - 1) * sourceEditor.getLineHeight());
				} catch (Exception e) {
				}
			}
		}, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
	}

	/*********************************************************************************************
	 *
	 * Activity Results: Make activity result available to Ruby
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		JRubyAdapter.defineGlobalVariable("$last_activity_result",
				new ActivityResult(requestCode, resultCode, data));
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

	protected static String scriptsDirName(Activity context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/jruby";
		} else {
			return context.getFilesDir().getAbsolutePath() + "/scripts";
		}
	}

	private void configScriptsDir(boolean checkForUpdate) {
    if (checkForUpdate) {
      JRubyAdapter.addLoadPath(IRB.scriptsDirName(this));
    } else {
		  IRBScript.setDir(IRB.scriptsDirName(this));
    }

		if (!IRBScript.getDirFile().exists()) {
			// on first install init directory + copy sample scripts
			copyDemoScripts(DEMO_SCRIPTS, IRBScript.getDirFile());
		} else {
      File from = new File(IRBScript.getDirFile(), "ruboto.rb");
  		if (from.exists())
        removeOldRubotoScripts();

      if (checkForUpdate && !checkVersionString()) {
			  // Scripts exist but need updating
			  confirmUpdate();
      }
		}
	}

	private void copyDemoScripts(String from, File to) {
		IRBScript.getDirFile().mkdirs();
		try {
			byte[] buffer = new byte[8192];
			for (String f : getAssets().list(from)) {
				File dest = new File(to, f);

				if (dest.exists())
					continue;

				Log.d(TAG, "copying file " + f);

				if (getAssets().list(from + "/" + f).length == 0) {
					InputStream is = getAssets().open(from + "/" + f);
					OutputStream fos = new BufferedOutputStream(
							new FileOutputStream(dest), 8192);

					int n;
					while ((n = is.read(buffer, 0, buffer.length)) != -1)
						fos.write(buffer, 0, n);

					is.close();
					fos.close();
				} else {
					dest.mkdir();
					copyDemoScripts(from + "/" + f, dest);
				}
			}

			updateVersionString();
		} catch (IOException iox) {
			Log.e(TAG, "error copying demo scripts", iox);
		}
	}

	public String recopyDemoScripts(String from, File to) {
		String rv = "";
		try {
			byte[] buffer = new byte[8192];
			for (String f : getAssets().list(from)) {
				File dest = new File(to, f);

				if (dest.exists()) {
					Log.d(TAG, "replacing file " + f);
				} else {
					Log.d(TAG, "copying file " + f);
				}

				if (getAssets().list(from + "/" + f).length == 0) {
					InputStream is = getAssets().open(from + "/" + f);
					OutputStream fos = new BufferedOutputStream(
							new FileOutputStream(dest), 8192);

					int n;
					while ((n = is.read(buffer, 0, buffer.length)) != -1)
						fos.write(buffer, 0, n);

					is.close();
					fos.close();
					rv += "\nCopied:" + f;
				} else {
					dest.mkdir();
					rv += "\n" + recopyDemoScripts(from + "/" + f, dest);
				}
			}

			updateVersionString();
		} catch (IOException iox) {
			Log.e(TAG, "error copying demo scripts", iox);
			rv = "Copy failed";
		}
		return rv;
	}

	private boolean checkVersionString() {
		return getPreferences(Context.MODE_PRIVATE).getString(
				"Demo_scripts_version", "0").equals(
				getString(R.string.demo_scripts_version));
	}

	private void updateVersionString() {
		if (!checkVersionString()) {
			SharedPreferences.Editor prefsEditor = getPreferences(
					Context.MODE_PRIVATE).edit();
			prefsEditor.putString("Demo_scripts_version",
					getString(R.string.demo_scripts_version));
			prefsEditor.commit();
		}
	}

	private void confirmUpdate() {
		displayDialog("Update Scripts", getString(R.string.Script_update_text),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(
								IRB.this,
								IRB.this.recopyDemoScripts(DEMO_SCRIPTS,
										IRBScript.getDirFile()),
								Toast.LENGTH_SHORT).show();
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
	}

	private void removeOldRubotoScripts() {
		displayDialog("Remove Ruboto Scripts", getString(R.string.Ruboto_remove_text),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
            File from = new File(IRBScript.getDirFile(), "ruboto.rb");
            if (from.exists())
              from.renameTo(new File(IRBScript.getDirFile(), "old-ruboto.rb"));

            from = new File(IRBScript.getDirFile(), "ruboto");
            if (from.exists())
              from.renameTo(new File(IRBScript.getDirFile(), "old-ruboto"));
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
	}
}
