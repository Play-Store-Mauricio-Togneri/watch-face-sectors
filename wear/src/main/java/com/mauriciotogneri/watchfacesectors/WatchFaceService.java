package com.mauriciotogneri.watchfacesectors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

import java.lang.ref.WeakReference;
import java.util.TimeZone;

public class WatchFaceService extends CanvasWatchFaceService
{
    private static final long INTERACTIVE_UPDATE_RATE_MS = 100;

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
        private boolean isAmbientMode;
        private Time currentTime;
        private Renderer renderer;

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

            renderer = new Renderer(getResources());
            currentTime = new Time();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds)
        {
            currentTime.setToNow();

            renderer.onDraw(canvas, bounds, currentTime);
        }

        @Override
        public void onDestroy()
        {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);

            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible)
        {
            super.onVisibilityChanged(visible);

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

                renderer.inAmbientMode(inAmbientMode, lowBitAmbient);

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