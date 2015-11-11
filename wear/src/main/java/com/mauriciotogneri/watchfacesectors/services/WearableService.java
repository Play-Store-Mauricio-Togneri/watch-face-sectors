package com.mauriciotogneri.watchfacesectors.services;

import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.mauriciotogneri.watchfacesectors.BroadcastApi.UpdateProfile;
import com.mauriciotogneri.watchfacesectors.MessageApi.Paths;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.Serializer;

public class WearableService extends WearableListenerService
{
    @Override
    public void onMessageReceived(MessageEvent messageEvent)
    {
        String path = messageEvent.getPath();
        byte[] payload = messageEvent.getData();

        if (TextUtils.equals(path, Paths.UPDATE_PROFILE))
        {
            Profile profile = Serializer.deserialize(payload);

            if (profile != null)
            {
                Intent intent = new Intent(UpdateProfile.ACTION);
                intent.putExtra(UpdateProfile.EXTRA_PROFILE, profile);
                sendBroadcast(intent);
            }
        }
    }
}