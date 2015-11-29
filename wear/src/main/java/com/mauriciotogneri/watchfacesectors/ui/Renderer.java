package com.mauriciotogneri.watchfacesectors.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.mauriciotogneri.watchfacesectors.ClockHandType;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.formats.DateFormat;
import com.mauriciotogneri.watchfacesectors.formats.TimeFormat;

import java.util.Calendar;

public class Renderer
{
    private final ProfileWrapper profileWrapper;

    private RectF outerSector;
    private RectF middleSector;
    private RectF innerSector;

    public Renderer(Profile profile)
    {
        profileWrapper = new ProfileWrapper(profile);
    }

    public synchronized void onDraw(Canvas canvas, Rect bounds, boolean inAmbientMode)
    {
        if (inAmbientMode)
        {
            canvas.drawColor(Color.BLACK);
        }
        else
        {
            canvas.drawColor(profileWrapper.profile.backgroundColor);
        }

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
            if (profileWrapper.profile.outerSectorType == ClockHandType.TYPE_HOURS)
            {
                drawOuterSector(canvas, hours, inAmbientMode);
            }
            else if (profileWrapper.profile.outerSectorType == ClockHandType.TYPE_MINUTES)
            {
                drawOuterSector(canvas, minutes, inAmbientMode);
            }
            else if ((profileWrapper.profile.outerSectorType == ClockHandType.TYPE_SECONDS) && (!inAmbientMode))
            {
                drawOuterSector(canvas, seconds, false);
            }
        }

        if (profileWrapper.profile.middleSector)
        {
            if (profileWrapper.profile.middleSectorType == ClockHandType.TYPE_HOURS)
            {
                drawMiddleSector(canvas, hours, inAmbientMode);
            }
            else if (profileWrapper.profile.middleSectorType == ClockHandType.TYPE_MINUTES)
            {
                drawMiddleSector(canvas, minutes, inAmbientMode);
            }
            else if ((profileWrapper.profile.middleSectorType == ClockHandType.TYPE_SECONDS) && (!inAmbientMode))
            {
                drawMiddleSector(canvas, seconds, false);
            }
        }

        if (profileWrapper.profile.innerSector)
        {
            if (profileWrapper.profile.innerSectorType == ClockHandType.TYPE_HOURS)
            {
                drawInnerSector(canvas, hours, inAmbientMode);
            }
            else if (profileWrapper.profile.innerSectorType == ClockHandType.TYPE_MINUTES)
            {
                drawInnerSector(canvas, minutes, inAmbientMode);
            }
            else if ((profileWrapper.profile.innerSectorType == ClockHandType.TYPE_SECONDS) && (!inAmbientMode))
            {
                drawInnerSector(canvas, seconds, false);
            }
        }

        if ((profileWrapper.profile.minutesMarkOn) && (!inAmbientMode))
        {
            drawMinutesMarks(canvas, bounds);
        }

        if ((profileWrapper.profile.hoursMarkOn) && (!inAmbientMode))
        {
            drawHoursMarks(canvas, bounds);
        }

        //-----------------------------------------------------------------------

        if ((profileWrapper.profile.dateOn) && (!inAmbientMode))
        {
            String text = DateFormat.formatDate(profileWrapper.profile.dateFormat, calendar);
            float datePosition = bounds.width() * ((10f - profileWrapper.profile.datePosition) / 10f);

            if (profileWrapper.profile.dateBorderWidth > 0)
            {
                canvas.drawText(text, bounds.centerX(), datePosition, profileWrapper.dateBorderPaint);
            }

            canvas.drawText(text, bounds.centerX(), datePosition, profileWrapper.dateForegroundPaint);
        }

        if ((profileWrapper.profile.timeOn) && (!inAmbientMode))
        {
            String text = TimeFormat.formatTime(profileWrapper.profile.timeFormat, calendar);
            float timePosition = bounds.width() * ((10f - profileWrapper.profile.timePosition) / 10f);

            if (profileWrapper.profile.timeBorderWidth > 0)
            {
                canvas.drawText(text, bounds.centerX(), timePosition, profileWrapper.timeBorderPaint);
            }

            canvas.drawText(text, bounds.centerX(), timePosition, profileWrapper.timeForegroundPaint);
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

            drawMark(canvas, profileWrapper.minutesMarkPaint, angle, radiusExternal, radiusInternal, centerX, centerY);
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

    private void drawOuterSector(Canvas canvas, float value, boolean inAmbientMode)
    {
        if (inAmbientMode)
        {
            drawSector(canvas, outerSector, value, profileWrapper.outerSectorPaintAmbient);
        }
        else
        {
            drawSector(canvas, outerSector, value, profileWrapper.outerSectorPaintInteractive);
        }
    }

    private void drawMiddleSector(Canvas canvas, float value, boolean inAmbientMode)
    {
        if (inAmbientMode)
        {
            drawSector(canvas, middleSector, value, profileWrapper.middleSectorPaintAmbient);
        }
        else
        {
            drawSector(canvas, middleSector, value, profileWrapper.middleSectorPaintInteractive);
        }

    }

    private void drawInnerSector(Canvas canvas, float value, boolean inAmbientMode)
    {
        if (inAmbientMode)
        {
            drawSector(canvas, innerSector, value, profileWrapper.innerSectorPaintAmbient);
        }
        else
        {
            drawSector(canvas, innerSector, value, profileWrapper.innerSectorPaintInteractive);
        }
    }

    private void drawSector(Canvas canvas, RectF sector, float value, Paint paint)
    {
        canvas.drawArc(sector, -90, value * 360f, true, paint);
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

    public synchronized void inAmbientMode(boolean isAmbientMode, boolean lowBitAmbient)
    {
        if (lowBitAmbient)
        {
            profileWrapper.setAntiAlias(!isAmbientMode);
        }
    }
}