package com.example.appmedisync.app.screens.home.medicinas


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.app.AlarmManager
import android.content.pm.PackageManager
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.appmedisync.R
import java.util.Calendar
import kotlin.jvm.java

fun createNotificationChannel(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("medisync_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
    val intent = Intent(context, AlertDetails::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val ACTION_SNOOZE = "com.example.appmedisync.ACTION_SNOOZE"
    val EXTRA_NOTIFICATION_ID = "com.example.appmedisync.EXTRA_NOTIFICATION_ID"

    val snoozeIntent = Intent(context, MyBroadcastReceiver::class.java).apply {
        action = ACTION_SNOOZE
        putExtra(EXTRA_NOTIFICATION_ID, 0)
    }

    val snoozePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        snoozeIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, "medisync_channel")
        .setSmallIcon(R.drawable.logo_azul)
        .setContentTitle(titulo)
        .setContentText(mensaje)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.icn_snooze, context.getString(R.string.snooze),
            snoozePendingIntent)
        .setAutoCancel(true)

    val notificationId = titulo.hashCode()

    with(NotificationManagerCompat.from(context)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // El permiso no ha sido otorgado, puedes pedirlo desde la Activity.
            Log.e("NOTIF", "Permiso POST_NOTIFICATIONS no concedido")
            return@with
        }

        notify(notificationId, builder.build())
    }
}

fun scheduleNotification(context: Context, medicamento: Medicamento) {
    val timeParts = medicamento.hora.split(":")
    val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: return
    val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: return

    // 1. Establecer la hora exacta en el calendario
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        // Si ya pasó la hora, programa para el día siguiente
        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    // 2. Crear Intent hacia el BroadcastReceiver
    val intent = Intent(context, MyBroadcastReceiver::class.java).apply {
        putExtra("titulo", medicamento.nombre)
        putExtra("mensaje", "Es hora de tomar tu ${medicamento.nombre}")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        medicamento.id.hashCode(), // único por medicamento
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // 3. Calcular la frecuencia en milisegundos
    val frecuenciaEnMillis = medicamento.frecuencia * 24 * 60 * 60 * 1000L

    // 4. Programar notificación repetitiva
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        frecuenciaEnMillis,
        pendingIntent
    )

    Log.d("SCHEDULE", "Notificación agendada para ${medicamento.nombre} a las $hour:$minute cada ${medicamento.frecuencia} días")

}





