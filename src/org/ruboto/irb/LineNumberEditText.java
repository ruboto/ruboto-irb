package org.ruboto.irb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.DynamicLayout;
import android.text.SpannableStringBuilder;
import android.widget.EditText;

/*********************************************************************************************
 * 
 * LineNumberEditText
 * @author Scott Moyer
 * 
 * Adds the ability to display line numbers next to text (mostly for displaying code).
 */

public class LineNumberEditText extends EditText {
    private boolean showLineNumbers = true;
    private int defaultLeftPadding = 0;
    private int lineHeight = 0;
	private int currentMagnitude = 0;
	private int lineNumberWidth = 0;
	private int lc = 1;
	private int lineAscent;
    private Paint paint = new Paint();
	private TextPaint textPaint = null;
    private DynamicLayout layout = null;
	private SpannableStringBuilder text = null;
	
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
		textPaint = new android.text.TextPaint(paint);
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
		buildLineNumbers();
		invalidate();
	}
	
    /*********************************************************************************************
    *
    * onDraw (main method for adding functionality to Views)
    */
   
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (getShowLineNumbers() && getLineCount() > 0) {
			if (layout == null || lc != getLineCount()) {
				buildLineNumbers();
			}

	        canvas.save();

	        canvas.translate(getScrollX() - 200 + lineNumberWidth, getExtendedPaddingTop() - lineAscent); 
        	canvas.clipRect(200 - lineNumberWidth, getScrollY() - getExtendedPaddingTop(), getPaddingLeft() + 200,
							getBottom() - getTop() - getExtendedPaddingBottom() + getScrollY() - getExtendedPaddingTop() + lineAscent);
        	layout.draw(canvas);

			canvas.restore();
		}
	}
	
    /*********************************************************************************************
     *
	 * Everything needed to update the line numbers and positioning
	 */
	
	private void buildLineNumbers() {
		if  (layout == null) {
			text = new android.text.SpannableStringBuilder("1");
			layout = new android.text.DynamicLayout(text, textPaint, 200, 
							android.text.Layout.Alignment.ALIGN_OPPOSITE, 
							0.0f, lineHeight, false);
			lineAscent = layout.getLineAscent(0);
		}

		int newMagnitude = (!getShowLineNumbers() || getLineCount() == 0) ? 0 : (int)((Math.log10(getLineCount()) + 0.0001) + 1);
		if (currentMagnitude != newMagnitude) {  
        	lineNumberWidth = newMagnitude * (int)paint.getTextSize();          
        	post(
			  	new Runnable() {
				  	public void run() {
					  	setPadding(defaultLeftPadding + lineNumberWidth, getPaddingTop(), getPaddingRight(), getPaddingBottom());
					}
				});
            currentMagnitude = newMagnitude;
		}

		if (lc > getLineCount() && getLineCount() > 0) {
	        text.replace(calculateLineNumberSize(getLineCount()), text.length(), "");
		} else {
	        for(int i = lc + 1; i <= getLineCount(); i++) {
				text.append("\n" + i);
			}
		}
		
		lc = getLineCount() == 0 ? 1 :  getLineCount();
	}
	
    private int calculateLineNumberSize(int i) {
		int size = (i * 2) - 1;
		for(int x = 1; x <= (int)(Math.log10(i) + 0.0001); x++) {
			size += (i - (int)Math.pow(10, x) + 1);
		}
		return size;
    }
}
