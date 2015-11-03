package com.mauriciotogneri.watchfacesectors;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi.SendMessageResult;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WearableConnectivity
{
    private final GoogleApiClient apiClient;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    private static final int TIMEOUT = 1000 * 10; // in milliseconds

    public WearableConnectivity(Context context, final WearableEvents wearableEvents)
    {
        this.apiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(new ConnectionCallbacks()
        {
            @Override
            public void onConnected(Bundle connectionHint)
            {
                wearableEvents.onConnectedSuccess();
            }

            @Override
            public void onConnectionSuspended(int cause)
            {
                wearableEvents.onConnectedFail();
            }
        }).addOnConnectionFailedListener(new OnConnectionFailedListener()
        {
            @Override
            public void onConnectionFailed(ConnectionResult result)
            {
                wearableEvents.onConnectedFail();
            }
        }).addApi(Wearable.API).build();
    }

    public synchronized void connect()
    {
        apiClient.connect();
    }

    public synchronized void disconnect()
    {
        apiClient.disconnect();
    }

    public synchronized void sendMessage(final String nodeId, final String path, final byte[] payload, final ResultCallback<SendMessageResult> callback)
    {
        threadPool.execute(new Runnable()
        {
            @Override
            public void run()
            {
                apiClient.blockingConnect(TIMEOUT, TimeUnit.MILLISECONDS);

                try
                {
                    PendingResult<SendMessageResult> pendingResult = Wearable.MessageApi.sendMessage(apiClient, nodeId, path, payload);

                    if (callback != null)
                    {
                        pendingResult.setResultCallback(callback);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized void getDefaultDeviceNode(final OnDeviceNodeDetected onDeviceNodeDetected)
    {
        threadPool.execute(new Runnable()
        {
            @Override
            public void run()
            {
                apiClient.blockingConnect(TIMEOUT, TimeUnit.MILLISECONDS);

                try
                {
                    NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(apiClient).await();

                    for (Node node : result.getNodes())
                    {
                        if (node.isNearby())
                        {
                            onDeviceNodeDetected.onDefaultDeviceNode(node.getId());
                            return;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                onDeviceNodeDetected.onDefaultDeviceNode(null);
            }
        });
    }

    public interface WearableEvents
    {
        void onConnectedSuccess();

        void onConnectedFail();
    }

    public interface OnDeviceNodeDetected
    {
        void onDefaultDeviceNode(String nodeId);
    }
}