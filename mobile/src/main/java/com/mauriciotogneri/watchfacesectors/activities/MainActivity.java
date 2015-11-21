package com.mauriciotogneri.watchfacesectors.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.mauriciotogneri.watchfacesectors.MessageApi.Paths;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.R;
import com.mauriciotogneri.watchfacesectors.Serializer;
import com.mauriciotogneri.watchfacesectors.colorpicker.ColorPicker;
import com.mauriciotogneri.watchfacesectors.utils.WearableConnectivity;
import com.mauriciotogneri.watchfacesectors.utils.WearableConnectivity.OnDeviceNodeDetected;
import com.mauriciotogneri.watchfacesectors.utils.WearableConnectivity.WearableEvents;
import com.mauriciotogneri.watchfacesectors.view.MainView;
import com.mauriciotogneri.watchfacesectors.view.MainViewObserver;

public class MainActivity extends Activity implements WearableEvents, MainViewObserver
{
    private String[] nodeIds = new String[0];
    private MainView mainView = null;
    private WearableConnectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_main);

        connectivity = new WearableConnectivity(this, this);
        connectivity.connect();

        View view = findViewById(android.R.id.content);
        mainView = new MainView(view, this);
    }

    @Override
    public void onConnectedSuccess()
    {
        connectivity.getDefaultDeviceNode(new OnDeviceNodeDetected()
        {
            @Override
            public void onDevicesDetected(String[] nodeIdsList)
            {
                if (nodeIdsList.length != 0)
                {
                    nodeIds = nodeIdsList;
                }
                else
                {
                    onConnectedFail();
                }
            }
        });
    }

    @Override
    public void onConnectedFail()
    {
        showToast(getString(R.string.main_error_connectionFail));
    }

    private void showToast(final String message)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (connectivity != null)
        {
            connectivity.disconnect();
        }
    }

    @Override
    public void onUpdate(Profile profile)
    {
        for (String nodeId : nodeIds)
        {
            connectivity.sendMessage(nodeId, Paths.UPDATE_PROFILE, Serializer.serialize(profile), null);
        }
    }

    @Override
    public void onChooseColor(int initialColor, ColorPicker.ColorPickerCallback callback)
    {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.show(this, initialColor, callback);
    }
}