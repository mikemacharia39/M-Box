package com.derrick.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DERRICK on 7/17/2016.
 */
public class RobotoCondensedBold extends TextView {
    public RobotoCondensedBold(Context context) {
        super(context);
        setFont();
    }

    public RobotoCondensedBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoCondensedBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        Typeface robotoCondensed = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        setTypeface(robotoCondensed, Typeface.BOLD);

    }
}
