/**********************************************************************************************
*
* RubotoActivity.java is generated from RubotoActivity.erb. Any changes needed in should be 
* made within the erb template or they will be lost.
*
*/

package org.jruby.ruboto;

import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Bundle;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;

public class RubotoActivity extends Activity 
    implements
        android.widget.TimePicker.OnTimeChangedListener,
        android.hardware.SensorEventListener,
        android.widget.AdapterView.OnItemClickListener,
        android.widget.TabHost.OnTabChangeListener,
        android.view.View.OnKeyListener,
        Runnable,
        android.view.View.OnClickListener,
        android.app.DatePickerDialog.OnDateSetListener,
        android.content.DialogInterface.OnClickListener,
        android.widget.TextView.OnEditorActionListener,
        android.widget.TabHost.TabContentFactory,
        android.app.TimePickerDialog.OnTimeSetListener,
        android.widget.DatePicker.OnDateChangedListener
{
    public static final int CB_TIME_CHANGED = 0;
    public static final int CB_ACCURACY_CHANGED = 1;
    public static final int CB_ITEM_CLICK = 2;
    public static final int CB_TAB_CHANGED = 3;
    public static final int CB_SENSOR_CHANGED = 4;
    public static final int CB_PAUSE = 5;
    public static final int CB_KEY = 6;
    public static final int CB_RUN = 7;
    public static final int CB_PREPARE_DIALOG = 8;
    public static final int CB_RESTART = 9;
    public static final int CB_DESTROY = 10;
    public static final int CB_CLICK = 11;
    public static final int CB_DATE_SET = 12;
    public static final int CB_DIALOG_CLICK = 13;
    public static final int CB_ACTIVITY_RESULT = 14;
    public static final int CB_EDITOR_ACTION = 15;
    public static final int CB_CREATE_OPTIONS_MENU = 16;
    public static final int CB_CREATE_CONTEXT_MENU = 17;
    public static final int CB_CREATE_TAB_CONTENT = 18;
    public static final int CB_TIME_SET = 19;
    public static final int CB_STOP = 20;
    public static final int CB_RESUME = 21;
    public static final int CB_SIZE_CHANGED = 22;
    public static final int CB_DRAW = 23;
    public static final int CB_DATE_CHANGED = 24;
    public static final int CB_RESTORE_INSTANCE_STATE = 25;
    public static final int CB_SAVE_INSTANCE_STATE = 26;
    public static final int CB_CREATE_DIALOG = 27;
    public static final int CB_START = 28;
	public static final int CB_LAST = 29;
	
	private boolean[] callbackOptions = new boolean [CB_LAST];
    
	private String remoteVariable = "";
	private ProgressDialog loadingDialog; 
    private final Handler loadingHandler = new Handler();
    private IRubyObject __this__;
    private Ruby __ruby__;

	public RubotoActivity setRemoteVariable(String var) {
		remoteVariable = ((var == null) ? "" : (var + "."));
		return this;
	}
	
	/**********************************************************************************
	 *  
	 *  Callback management
	 */
	
	public void requestCallback(int id) {
		callbackOptions[id] = true;
	}
	
	public void removeCallback(int id) {
		callbackOptions[id] = false;
	}
	
	/* 
	 *  Activity Lifecycle: onCreate
	 */
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		
		if (getIntent().getAction() != null  && 
				getIntent().getAction().equals("org.ruboto.intent.action.LAUNCH_SCRIPT")) {
			/* Launched from a shortcut */
		    Thread t = new Thread() {
				public void run(){
		            Script.configDir(IRB.SDCARD_SCRIPTS_DIR, getFilesDir().getAbsolutePath() + "/scripts");
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

		    __ruby__ = Script.getRuby();
		    __this__ = JavaUtil.convertJavaToRuby(__ruby__, this);
			Script.defineGlobalVariable("$bundle", savedState);
			Script.execute(remoteVariable + "on_create($bundle)");
//            RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_create", JavaUtil.convertJavaToRuby(__ruby__, savedState));
		}
	}
	
    protected final Runnable loadingComplete = new Runnable(){
        public void run(){
            loadingDialog.dismiss();
		    __ruby__ = Script.getRuby();
		    __this__ = JavaUtil.convertJavaToRuby(__ruby__, RubotoActivity.this);
			Script script = new Script(getIntent().getExtras().getString("org.ruboto.extra.SCRIPT_NAME"));
	        Script.defineGlobalVariable("$activity", RubotoActivity.this);
			try {script.execute();}
			catch (IOException e) {finish();}
        }
    };

	/*********************************************************************************
	 *
	 * Ruby Generated Callback Methods
	 */
	
	public void onTimeChanged(android.widget.TimePicker arg0, int arg1, int arg2) {
		
		if (callbackOptions[CB_TIME_CHANGED]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_time_changed", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onAccuracyChanged(android.hardware.Sensor arg0, int arg1) {
		
		if (callbackOptions[CB_ACCURACY_CHANGED]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_accuracy_changed", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onItemClick(android.widget.AdapterView<?> arg0, android.view.View arg1, int arg2, long arg3) {
		
		if (callbackOptions[CB_ITEM_CLICK]) {
            try {
            	IRubyObject[] args = {JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2), JavaUtil.convertJavaToRuby(__ruby__, arg3)};
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_item_click", args);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onTabChanged(String arg0) {
		
		if (callbackOptions[CB_TAB_CHANGED]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_tab_changed", JavaUtil.convertJavaToRuby(__ruby__, arg0));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onSensorChanged(android.hardware.SensorEvent arg0) {
		
		if (callbackOptions[CB_SENSOR_CHANGED]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_sensor_changed", JavaUtil.convertJavaToRuby(__ruby__, arg0));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onPause() {
		super.onPause();
		if (callbackOptions[CB_PAUSE]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_pause");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public boolean onKey(android.view.View arg0, int arg1, android.view.KeyEvent arg2) {
		
		if (callbackOptions[CB_KEY]) {
            try {
            	return (Boolean)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_key", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2)).toJava(boolean.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return false;
	}
	public void run() {
		
		if (callbackOptions[CB_RUN]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "run");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onPrepareDialog(int arg0, android.app.Dialog arg1) {
		super.onPrepareDialog(arg0, arg1);
		if (callbackOptions[CB_PREPARE_DIALOG]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_prepare_dialog", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onRestart() {
		super.onRestart();
		if (callbackOptions[CB_RESTART]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_restart");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onDestroy() {
		super.onDestroy();
		if (callbackOptions[CB_DESTROY]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_destroy");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onClick(android.view.View arg0) {
		
		if (callbackOptions[CB_CLICK]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_click", JavaUtil.convertJavaToRuby(__ruby__, arg0));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onDateSet(android.widget.DatePicker arg0, int arg1, int arg2, int arg3) {
		
		if (callbackOptions[CB_DATE_SET]) {
            try {
            	IRubyObject[] args = {JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2), JavaUtil.convertJavaToRuby(__ruby__, arg3)};
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_date_set", args);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onClick(android.content.DialogInterface arg0, int arg1) {
		
		if (callbackOptions[CB_DIALOG_CLICK]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_dialog_click", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onActivityResult(int arg0, int arg1, android.content.Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (callbackOptions[CB_ACTIVITY_RESULT]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_activity_result", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public boolean onEditorAction(android.widget.TextView arg0, int arg1, android.view.KeyEvent arg2) {
		
		if (callbackOptions[CB_EDITOR_ACTION]) {
            try {
            	return (Boolean)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_editor_action", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2)).toJava(boolean.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return false;
	}
	public boolean onMenuItemSelected(int arg0, android.view.MenuItem arg1) {
		super.onMenuItemSelected(arg0, arg1);
		if (callbackOptions[CB_CREATE_OPTIONS_MENU]) {
            try {
            	return (Boolean)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_menu_item_selected", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1)).toJava(boolean.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return false;
	}
	public boolean onContextItemSelected(android.view.MenuItem arg0) {
		super.onContextItemSelected(arg0);
		if (callbackOptions[CB_CREATE_CONTEXT_MENU]) {
            try {
            	return (Boolean)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_context_item_selected", JavaUtil.convertJavaToRuby(__ruby__, arg0)).toJava(boolean.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return false;
	}
	public void onCreateContextMenu(android.view.ContextMenu arg0, android.view.View arg1, android.view.ContextMenu.ContextMenuInfo arg2) {
		super.onCreateContextMenu(arg0, arg1, arg2);
		if (callbackOptions[CB_CREATE_CONTEXT_MENU]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_create_context_menu", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public android.view.View createTabContent(String arg0) {
		
		if (callbackOptions[CB_CREATE_TAB_CONTENT]) {
            try {
            	return (android.view.View)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "create_tab_content", JavaUtil.convertJavaToRuby(__ruby__, arg0)).toJava(android.view.View.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return null;
	}
	public void onTimeSet(android.widget.TimePicker arg0, int arg1, int arg2) {
		
		if (callbackOptions[CB_TIME_SET]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_time_set", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onStop() {
		super.onStop();
		if (callbackOptions[CB_STOP]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_stop");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onResume() {
		super.onResume();
		if (callbackOptions[CB_RESUME]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_resume");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onSizeChanged(RubotoView arg0, int arg1, int arg2, int arg3, int arg4) {
		
		if (callbackOptions[CB_SIZE_CHANGED]) {
            try {
            	IRubyObject[] args = {JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2), JavaUtil.convertJavaToRuby(__ruby__, arg3), JavaUtil.convertJavaToRuby(__ruby__, arg4)};
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_size_changed", args);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onDraw(RubotoView arg0, android.graphics.Canvas arg1) {
		
		if (callbackOptions[CB_DRAW]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_draw", JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onDateChanged(android.widget.DatePicker arg0, int arg1, int arg2, int arg3) {
		
		if (callbackOptions[CB_DATE_CHANGED]) {
            try {
            	IRubyObject[] args = {JavaUtil.convertJavaToRuby(__ruby__, arg0), JavaUtil.convertJavaToRuby(__ruby__, arg1), JavaUtil.convertJavaToRuby(__ruby__, arg2), JavaUtil.convertJavaToRuby(__ruby__, arg3)};
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_date_changed", args);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onRestoreInstanceState(android.os.Bundle arg0) {
		super.onRestoreInstanceState(arg0);
		if (callbackOptions[CB_RESTORE_INSTANCE_STATE]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_restore_instance_state", JavaUtil.convertJavaToRuby(__ruby__, arg0));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public void onSaveInstanceState(android.os.Bundle arg0) {
		super.onSaveInstanceState(arg0);
		if (callbackOptions[CB_SAVE_INSTANCE_STATE]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_save_instance_state", JavaUtil.convertJavaToRuby(__ruby__, arg0));
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public android.app.Dialog onCreateDialog(int arg0) {
		super.onCreateDialog(arg0);
		if (callbackOptions[CB_CREATE_DIALOG]) {
            try {
            	return (android.app.Dialog)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_create_dialog", JavaUtil.convertJavaToRuby(__ruby__, arg0)).toJava(android.app.Dialog.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return null;
	}
	public void onStart() {
		super.onStart();
		if (callbackOptions[CB_START]) {
            try {
            	RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_start");
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        
	}
	public boolean onCreateOptionsMenu(android.view.Menu arg0) {
		super.onCreateOptionsMenu(arg0);
		if (callbackOptions[CB_CREATE_OPTIONS_MENU]) {
            try {
            	return (Boolean)RuntimeHelpers.invoke(__ruby__.getCurrentContext(), __this__, "on_create_options_menu", JavaUtil.convertJavaToRuby(__ruby__, arg0)).toJava(boolean.class);
            } catch (RaiseException re) {
                re.printStackTrace(__ruby__.getErrorStream());
            }
		}
        return false;
	}
}
