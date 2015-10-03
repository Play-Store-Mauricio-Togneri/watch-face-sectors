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
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public Renderer(Resources resources)
    {
        textForegroundPaint = getTextForegroundPaint(resources);
        textBorderPaint = getTextBorderPaint(resources);
        outerSectorPaint = getOuterSectorPaint();
        middleSectorPaint = getMiddleSectorPaint();
        innerSectorPaint = getInnerSectorPaint();
    }

    public void onDraw(Canvas canvas, Rect bounds, Time currentTime)
    {
        // clean canvas
        canvas.drawColor(Color.BLACK);

        //-----------------------------------------------------------------------

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

        //-----------------------------------------------------------------------

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("SSS");

        float milliseconds = Integer.parseInt(dateFormat.format(date)) / 1000f;
        float seconds = (currentTime.second + milliseconds) / 60f;
        float minutes = (currentTime.minute + seconds) / 60f;
        float hours = (((currentTime.hour >= 12) ? (currentTime.hour - 12) : currentTime.hour) + minutes) / 12f;

        //-----------------------------------------------------------------------

        canvas.drawArc(outerSector, -90, seconds * 360f, true, outerSectorPaint);
        canvas.drawArc(middleSector, -90, minutes * 360f, true, middleSectorPaint);
        canvas.drawArc(innerSector, -90, hours * 360f, true, innerSectorPaint);

        //-----------------------------------------------------------------------

        String text = String.format("%d:%02d", currentTime.hour, currentTime.minute);
        canvas.drawText(text, bounds.centerX(), (int) (bounds.height() - (bounds.height() * 0.1)), textBorderPaint);
        canvas.drawText(text, bounds.centerX(), (int) (bounds.height() - (bounds.height() * 0.1)), textForegroundPaint);
    }

    public void inAmbientMode(boolean inAmbientMode, boolean lowBitAmbient)
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