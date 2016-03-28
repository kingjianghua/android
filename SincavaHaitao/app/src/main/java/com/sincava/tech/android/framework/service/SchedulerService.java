package com.sincava.tech.android.framework.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class SchedulerService extends IntentService {

    public static final String LOG_TAG = "SchedulerService";

    public SchedulerService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "Starting scheduled action service");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
        wakeLock.acquire();
    }
}
