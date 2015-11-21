package com.mauriciotogneri.watchfacesectors.services;

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
import android.view.SurfaceHolder;

import com.mauriciotogneri.watchfacesectors.Preferences;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.broadcast.BroadcastApi.UpdateProfile;
import com.mauriciotogneri.watchfacesectors.ui.Renderer;

import java.lang.ref.WeakReference;

public class WatchFaceService extends CanvasWatchFaceService
{
    private static final int UPDATE_RATE_INTERACTIVE_MODE = 100; // in milliseconds
    private static final int UPDATE_RATE_AMBIENT_MODE = 1000 * 60; // in milliseconds

    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine()
    {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine
    {
        private final Handler updateTimeHandler = new EngineHandler(this);

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

            final Preferences preferences = Preferences.getInstance(WatchFaceService.this);
            renderer = new Renderer(preferences.getProfile());

            IntentFilter filter = new IntentFilter(UpdateProfile.ACTION);
            registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    Profile profile = (Profile) intent.getSerializableExtra(UpdateProfile.EXTRA_PROFILE);
                    preferences.saveProfile(profile);

                    onProfileUpdated(profile);
                }
            }, filter);
        }

        private synchronized void onProfileUpdated(Profile profile)
        {
            renderer.updateProfile(profile);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds)
        {
            renderer.onDraw(canvas, bounds, isInAmbientMode());
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

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        @Override
        public void onPropertiesChanged(Bundle properties)
        {
            super.onPropertiesChanged(properties);

            lowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode)
        {
            super.onAmbientModeChanged(inAmbientMode);

            renderer.inAmbientMode(inAmbientMode, lowBitAmbient);

            invalidate();

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

            if (isVisible())
            {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Handle updating the time periodically.
         */
        private void handleUpdateTimeMessage()
        {
            invalidate();

            if (isInAmbientMode())
            {
                sendDelayedMessage(UPDATE_RATE_AMBIENT_MODE);
            }
            else
            {
                sendDelayedMessage(UPDATE_RATE_INTERACTIVE_MODE);
            }
        }

        private void sendDelayedMessage(int updateRate)
        {
            long timeMs = System.currentTimeMillis();
            long delayMs = updateRate - (timeMs % updateRate);
            updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
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