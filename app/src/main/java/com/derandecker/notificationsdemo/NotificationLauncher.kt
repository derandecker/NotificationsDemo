package com.derandecker.notificationsdemo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getString

const val NOTIFICATION_ID = 2
const val channelId = "Main"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun notificationLauncher(context: Context) {

    val name = getString(context, R.string.channel_name)
    val descriptionText = getString(context, R.string.channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelId, name, importance).apply {
        description = descriptionText
    }
    val notificationManager: NotificationManager =
        context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)

    val intent = Intent(ACTION_LOCALE_SETTINGS)
    val pendingIntent: PendingIntent =
        getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

    Log.d("Intent", intent.toString())
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.car_icon)
        .setContentTitle("TIME ZONE CHANGED")
        .setContentIntent(pendingIntent)
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText("New time zone. Change system language?"))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(R.drawable.ic_launcher_background, "Open Locale Settings", pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(NOTIFICATION_ID, builder.build())
    }
}
