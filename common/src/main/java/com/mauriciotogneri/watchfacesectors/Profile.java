package com.mauriciotogneri.watchfacesectors;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;

public class Profile
{
    public boolean hoursMarkOn = true;
    public float hoursMarkLength = 1.5f;
    public float hoursMarkWidth = 4;
    public int hoursMarkColor = Color.argb(255, 240, 240, 240);
    private Paint hoursMarkPaint;

    // -------------------------------------------------------------------

    public boolean minutesMarkOn = true;
    public float minutesMarkLength = 1;
    public float minutesMarkWidth = 2;
    public int minutesMarkColor = Color.argb(255, 255, 0, 255);
    private Paint minutesMarkPaint;

    // -------------------------------------------------------------------

    public boolean timeOn = true;
    public String timeFormat = TIME_FORMAT_24;

    public float timeForegroundSize = 40;
    public int timeForegroundColor = Color.argb(255, 210, 210, 210);

    public float timeBorderSize = 1;
    public int timeBorderColor = Color.RED;

    private Paint textForegroundPaint;
    private Paint textBorderPaint;

    // -------------------------------------------------------------------

    public Paint outerSectorPaint;
    public Paint middleSectorPaint;
    public Paint innerSectorPaint;

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private static final String TIME_FORMAT_24 = "%d:%02d";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Profile()
    {
        textForegroundPaint = createTimePaint(Style.FILL);
        textBorderPaint = createTimePaint(Style.STROKE);
        outerSectorPaint = getOuterSectorPaint();
        middleSectorPaint = getMiddleSectorPaint();
        innerSectorPaint = getInnerSectorPaint();
        hoursMarkPaint = createHoursMarkPaint();
        minutesMarkPaint = createMinutesMarkPaint();
    }

    public void setAntiAlias(boolean value)
    {
        textForegroundPaint.setAntiAlias(value);
        textBorderPaint.setAntiAlias(value);
        outerSectorPaint.setAntiAlias(value);
        middleSectorPaint.setAntiAlias(value);
        innerSectorPaint.setAntiAlias(value);
        hoursMarkPaint.setAntiAlias(value);
        minutesMarkPaint.setAntiAlias(value);
    }

    // =============================================================================================

    private Paint createTimePaint(Style style)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        paint.setTypeface(NORMAL_TYPEFACE);
        paint.setTextAlign(Align.CENTER);

        return paint;
    }

    public Paint getTimeForegroundPaint()
    {
        textForegroundPaint.setColor(timeForegroundColor);
        textForegroundPaint.setTextSize(timeForegroundSize);

        return textForegroundPaint;
    }

    public Paint getTimeBorderPaint()
    {
        textBorderPaint.setColor(timeBorderColor);
        textBorderPaint.setStrokeWidth(timeBorderSize);
        textBorderPaint.setTextSize(timeForegroundSize);

        return textBorderPaint;
    }

    // =============================================================================================

    private Paint getOuterSectorPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.argb(255, 165, 200, 60));

        return paint;
    }

    private Paint getMiddleSectorPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.argb(255, 90, 160, 210));

        return paint;
    }

    private Paint getInnerSectorPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.argb(255, 210, 90, 90));

        return paint;
    }

    // =============================================================================================

    private Paint createHoursMarkPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);

        return paint;
    }

    public Paint getHoursMarkPaint()
    {
        hoursMarkPaint.setStrokeWidth(hoursMarkWidth);
        hoursMarkPaint.setColor(hoursMarkColor);

        return hoursMarkPaint;
    }

    // =============================================================================================

    private Paint createMinutesMarkPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);

        return paint;
    }

    public Paint getMinutesMarkPaint()
    {
        minutesMarkPaint.setStrokeWidth(minutesMarkWidth);
        minutesMarkPaint.setColor(minutesMarkColor);

        return minutesMarkPaint;
    }
}