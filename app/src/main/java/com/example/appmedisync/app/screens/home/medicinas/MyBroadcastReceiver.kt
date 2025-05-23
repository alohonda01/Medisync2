package com.example.appmedisync.app.screens.home.medicinas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.appmedisync.ACTION_SNOOZE") {
            val notificationId = intent.getIntExtra("com.example.appmedisync.EXTRA_NOTIFICATION_ID", 0)

            // Aquí haces lo que quieras al posponer (ej: reprogramar)
            Log.d("SNOOZE", "Posponiendo notificación $notificationId")

            // También puedes cancelar la notificación si lo deseas
            NotificationManagerCompat.from(context).cancel(notificationId)
        }
    }
}
