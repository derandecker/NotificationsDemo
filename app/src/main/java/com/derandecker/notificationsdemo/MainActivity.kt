package com.derandecker.notificationsdemo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.derandecker.notificationsdemo.ui.theme.NotificationsDemoTheme

const val NOT_ID = 2


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        createNotificationChannel()
        setContent {
            NotificationsDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Button(
                        modifier = Modifier.padding(innerPadding),
                        onClick = { showNotification() }
                    ) {
                        Text(text = "Show notification")
                    }
                }
            }
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Log.d("Permission", "Notification permission not granted")

            }
        }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Main", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    var builder = NotificationCompat.Builder(this, "Main")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("This is the title")
        .setContentText("Here is some content for the notification")
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(
                    BitmapFactory.decodeResource(applicationContext.resources, R.drawable.dog)
                )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showNotification() {
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                when {
                    ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                    }

                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity, Manifest.permission.POST_NOTIFICATIONS
                    ) -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.
                    }

                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }
                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(NOT_ID, builder.build())

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotificationsDemoTheme {
        Greeting("Android")
    }
}
