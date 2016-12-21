package com.derrick.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DERRICK on 7/17/2016.
 */
public class RobotoSlabTextView extends TextView {
    public RobotoSlabTextView(Context context) {
        super(context);
        setFont();
    }

    public RobotoSlabTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoSlabTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }
    private void setFont() {
        Typeface robotoCondensed = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoSlab-Regular.ttf");
        setTypeface(robotoCondensed, Typeface.BOLD);

    }
}
