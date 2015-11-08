package com.mauriciotogneri.watchfacesectors;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;

public class ColorDisplayer extends View
{
    public ColorDisplayer(Context context)
    {
        super(context);
    }

    public ColorDisplayer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ColorDisplayer(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public int getDisplayedColor()
    {
        ColorDrawable viewColor = (ColorDrawable) getBackground();

        return viewColor.getColor();
    }
}