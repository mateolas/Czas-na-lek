package com.studio.skyline.wezlek.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.studio.skyline.wezlek.extras.Util;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Util.scheduleAlarm(context);
    }
}
