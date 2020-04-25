package com.jueezy.spotify;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationListenerService extends android.service.notification.NotificationListenerService {
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF = "Data_Saved";

    private int adsCounter, songCounter;
    private boolean isMute, isKill;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        isMute = sharedPreferences.getBoolean("sw1", true);
        isKill = sharedPreferences.getBoolean("sw2", false);
        adsCounter = sharedPreferences.getInt("adsCounter", 0);
        songCounter = sharedPreferences.getInt("songCounter", 0);

        String pack = sbn.getPackageName();
        String currentSong = sbn.getNotification().extras.getCharSequence("android.text").toString();
        currentSong = sharedPreferences.getString("currentSong", "");
        String newSong = sbn.getNotification().extras.getCharSequence("android.text").toString();

        Log.d("DC", "Mute On ^_^");

        if (RootUtil.isDeviceRooted() && isKill) {

            if (pack.equals("com.spotify.music")) {
                {
                    Notification.Action[] actions = sbn.getNotification().actions;
                    if (actions.length == 3) {
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
            }
        }

        if (isMute) {
            /*Log.d("DC", "Mute On ^_^");
            Toast.makeText(this, "Mute Option", Toast.LENGTH_SHORT).show();*/
            if (pack.equals("com.spotify.music")) {

                if (newSong.equals(currentSong)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();     // play & Pause
                    editor.putString("currentSong", currentSong);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentSong", newSong);                      // Next & Previous
                    editor.putInt("songCounter", songCounter + 1);
                    editor.apply();
                }

                Notification.Action[] actions = sbn.getNotification().actions;
                if (actions.length == 3) {
                    Toast.makeText(NotificationListenerService.this, "Ad Appeared", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("adsCounter", adsCounter + 1);
                    editor.apply();
                    Muter.mute(getApplicationContext());
                } else
                    Muter.unMute(getApplicationContext());
            }
        }
    }

}
