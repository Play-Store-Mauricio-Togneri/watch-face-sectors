package com.mauriciotogneri.watchfacesectors;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class ProfileWrapper
{
    public Profile profile;

    public Paint hoursMarkPaint;
    public Paint minutesMarkPaint;

    public Paint textForegroundPaint;
    public Paint textBorderPaint;

    public Paint outerSectorPaint;
    public Paint middleSectorPaint;
    public Paint innerSectorPaint;

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    public ProfileWrapper(Profile profile)
    {
        updateProfile(profile);
    }

    public synchronized void updateProfile(Profile newProfile)
    {
        profile = newProfile;

        textForegroundPaint = createTimePaint(Style.FILL);
        textForegroundPaint.setColor(profile.timeFontColor);
        textForegroundPaint.setTextSize(profile.timeFontSize);

        textBorderPaint = createTimePaint(Style.STROKE);
        textBorderPaint.setColor(profile.timeBorderColor);
        textBorderPaint.setStrokeWidth(profile.timeBorderWidth);
        textBorderPaint.setTextSize(profile.timeFontSize);

        outerSectorPaint = getOuterSectorPaint();
        middleSectorPaint = getMiddleSectorPaint();
        innerSectorPaint = getInnerSectorPaint();

        hoursMarkPaint = createHoursMarkPaint();
        hoursMarkPaint.setStrokeWidth(profile.hoursMarkWidth);
        hoursMarkPaint.setColor(profile.hoursMarkColor);

        minutesMarkPaint = createMinutesMarkPaint();
        minutesMarkPaint.setStrokeWidth(profile.minutesMarkWidth);
        minutesMarkPaint.setColor(profile.minutesMarkColor);
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

    // =============================================================================================

    private Paint getOuterSectorPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(profile.outerSectorColor);

        return paint;
    }

    private Paint getMiddleSectorPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(profile.middleSectorColor);

        return paint;
    }

    private Paint getInnerSectorPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(profile.innerSectorColor);

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

    private Paint createMinutesMarkPaint()
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);

        return paint;
    }
}