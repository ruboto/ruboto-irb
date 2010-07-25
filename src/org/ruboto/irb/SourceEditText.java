package org.ruboto.irb;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.EditText;
import android.widget.TextView;

/*********************************************************************************************
 * 
 * SourceEditText
 * @author Scott Moyer
 * 
 * Adds the ability to display line numbers next to source code. Currently depends on an
 * external TextView for the line numbers and a ScrollView/RelativeLayout to contain the 
 * two. Later, try to do this all within this subclass.
 */

public class SourceEditText extends EditText {
    private TextView lineNumbers = null;
    private int lastLineNumber = 0;
    private int defaultLeftPadding = 0;
    private boolean showLineNumbers = true;

    public SourceEditText(Context context) {
		super(context);
	}

	public SourceEditText(Context context,  android.util.AttributeSet attributes) {
		super(context, attributes);    
	}

	public SourceEditText(Context context,  android.util.AttributeSet attributes, int style) {
		super(context, attributes, style);
	}

	public void setLineNumbersTextView(TextView tv) {
		lineNumbers = tv;
        defaultLeftPadding = getPaddingLeft();
        lineNumbers.setPadding(0, getPaddingTop() + getLineHeight() - lineNumbers.getLineHeight() - 6, 0, 0);
        lineNumbers.setLineSpacing((getLineHeight() - lineNumbers.getLineHeight()), 1.0f);
	}
	
	public boolean getShowLineNumbers() {
		return showLineNumbers;
	}
	
	public void setShowLineNumbers(boolean trueOrFalse) {
		showLineNumbers = trueOrFalse;
		if (showLineNumbers) {
            lineNumbers.setVisibility(VISIBLE);
            checkLineNumbers();
		} else {
            lineNumbers.setVisibility(GONE);
	        setPadding(defaultLeftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
		}
	}
	
	/*
	 * Update line numbers every draw. May be able to move this to onTextChanged.
	 */
	@Override
	public void onDraw(Canvas c) {
		super.onDraw(c);
		checkLineNumbers();
	}
	
	/*
	 * Everything needed to update the line numbers and positioning
	 */
    private void checkLineNumbers() {
    	if (lineNumbers != null && showLineNumbers) {
        	int lineCount = getLineCount();
        	int width = 10;
        	int padding = defaultLeftPadding;

        	// Limits the number of line numbers visible if there are more than in the editor
	        lineNumbers.setMaxHeight((getLineHeight() * lineCount) + getPaddingTop());

	        if (lineCount != 0) {
	        	// Calculate the number of digits and space to display them
	        	width = (int)((Math.floor(Math.log10(lineCount)) + 1) * 10);
	        	padding += width; 
	        }

        	// Limit the width of the line numbers based on the number of digit that need to be displayed
	        lineNumbers.setMaxWidth(width); 
        	// Adjusts the padding on the to leave room for the line numbers
        	setPadding(padding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
	        
        	// Add line numbers to get enough for lines in the editor
        	if (lineCount > lastLineNumber) {
            	StringBuilder string = new StringBuilder();
        		if (lastLineNumber > 0) string.append("\n");
        		string.append(++lastLineNumber);
                while(lastLineNumber < lineCount) {
                	string.append("\n");
                	string.append(++lastLineNumber);
                }
                lineNumbers.append(string.toString());
        	}
    	}
    }
}
