package org.jruby.ruboto;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/*********************************************************************************************
 * 
 * EditText with history (key down, key up)
 * @author Jan Berkel
 */
public class HistoryEditText extends EditText implements
	android.view.View.OnKeyListener,
	TextView.OnEditorActionListener
{ 
	public interface LineListener {
		void onNewLine(String s);
	}     

	private int cursor = -1;
	private List<String> history = new ArrayList<String>();
	private LineListener listener;

	public HistoryEditText(Context ctxt) {
		super(ctxt);
		initListeners();
	}

	public HistoryEditText(Context ctxt,  android.util.AttributeSet attrs) {
		super(ctxt, attrs);    
		initListeners();
	}

	public HistoryEditText(Context ctxt,  android.util.AttributeSet attrs, int defStyle) {
		super(ctxt, attrs, defStyle);
		initListeners();
	}

	private void initListeners() {
		setOnKeyListener(this);
		setOnEditorActionListener(this);
	}

    public void onSaveInstanceState(Bundle savedInstanceState) {
    	savedInstanceState.putStringArrayList("history", (ArrayList<String>)history);
    	savedInstanceState.putInt("cursor", cursor);
    }
    
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	if (savedInstanceState.containsKey("history")) history = (List<String>)savedInstanceState.getStringArrayList("history");
    	if (savedInstanceState.containsKey("cursor")) cursor = savedInstanceState.getInt("cursor");
    }
    
    public String getHistoryString() {
    	StringBuilder string = new StringBuilder();
    	for (int i=0; i < history.size(); i++) {
    		string.append(history.get(i));
    		string.append("\n");
    	}
    	return string.toString();
    }
    
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_NULL) {
			String line = getText().toString();
			if (line.length() == 0) return true;           
			history.add(line);        
			cursor = history.size();

			if (listener != null) {
				listener.onNewLine(line);
				return true;
			}
		}
		return false;
	}

	public void setLineListener(LineListener l) { this.listener = l; }

	public void setCursorPosition(int pos) {
		Selection.setSelection(getText(), pos);
	}

	public boolean onKey(View view, int keyCode, KeyEvent evt) {        
		if (evt.getAction() == KeyEvent.ACTION_DOWN || evt.getAction() == KeyEvent.ACTION_MULTIPLE) {

			if (cursor >= 0 && (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {                        
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP ) {
					cursor -= 1;
				} else {
					cursor += 1;
				}

				if (cursor < 0)
					cursor = 0;
				else if (cursor >= history.size()) {
					cursor = history.size() - 1;
				}
				setText(history.get(cursor));
				return true;
			} 
		}
		return false;
	}
}
