package com.mauriciotogneri.watchfacesectors.services;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearableService extends WearableListenerService
{
    @Override
    public void onMessageReceived(MessageEvent messageEvent)
    {
        String nodeId = messageEvent.getSourceNodeId();
        String path = messageEvent.getPath();
        byte[] payload = messageEvent.getData();

        Toast.makeText(this, "RECEIVED: " + path, Toast.LENGTH_SHORT).show();
    }
}