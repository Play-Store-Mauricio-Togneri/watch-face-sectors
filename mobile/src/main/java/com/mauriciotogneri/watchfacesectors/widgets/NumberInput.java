package com.mauriciotogneri.watchfacesectors.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mauriciotogneri.watchfacesectors.R;

public class NumberInput extends EditText
{
    public NumberInput(Context context)
    {
        super(context);

        init(context, null);
    }

    public NumberInput(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs);
    }

    public NumberInput(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (!isInEditMode())
        {
            if (attrs != null)
            {
                TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.NumberInput);
                float minValue = styledAttributes.getFloat(R.styleable.NumberInput_minValue, 0);
                float maxValue = styledAttributes.getFloat(R.styleable.NumberInput_maxValue, 100);
                styledAttributes.recycle();

                setFilters(new InputFilter[] {new InputFilterMinMax(minValue, maxValue)});
            }
        }
    }

    public void setValue(float value)
    {
        setText(String.valueOf(value));
    }

    public float getValue()
    {
        String text = getText().toString();

        try
        {
            return Float.parseFloat(text);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    private class InputFilterMinMax implements InputFilter
    {
        private final float min;
        private final float max;

        public InputFilterMinMax(float min, float max)
        {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
            try
            {
                float input = Float.parseFloat(dest.toString() + source.toString());

                if (isInRange(min, max, input))
                {
                    return null;
                }
            }
            catch (NumberFormatException nfe)
            {
                // ignore
            }

            return "";
        }

        private boolean isInRange(float a, float b, float c)
        {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}