package com.mauriciotogneri.watchfacerandomimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;

import java.lang.ref.WeakReference;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

// https://unsplash.it/400/?random
// http://developer.android.com/intl/zh-tw/training/wearables/watch-faces/drawing.html
public class WatchFaceService extends CanvasWatchFaceService
{
    private static final Typeface NORMAL_TYPEFACE = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine()
    {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine
    {
        private final Handler updateTimeHandler = new EngineHandler(this);

        private final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                currentTime.clear(intent.getStringExtra("time-zone"));
                currentTime.setToNow();
            }
        };

        private boolean registeredTimeZoneReceiver = false;

        private Paint foregroundPaint;

        private boolean isAmbientMode;

        private Time currentTime;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        private boolean lowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder)
        {
            super.onCreate(holder);

            WatchFaceStyle.Builder builder = new WatchFaceStyle.Builder(WatchFaceService.this);
            builder.setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE);
            builder.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE);
            builder.setShowSystemUiTime(false);

            setWatchFaceStyle(builder.build());

            foregroundPaint = createTextPaint();

            currentTime = new Time();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds)
        {
            // Draw the background.
            if (isAmbientMode)
            {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.download);
                Paint paint = new Paint();
                paint.setFilterBitmap(true);
                canvas.drawBitmap(bitmap, null, bounds, paint);
            }

            currentTime.setToNow();
            String text = String.format("%d:%02d", currentTime.hour, currentTime.minute);

            Paint stkPaint = new Paint();
            stkPaint.setStyle(Style.STROKE);
            stkPaint.setStrokeWidth(3);
            stkPaint.setColor(Color.WHITE);
            stkPaint.setTextSize(60);
            stkPaint.setTypeface(NORMAL_TYPEFACE);
            stkPaint.setAntiAlias(true);
            stkPaint.setTextAlign(Align.CENTER);
            canvas.drawText(text, bounds.centerX(), (int) (bounds.height() - (bounds.height() * 0.1)), stkPaint);

            canvas.drawText(text, bounds.centerX(), (int) (bounds.height() - (bounds.height() * 0.1)), foregroundPaint);
        }

        @Override
        public void onDestroy()
        {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);

            super.onDestroy();
        }

        private Paint createTextPaint()
        {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setAntiAlias(true);
            paint.setTextAlign(Align.CENTER);

            float size = getResources().getDimension(R.dimen.digital_text_size);
            paint.setTextSize(size);

            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible)
        {
            super.onVisibilityChanged(visible);

            Log.e("VISIBILITY", "" + visible);

            if (visible)
            {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                currentTime.clear(TimeZone.getDefault().getID());
                currentTime.setToNow();
            }
            else
            {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver()
        {
            if (registeredTimeZoneReceiver)
            {
                return;
            }

            registeredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            WatchFaceService.this.registerReceiver(timeZoneReceiver, filter);
        }

        private void unregisterReceiver()
        {
            if (!registeredTimeZoneReceiver)
            {
                return;
            }

            registeredTimeZoneReceiver = false;
            WatchFaceService.this.unregisterReceiver(timeZoneReceiver);
        }

        @Override
        public void onPropertiesChanged(Bundle properties)
        {
            super.onPropertiesChanged(properties);

            lowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick()
        {
            super.onTimeTick();

            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode)
        {
            super.onAmbientModeChanged(inAmbientMode);

            if (isAmbientMode != inAmbientMode)
            {
                isAmbientMode = inAmbientMode;

                if (lowBitAmbient)
                {
                    foregroundPaint.setAntiAlias(!inAmbientMode);
                }

                invalidate();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        /**
         * Starts the {@link #updateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer()
        {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);

            if (shouldTimerBeRunning())
            {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #updateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning()
        {
            return (isVisible() && !isInAmbientMode());
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage()
        {
            invalidate();

            if (shouldTimerBeRunning())
            {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }
    }

    private static class EngineHandler extends Handler
    {
        private final WeakReference<WatchFaceService.Engine> weakReference;

        public EngineHandler(WatchFaceService.Engine reference)
        {
            weakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg)
        {
            WatchFaceService.Engine engine = weakReference.get();

            if (engine != null)
            {
                switch (msg.what)
                {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }
}