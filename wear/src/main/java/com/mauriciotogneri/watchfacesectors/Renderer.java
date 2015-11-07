package com.mauriciotogneri.watchfacesectors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Calendar;

// http://developer.android.com/intl/zh-tw/training/wearables/watch-faces/drawing.html
public class Renderer
{
    private ProfileWrapper profileWrapper;

    private RectF outerSector;
    private RectF middleSector;
    private RectF innerSector;

    public Renderer()
    {
        profileWrapper = new ProfileWrapper(new Profile());
    }

    public synchronized void onDraw(Canvas canvas, Rect bounds)
    {
        canvas.drawColor(profileWrapper.profile.backgroundColor);

        setSectorsBounds(bounds);

        //-----------------------------------------------------------------------

        Calendar calendar = Calendar.getInstance();
        int calendarMilliseconds = calendar.get(Calendar.MILLISECOND);
        int calendarSeconds = calendar.get(Calendar.SECOND);
        int calendarMinutes = calendar.get(Calendar.MINUTE);
        int calendarHours = calendar.get(Calendar.HOUR_OF_DAY);

        float milliseconds = (calendarMilliseconds / 1000f);
        float seconds = (calendarSeconds + milliseconds) / 60f;
        float minutes = (calendarMinutes + seconds) / 60f;
        float hours = (((calendarHours >= 12) ? (calendarHours - 12) : calendarHours) + minutes) / 12f;

        //-----------------------------------------------------------------------

        if (profileWrapper.profile.outerSector)
        {
            drawOuterSector(canvas, hours);
        }

        if (profileWrapper.profile.middleSector)
        {
            drawMiddleSector(canvas, minutes);
        }

        if (profileWrapper.profile.innerSector)
        {
            drawInnerSector(canvas, seconds);
        }

        if (profileWrapper.profile.hoursMarkOn)
        {
            drawHoursMarks(canvas, bounds);
        }

        if (profileWrapper.profile.minutesMarkOn)
        {
            drawMinutesMarks(canvas, bounds);
        }

        //-----------------------------------------------------------------------

        if (profileWrapper.profile.timeOn)
        {
            String text = String.format(profileWrapper.profile.timeFormat, calendarHours, calendarMinutes);
            float timePosition = bounds.width() * ((10f - profileWrapper.profile.timePosition) / 10f);

            canvas.drawText(text, bounds.centerX(), timePosition, profileWrapper.textBorderPaint);
            canvas.drawText(text, bounds.centerX(), timePosition, profileWrapper.textForegroundPaint);
        }
    }

    public synchronized void updateProfile(Profile newProfile)
    {
        profileWrapper.updateProfile(newProfile);
    }

    private void drawHoursMarks(Canvas canvas, Rect bounds)
    {
        float radiusExternal = bounds.width();
        float radiusInternal = (bounds.width() / 2f) * ((10f - profileWrapper.profile.hoursMarkLength) / 10f);

        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        for (int i = 0; i < 12; i++)
        {
            drawMark(canvas, profileWrapper.hoursMarkPaint, i * 30, radiusExternal, radiusInternal, centerX, centerY);
        }
    }

    private void drawMinutesMarks(Canvas canvas, Rect bounds)
    {
        float radiusExternal = bounds.width();
        float radiusInternal = (bounds.width() / 2f) * ((10f - profileWrapper.profile.minutesMarkLength) / 10f);

        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        for (int i = 0; i < 60; i++)
        {
            int angle = i * 6;

            if ((angle % 30) != 0)
            {
                drawMark(canvas, profileWrapper.minutesMarkPaint, angle, radiusExternal, radiusInternal, centerX, centerY);
            }
        }
    }

    private void drawMark(Canvas canvas, Paint paint, float angle, float radiusExternal, float radiusInternal, float centerX, float centerY)
    {
        float finalAngle = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(finalAngle);
        float sin = (float) Math.sin(finalAngle);

        Path path = new Path();
        path.moveTo(centerX + (radiusExternal * cos), centerY + (radiusExternal * sin));
        path.lineTo(centerX + (radiusInternal * cos), centerY + (radiusInternal * sin));
        canvas.drawPath(path, paint);
    }

    private void drawOuterSector(Canvas canvas, float value)
    {
        canvas.drawArc(outerSector, -90, value * 360f, true, profileWrapper.outerSectorPaint);
    }

    private void drawMiddleSector(Canvas canvas, float value)
    {
        canvas.drawArc(middleSector, -90, value * 360f, true, profileWrapper.middleSectorPaint);
    }

    private void drawInnerSector(Canvas canvas, float value)
    {
        canvas.drawArc(innerSector, -90, value * 360f, true, profileWrapper.innerSectorPaint);
    }

    private void setSectorsBounds(Rect bounds)
    {
        if (outerSector == null)
        {
            outerSector = new RectF(bounds);
            float half = Math.max(bounds.width(), bounds.height()) / 2f;
            float increment = (half * (float) Math.sqrt(2f)) - half;
            outerSector.inset(-increment, -increment);
        }

        if (middleSector == null)
        {
            middleSector = new RectF(bounds);
            middleSector.inset((bounds.width() / 2) * 0.33f, (bounds.height() / 2) * 0.33f);
        }

        if (innerSector == null)
        {
            innerSector = new RectF(bounds);
            innerSector.inset((bounds.width() / 2) * 0.66f, (bounds.height() / 2) * 0.66f);
        }
    }

    public synchronized void inAmbientMode(boolean inAmbientMode, boolean lowBitAmbient)
    {
        if (lowBitAmbient)
        {
            profileWrapper.setAntiAlias(!inAmbientMode);
        }
    }
}