package com.jueezy.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    TextView rootDescription;
    LinearLayout leftLayout, rightLayout;
    Switch sw1, sw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rootDescription = (TextView) findViewById(R.id.root_description);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        sw1 = (Switch) findViewById(R.id.muter);
        sw2 = (Switch) findViewById(R.id.killer);


        if (RootUtil.isDeviceRooted()) {
            Toast.makeText(this, "Your Decice is Rooted", LENGTH_LONG).show();
            rootDescription.setText("Your Devive is Rooted");
        } else {
            Toast.makeText(this, "Your Decice is Non-Rooted", LENGTH_LONG).show();
            rootDescription.setText("Your Devive is Non-Rooted");
            /*rightLayout.setVisibility(View.GONE);*/
        }

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
            if (!isNotificationServiceEnabled()) {
                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
              }
                    } else {

                }
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isNotificationServiceEnabled()) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                } else {

                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!isNotificationServiceEnabled()){
            sw1.setChecked(false);
            sw2.setChecked(false);
        }else {
            sw1.setChecked(true);
            sw2.setChecked(true);
        }
    }

    private boolean isNotificationServiceEnabled() {
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