package com.mauriciotogneri.watchfacesectors;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

// http://developer.android.com/intl/zh-tw/training/wearables/watch-faces/drawing.html
public class Renderer
{
    private Paint textForegroundPaint;
    private Paint textBorderPaint;

    private Paint outerSectorPaint;
    private Paint middleSectorPaint;
    private Paint innerSectorPaint;

    private RectF outerSector;
    private RectF middleSector;
    private RectF innerSector;

    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Renderer(Resources resources)
    {
        textForegroundPaint = getTextForegroundPaint(resources);
        textBorderPaint = getTextBorderPaint(resources);
        outerSectorPaint = getOuterSectorPaint();
        middleSectorPaint = getMiddleSectorPaint();
        innerSectorPaint = getInnerSectorPaint();
    }

    public synchronized void onDraw(Canvas canvas, Rect bounds)
    {
        canvas.drawColor(Color.BLACK);

        setSectorsBounds(bounds);

        //-----------------------------------------------------------------------

        Calendar calendar = Calendar.getInstance();
        int calendarMilliseconds = calendar.get(Calendar.MILLISECOND);
        int calendarSeconds = calendar.get(Calendar.SECOND);
        int calendarMinutes = calendar.get(Calendar.MINUTE);
        int calendarHours = calendar.get(Calendar.HOUR_OF_DAY);

        Log.e("DATE", DATE_FORMAT.format(calendar.getTime()));

        float milliseconds = (calendarMilliseconds / 1000f);
        float seconds = (calendarSeconds + milliseconds) / 60f;
        float minutes = (calendarMinutes + seconds) / 60f;
        float hours = (((calendarHours >= 12) ? (calendarHours - 12) : calendarHours) + minutes) / 12f;

        //-----------------------------------------------------------------------

        drawOuterSector(canvas, hours);
        drawMiddleSector(canvas, minutes);
        drawInnerSector(canvas, seconds);

        //-----------------------------------------------------------------------

        String text = String.format("%d:%02d", calendarHours, calendarMinutes);
        canvas.drawText(text, bounds.centerX(), (int) (bounds.height() - (bounds.height() * 0.1)), textBorderPaint);
        canvas.drawText(text, bounds.centerX(), (int) (bounds.height() - (bounds.height() * 0.1)), textForegroundPaint);
    }

    private void drawMarks(Canvas canvas)
    {

    }

    private void drawOuterSector(Canvas canvas, float value)
    {
        canvas.drawArc(outerSector, -90, value * 360f, true, outerSectorPaint);
    }

    private void drawMiddleSector(Canvas canvas, float value)
    {
        canvas.drawArc(middleSector, -90, value * 360f, true, middleSectorPaint);
    }

    private void drawInnerSector(Canvas canvas, float value)
    {
        canvas.drawArc(innerSector, -90, value * 360f, true, innerSectorPaint);
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
            textForegroundPaint.setAntiAlias(!inAmbientMode);
        }
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
        paint.setStrokeWidth(3);
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
}