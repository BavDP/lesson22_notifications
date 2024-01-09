package com.example.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private var reqPermissionNotifierLauncher: ActivityResultLauncher<String>? = null
    private val btn: Button by lazy {
        findViewById(R.id.notifyBtn)
    }

    object Constants {
        const val CHANNEL_ID = "test_notifications"
        const val CHANNEL_NAME = "channel name"
        const val CHANNEL_DESC = "channel description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reqPermissions()
        btn.setOnClickListener {
            reqPermissionNotifierLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun initNotifications() {
        createNotificationChannel()
    }

    private fun reqPermissions() {
        reqPermissionNotifierLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
                    if (isGranted) {
                        initNotifications()
                        showNotification("dfgdfg")
                    } else {
                        // TODO: explain why app need such permission
                    }
            }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.CHANNEL_NAME
            val descriptionText = Constants.CHANNEL_DESC
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(text: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            var builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("my title")
                .setContentText("my content")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            NotificationManagerCompat.from(this).notify(1, builder.build())
        }
    }
}