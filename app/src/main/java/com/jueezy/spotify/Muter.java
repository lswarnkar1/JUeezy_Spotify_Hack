package com.jueezy.spotify;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

public class Muter {

    static void mute(Context context){
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamMute(AudioManager.STREAM_MUSIC, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
        else
            manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    static void unMute(Context context){
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
        else
            manager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

}
