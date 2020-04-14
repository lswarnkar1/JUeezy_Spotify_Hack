package com.jueezy.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView rootDescription;
        Switch sw1, sw2;

        rootDescription = (TextView) findViewById(R.id.root_description);

        if (RootUtil.isDeviceRooted()) {
            Toast.makeText(this, "Your Decice is Rooted",LENGTH_LONG ).show();
        } else {
            Toast.makeText(this, "Your Decice is Non-Rooted", LENGTH_LONG).show();
            rootDescription.setText("Your Devive is Non-Rooted");
        }
        /*if(!isNotificationServiceEnabled())
            startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));*/
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}