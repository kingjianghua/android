package com.sincava.tech.android.framework.ui.passcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class PasscodeLockActivity extends AppCompatActivity {

    private static final String TAG = "PasscodeLockActivity";

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
