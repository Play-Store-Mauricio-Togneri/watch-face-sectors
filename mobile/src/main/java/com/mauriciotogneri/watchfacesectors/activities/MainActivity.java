package com.mauriciotogneri.watchfacesectors.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mauriciotogneri.watchfacesectors.MessageApi.Paths;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.R;
import com.mauriciotogneri.watchfacesectors.Serializer;
import com.mauriciotogneri.watchfacesectors.WearableConnectivity;
import com.mauriciotogneri.watchfacesectors.WearableConnectivity.OnDeviceNodeDetected;
import com.mauriciotogneri.watchfacesectors.WearableConnectivity.WearableEvents;
import com.mauriciotogneri.watchfacesectors.colorpicker.ColorPicker;
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

        //View view = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        //View view = getWindow().getDecorView().findViewById(android.R.id.content);
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
                    // TODO
                    //Toast.makeText(MainActivity.this, "NOT CONNECTED!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectedFail()
    {
        // TODO
        Toast.makeText(this, "NOT CONNECTED!", Toast.LENGTH_SHORT).show();
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