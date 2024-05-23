package com.derandecker.notificationsdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class TimeZoneBroadcastReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        notificationLauncher(context)
    }
}
