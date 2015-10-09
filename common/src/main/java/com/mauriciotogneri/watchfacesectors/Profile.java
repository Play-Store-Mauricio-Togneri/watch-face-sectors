package com.mauriciotogneri.watchfacesectors;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import com.mauriciotogneri.common.R;

import java.text.SimpleDateFormat;

public class Profile
{
    public boolean hoursMarkOn = true;
    public float hoursMarkLength = 1.5f;
    public float hoursMarkWidth = 4;
    public int hoursMarkColor = Color.argb(255, 240, 240, 240);
    private Paint hoursMarkPaint;

    public boolean minutesMarkOn = true;
    public float minutesMarkLength = 1;
    public float minutesMarkWidth = 2;
    public int minutesMarkColor = Color.argb(255, 255, 0, 255);
    private Paint minutesMarkPaint;

    public boolean timeOn = true;
    public String timeFormat = TIME_FORMAT_24;
    public Paint textForegroundPaint;
    public Paint textBorderPaint;

    public Paint outerSectorPaint;
    public Paint middleSectorPaint;
    public Paint innerSectorPaint;

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private static final String TIME_FORMAT_24 = "%d:%02d";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Profile(Resources resources)
    {
        textForegroundPaint = createTextForegroundPaint(resources);
        textBorderPaint = createTextBorderPaint(resources);
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

    private Paint createTextForegroundPaint(Resources resources)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 210, 210, 210));
        paint.setTypeface(NORMAL_TYPEFACE);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(resources.getDimension(R.dimen.digital_text_size));

        return paint;
    }

    private Paint createTextBorderPaint(Resources resources)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setTypeface(NORMAL_TYPEFACE);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(resources.getDimension(R.dimen.digital_text_size));

        return paint;
    }

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