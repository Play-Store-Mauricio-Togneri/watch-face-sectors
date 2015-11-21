package com.mauriciotogneri.watchfacesectors.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

import com.mauriciotogneri.watchfacesectors.Profile;

public class ProfileWrapper
{
    public Profile profile;

    public Paint hoursMarkPaint;
    public Paint minutesMarkPaint;

    public Paint dateForegroundPaint;
    public Paint dateBorderPaint;

    public Paint timeForegroundPaint;
    public Paint timeBorderPaint;

    public Paint outerSectorPaintInteractive;
    public Paint outerSectorPaintAmbient;
    public Paint middleSectorPaintInteractive;
    public Paint middleSectorPaintAmbient;
    public Paint innerSectorPaintInteractive;
    public Paint innerSectorPaintAmbient;

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    public ProfileWrapper(Profile profile)
    {
        updateProfile(profile);
    }

    public synchronized void updateProfile(Profile newProfile)
    {
        profile = newProfile;

        dateForegroundPaint = createDatePaint(Style.FILL);
        dateForegroundPaint.setColor(profile.dateFontColor);
        dateForegroundPaint.setTextSize(profile.dateFontSize);

        dateBorderPaint = createDatePaint(Style.STROKE);
        dateBorderPaint.setColor(profile.dateBorderColor);
        dateBorderPaint.setStrokeWidth(profile.dateBorderWidth);
        dateBorderPaint.setTextSize(profile.dateFontSize);

        timeForegroundPaint = createTimePaint(Style.FILL);
        timeForegroundPaint.setColor(profile.timeFontColor);
        timeForegroundPaint.setTextSize(profile.timeFontSize);

        timeBorderPaint = createTimePaint(Style.STROKE);
        timeBorderPaint.setColor(profile.timeBorderColor);
        timeBorderPaint.setStrokeWidth(profile.timeBorderWidth);
        timeBorderPaint.setTextSize(profile.timeFontSize);

        outerSectorPaintInteractive = getOuterSectorPaint(false);
        outerSectorPaintAmbient = getOuterSectorPaint(true);
        middleSectorPaintInteractive = getMiddleSectorPaint(false);
        middleSectorPaintAmbient = getMiddleSectorPaint(true);
        innerSectorPaintInteractive = getInnerSectorPaint(false);
        innerSectorPaintAmbient = getInnerSectorPaint(true);

        hoursMarkPaint = createHoursMarkPaint();
        hoursMarkPaint.setStrokeWidth(profile.hoursMarkWidth);
        hoursMarkPaint.setColor(profile.hoursMarkColor);

        minutesMarkPaint = createMinutesMarkPaint();
        minutesMarkPaint.setStrokeWidth(profile.minutesMarkWidth);
        minutesMarkPaint.setColor(profile.minutesMarkColor);
    }

    public void setAntiAlias(boolean value)
    {
        dateForegroundPaint.setAntiAlias(value);
        dateBorderPaint.setAntiAlias(value);
        timeForegroundPaint.setAntiAlias(value);
        timeBorderPaint.setAntiAlias(value);
        outerSectorPaintInteractive.setAntiAlias(value);
        outerSectorPaintAmbient.setAntiAlias(value);
        middleSectorPaintInteractive.setAntiAlias(value);
        middleSectorPaintAmbient.setAntiAlias(value);
        innerSectorPaintInteractive.setAntiAlias(value);
        innerSectorPaintAmbient.setAntiAlias(value);
        hoursMarkPaint.setAntiAlias(value);
        minutesMarkPaint.setAntiAlias(value);
    }

    // =============================================================================================

    private Paint createDatePaint(Style style)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        paint.setTypeface(NORMAL_TYPEFACE);
        paint.setTextAlign(Align.CENTER);

        return paint;
    }

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

    private Paint getOuterSectorPaint(boolean ambient)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);

        if (ambient)
        {
            paint.setColor(getGrayscaleColor(profile.outerSectorColor));
        }
        else
        {
            paint.setColor(profile.outerSectorColor);
        }

        return paint;
    }

    private Paint getMiddleSectorPaint(boolean ambient)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);

        if (ambient)
        {
            paint.setColor(getGrayscaleColor(profile.middleSectorColor));
        }
        else
        {
            paint.setColor(profile.middleSectorColor);
        }

        return paint;
    }

    private Paint getInnerSectorPaint(boolean ambient)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);

        if (ambient)
        {
            paint.setColor(getGrayscaleColor(profile.innerSectorColor));
        }
        else
        {
            paint.setColor(profile.innerSectorColor);
        }

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

    // =============================================================================================

    private int getGrayscaleColor(int color)
    {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int average = (red + green + blue) / 3;

        return Color.argb(255, average, average, average);
    }
}