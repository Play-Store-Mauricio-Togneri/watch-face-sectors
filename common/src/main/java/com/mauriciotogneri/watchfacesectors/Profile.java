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
    public boolean hoursMarks = true;
    public boolean minutesMarks = true;

    public Paint textForegroundPaint;
    public Paint textBorderPaint;

    public Paint outerSectorPaint;
    public Paint middleSectorPaint;
    public Paint innerSectorPaint;

    public Paint markHoursPaint;
    public Paint markMinutesPaint;

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Profile(Resources resources)
    {
        textForegroundPaint = getTextForegroundPaint(resources);
        textBorderPaint = getTextBorderPaint(resources);
        outerSectorPaint = getOuterSectorPaint();
        middleSectorPaint = getMiddleSectorPaint();
        innerSectorPaint = getInnerSectorPaint();
        markHoursPaint = getMarkHoursPaint();
        markMinutesPaint = getMarkMinutesPaint();
    }

    private Paint getTextForegroundPaint(Resources resources)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 210, 210, 210));
        paint.setTypeface(NORMAL_TYPEFACE);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(resources.getDimension(R.dimen.digital_text_size));

        return paint;
    }

    private Paint getTextBorderPaint(Resources resources)
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

    private Paint getMarkHoursPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.argb(255, 240, 240, 240));

        return paint;
    }

    private Paint getMarkMinutesPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.argb(255, 255, 0, 255));

        return paint;
    }
}