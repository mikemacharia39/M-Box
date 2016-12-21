package com.derrick.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DERRICK on 7/17/2016.
 */
public class RobotoCondensedLight extends TextView {

    public RobotoCondensedLight(Context context) {
        super(context);
        setFont();
    }

    public RobotoCondensedLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public RobotoCondensedLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }
    private void setFont() {
        Typeface robotoCondensed = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Light.ttf");
        setTypeface(robotoCondensed, Typeface.BOLD);

    }
}
