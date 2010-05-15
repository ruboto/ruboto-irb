package org.jruby.ruboto;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class RubotoView extends View {
	private boolean requestedCallback = true;
	
	public RubotoView(Context context) {
		super(context);
	}

	public RubotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RubotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	void setCallback(boolean flag) {
		requestedCallback = flag;
	}

    @Override 
    protected void onDraw(Canvas canvas) {
    	if (requestedCallback) ((RubotoActivity) getContext()).onDraw(this, canvas);
    }
	
}
