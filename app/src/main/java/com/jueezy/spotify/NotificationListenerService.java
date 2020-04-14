package com.jueezy.spotify;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.v("DC", "received");

        String pack = sbn.getPackageName();

        if(pack.equals("com.spotify.music")){
            Notification.Action[] actions = sbn.getNotification().actions;
            if(actions.length == 3){
                ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                am.killBackgroundProcesses("com.spotify.music");

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent playSpotify = new Intent("com.spotify.mobile.android.ui.widget.NEXT");
                        playSpotify.setPackage("com.spotify.music");
                        sendBroadcast(playSpotify);
                    }
                });
            }
        }

        /*if(pack.equals("com.spotify.music")){
            Notification.Action[] actions = sbn.getNotification().actions;
            if(actions.length == 3)
                Muter.mute(getApplicationContext());
            else
                Muter.unMute(getApplicationContext());
        }*/
    }

}
