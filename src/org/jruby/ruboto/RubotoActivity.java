package org.jruby.ruboto;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class RubotoActivity extends Activity 
	implements 
		OnItemClickListener, 
		OnKeyListener,
		OnEditorActionListener,
		OnClickListener {
	
	public static final int CB_ACTIVITY_START 	= 0;
	public static final int CB_ACTIVITY_RESTART = 1;
	public static final int CB_ACTIVITY_RESUME 	= 2;
	public static final int CB_ACTIVITY_PAUSE 	= 3;
	public static final int CB_ACTIVITY_STOP 	= 4;
	public static final int CB_ACTIVITY_DESTROY = 5;
	public static final int CB_INSTANCE_STATE	= 6;
	public static final int CB_ACTIVITY_RESULT 	= 7;
	
	public static final int CB_ITEM_CLICK 		= 8;
	public static final int CB_OPTIONS_MENU 	= 9;
	public static final int CB_CONTEXT_MENU 	= 10;
	public static final int CB_KEY 				= 11;
	public static final int CB_EDITOR_ACTION 	= 12;
	public static final int CB_CLICK 			= 13;
	
	public static final int CB_LAST 			= 20;
	
	private boolean[] callbackOptions = new boolean [CB_LAST];
	private String remoteVariable = "";
	private ProgressDialog loadingDialog; 
    private final Handler loadingHandler = new Handler();

	public RubotoActivity setRemoteVariable(String var) {
		remoteVariable = ((var == null) ? "" : (var + "."));
		return this;
	}
	
	/**********************************************************************************
	 *  
	 *  Callbacks
	 *  
	 */

	/* 
	 *  Management
	 */
	
	public void requestCallback(int id) {
		callbackOptions[id] = true;
	}
	
	public void removeCallback(int id) {
		callbackOptions[id] = false;
	}
	
	/* 
	 *  Activity Lifecycle
	 */
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		
		if (getIntent().getAction() != null  && 
				getIntent().getAction().equals("org.jruby.ruboto.irb.intent.action.LAUNCH_SCRIPT")) {
			/* Launched from a shortcut */
		    Thread t = new Thread() {
				public void run(){
					Script.setUpJRuby(null);
				    loadingHandler.post(loadingComplete);
				}
	        };
	        t.start();
			loadingDialog = ProgressDialog.show(this, "Launching Script", "Initializing Ruboto...", true, false);
		} else {
			Bundle configBundle = getIntent().getBundleExtra("RubotoActivity Config");
			if (configBundle != null) {
				setRemoteVariable(configBundle.getString("Remote Variable"));
				if (configBundle.getBoolean("Define Remote Variable")) {
			        Script.defineGlobalVariable(configBundle.getString("Remote Variable"), this);
				}
				if (configBundle.getString("Initialize Script") != null) {
					Script.execute(configBundle.getString("Initialize Script"));
				}
			} else {
		        Script.defineGlobalVariable("$activity", this);
			}

			Script.defineGlobalVariable("$bundle", savedState);
			Script.execute(remoteVariable + "on_create($bundle)");
		}
	}
	
    protected final Runnable loadingComplete = new Runnable(){
        public void run(){
            loadingDialog.dismiss();
			Script script = new Script(getIntent().getExtras().getString("org.jruby.ruboto.irb.extra.SCRIPT_NAME"));
	        Script.defineGlobalVariable("$activity", RubotoActivity.this);
			try {script.execute();}
			catch (IOException e) {finish();}
        }
    };
	
	@Override
	public void onStart() {
		if (callbackOptions[CB_ACTIVITY_START]) Script.execute(remoteVariable + "on_start()");
		super.onStart();
	}
	
	@Override
	public void onResume() {
		if (callbackOptions[CB_ACTIVITY_RESUME]) Script.execute(remoteVariable + "on_resume()");
		super.onResume();
	}
	
	@Override
	public void onRestart() {
		if (callbackOptions[CB_ACTIVITY_RESTART]) Script.execute(remoteVariable + "on_restart()");
		super.onRestart();
	}
	
	@Override
	public void onPause() {
		if (callbackOptions[CB_ACTIVITY_PAUSE]) Script.execute(remoteVariable + "on_pause()");
		super.onPause();
	}
	
	@Override
	public void onStop() {
		if (callbackOptions[CB_ACTIVITY_STOP]) Script.execute(remoteVariable + "on_stop()");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		if (callbackOptions[CB_ACTIVITY_DESTROY]) Script.execute(remoteVariable + "on_destroy()");
		super.onDestroy();
	}
	
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
		if (callbackOptions[CB_INSTANCE_STATE]) {
	        Script.defineGlobalVariable("$bundle", savedInstanceState);
			Script.execute(remoteVariable + "on_save_instance_state($bundle)");
		}
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
		if (callbackOptions[CB_INSTANCE_STATE]) {
	        Script.defineGlobalVariable("$bundle", savedInstanceState);
			Script.execute(remoteVariable + "on_restore_instance_state($bundle)");
		}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (callbackOptions[CB_ACTIVITY_RESULT]) {
	        Script.defineGlobalVariable("$intent", data);
			Script.execute(remoteVariable + "on_activity_result(" + requestCode + ", " + resultCode + ", $intent)");
		}
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	/* 
	 *  Option Menus
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (callbackOptions[CB_OPTIONS_MENU]) {
	        Script.defineGlobalVariable("$menu", menu);
			Script.execute(remoteVariable + "on_create_options_menu($menu)");
		}
		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (callbackOptions[CB_OPTIONS_MENU]) {
	        Script.defineGlobalVariable("$menu_item", item);
			Script.execute(remoteVariable + "on_menu_item_selected(" + featureId + ", $menu_item)");
		}
    	return super.onMenuItemSelected(featureId, item);
    }

	/*
	 *  Context Menus
	 */
	
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	if (callbackOptions[CB_CONTEXT_MENU]) {
	        Script.defineGlobalVariable("$menu", menu);
	        Script.defineGlobalVariable("$view", v);
	        Script.defineGlobalVariable("$menu_info", menuInfo);
			Script.execute(remoteVariable + "on_create_context_menu($menu, $view, $menu_info)");
    	}
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	if (callbackOptions[CB_OPTIONS_MENU]) {
	        Script.defineGlobalVariable("$menu_item", item);
			Script.execute(remoteVariable + "on_context_item_selected($menu_item)");
	    	return true;
    	}
    	return false;
    }
    
	/* 
	 *  Item Click
	 */
	
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
    	if (callbackOptions[CB_ITEM_CLICK]) {
	        Script.defineGlobalVariable("$adapter_view", av);
	        Script.defineGlobalVariable("$view", v);
			Script.execute(remoteVariable + "on_item_click($adapter_view, $view," + pos + ", " + id + ")");
    	}
    }
    
    public ArrayAdapter<String> arrayAdapterForList(String[] items) {
    	return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    }
    
	/* 
	 *  Editor Clicks and Actions
	 */
    
    public boolean onKey (View v, int keyCode, KeyEvent event) {
    	if (callbackOptions[CB_KEY]) {
	        Script.defineGlobalVariable("$view", v);
	        Script.defineGlobalVariable("$event", event);
			Script.execute(remoteVariable + "on_key($view," + keyCode + ", $event)");
	    	return true;
    	}
    	return false;
    }
    
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
    	if (callbackOptions[CB_EDITOR_ACTION]) {
	        Script.defineGlobalVariable("$view", arg0);
	        Script.defineGlobalVariable("$event", event);
			Script.execute(remoteVariable + "on_editor_action($view," + actionId + ", $event)");
	    	return true;
    	}
		return false;
	}

	
	/* 
	 *  Click
	 */
    
    public void onClick (View v) {
        Script.defineGlobalVariable("$view", v);
		Script.execute(remoteVariable + "on_click($view)");
    }
}
