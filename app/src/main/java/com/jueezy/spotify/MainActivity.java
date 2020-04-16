package com.jueezy.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String SHARED_PREF = "Data_Saved";
    TextView rootDescription;
    LinearLayout leftLayout, rightLayout;
    SharedPreferences sharedPreferences;
    View view;
    Switch sw1, sw2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootDescription = (TextView) findViewById(R.id.root_description);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        view = (View) findViewById(R.id.view);
        sw1 = (Switch) findViewById(R.id.muter);
        sw2 = (Switch) findViewById(R.id.killer);

        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        if (RootUtil.isDeviceRooted()) {
            Toast.makeText(this, "Your Decice is Rooted", LENGTH_LONG).show();
            rootDescription.setText("Your Devive is Rooted");
        } else {
            Toast.makeText(this, "Your Decice is Non-Rooted", LENGTH_LONG).show();
            rootDescription.setText("Your Devive is Non-Rooted");
            /*rightLayout.setVisibility(View.GONE);
            view.setVisibility(View.GONE);*/
        }

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("sw1", true);
                    editor.apply();
                    sw1.setChecked(true);
                    Toast.makeText(MainActivity.this, "Muter On", Toast.LENGTH_SHORT).show();
                    if (sw2.isChecked()) {
                        sw2.setChecked(false);
                    }
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("sw1", false);
                    editor.apply();
                    sw1.setChecked(false);
                }
                if (!isNotificationServiceEnabled()) {
                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("sw2", true);
                    editor.apply();
                    sw2.setChecked(true);
                    Toast.makeText(MainActivity.this, "Killer On", Toast.LENGTH_SHORT).show();
                    if (sw1.isChecked()) {
                        sw1.setChecked(false);
                    }
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("sw2", false);
                    editor.apply();
                    sw2.setChecked(false);
                }
                if (!isNotificationServiceEnabled()) {
                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        sw1.setChecked(sharedPreferences.getBoolean("sw1", true));
        sw2.setChecked(sharedPreferences.getBoolean("sw2", false));
        if (sw1.isChecked()) {
            sw1.setChecked(sharedPreferences.getBoolean("sw1", true));
            sw2.setChecked(sharedPreferences.getBoolean("sw2", false));
        }
        if (sw2.isChecked()) {
            sw2.setChecked(sharedPreferences.getBoolean("sw2", true));
            sw1.setChecked(sharedPreferences.getBoolean("sw1", false));
        }
        if (!isNotificationServiceEnabled()) {
            sw1.setChecked(false);
            sw2.setChecked(false);
        } else {

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
    };
}