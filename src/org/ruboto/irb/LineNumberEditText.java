package org.ruboto.irb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.EditText;

/*********************************************************************************************
 * 
 * LineNumberEditText
 * @author Scott Moyer
 * 
 * Adds the ability to display line numbers next to text (mostly for displaying code).
 */

public class LineNumberEditText extends EditText {
    private int defaultLeftPadding = 0;
    private boolean showLineNumbers = true;
    private Paint paint = new Paint();
    private Rect lineRect = new Rect();
    private int lineHeight;

    /*********************************************************************************************
     *
     * Constructors
     */
    
    public LineNumberEditText(Context context) {
		super(context);
    	init();
	}

	public LineNumberEditText(Context context,  android.util.AttributeSet attributes) {
		super(context, attributes);    
    	init();
	}

	public LineNumberEditText(Context context,  android.util.AttributeSet attributes, int style) {
		super(context, attributes, style);
    	init();
	}

    /*********************************************************************************************
    *
    * Init after inflation
    */
   
	public void init() {
    setHorizontallyScrolling(true);
	    paint.setColor(getTextColors().getDefaultColor() & 0xAAFFFFFF);
        paint.setTextSize(getTextSize() * 0.6f);
        defaultLeftPadding = getPaddingLeft();
        lineHeight = getLineHeight();
	}
	
    /*********************************************************************************************
    *
    * Show/hide line numbers
    */
   
	public boolean getShowLineNumbers() {
		return showLineNumbers;
	}
	
	public void setShowLineNumbers(boolean trueOrFalse) {
		showLineNumbers = trueOrFalse;
		invalidate();
	}
	
    /*********************************************************************************************
    *
    * onDraw (main method for adding functionality to Views)
    */
   
	@Override
	public void onDraw(Canvas c) {
    	setPadding(lineNumberPadding(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
		super.onDraw(c);
		if (showLineNumbers) drawLineNumbers(c);
	}
	
    /*********************************************************************************************
     *
	 * Everything needed to update the line numbers and positioning
	 */
	
    private void drawLineNumbers(Canvas canvas) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int topLineNumber = Math.max(1, ((scrollY - getExtendedPaddingTop()) / lineHeight));
        int bottomLineNumber = Math.min(getLineCount(), topLineNumber + (int)(getHeight() / lineHeight));
        
    	canvas.save();

        canvas.clipRect(0, 
						getExtendedPaddingTop() + scrollY, 
						getPaddingLeft() + scrollX,
						getBottom() - getTop() - getExtendedPaddingBottom() + scrollY);

        for(int i = topLineNumber; i <= bottomLineNumber; i ++) {
        	getLineBounds(i - 1, lineRect);
            canvas.drawText(String.valueOf(i), defaultLeftPadding + scrollX, lineRect.bottom - 8, paint);
        }

        canvas.restore();
    }
    
    private int lineNumberPadding() {
    	int lineCount = getLineCount();
    	
    	if (!showLineNumbers || lineCount == 0) {
    		return defaultLeftPadding;
    	} else {
        	return defaultLeftPadding + (int)((Math.floor(Math.log10(lineCount)) + 1) * paint.getTextSize());
    	}
    }
}
