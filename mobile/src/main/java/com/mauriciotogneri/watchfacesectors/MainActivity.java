package com.mauriciotogneri.watchfacesectors;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.mauriciotogneri.watchfacesectors.WearableConnectivity.OnDeviceNodeDetected;
import com.mauriciotogneri.watchfacesectors.WearableConnectivity.WearableEvents;

public class MainActivity extends Activity implements WearableEvents
{
    private String nodeId = "";
    private WearableConnectivity connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        connectivity = new WearableConnectivity(this, this);
        connectivity.connect();
    }

    @Override
    public void onConnectedSuccess()
    {
        connectivity.getDefaultDeviceNode(new OnDeviceNodeDetected()
        {
            @Override
            public void onDefaultDeviceNode(String deviceNodeId)
            {
                if (!TextUtils.isEmpty(deviceNodeId))
                {
                    nodeId = deviceNodeId;

                    connectivity.sendMessage(deviceNodeId, "/test", null, null);
                }
                else
                {
                    //                    Handler handler = new Handler(Looper.getMainLooper());
                    //                    handler.post(new Runnable()
                    //                    {
                    //                        @Override
                    //                        public void run()
                    //                        {
                    //                            view.displayData(new ArrayList<Stop>());
                    //                            view.toast(R.string.error_connection);
                    //                        }
                    //                    });
                }
            }
        });
    }

    @Override
    public void onConnectedFail()
    {
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
}