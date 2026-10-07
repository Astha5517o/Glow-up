package com.example.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import java.util.Calendar

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "✨ GlowUp"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Take a gentle moment for yourself today."
        val notificationId = intent.getIntExtra(EXTRA_ID, 1001)

        GlowUpNotificationHelper.showNotification(
            context = context,
            notificationId = notificationId,
            title = title,
            message = message
        )
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_ID = "extra_id"
    }
}

object GlowUpNotificationHelper {
    const val CHANNEL_ID = "glowup_gentle_care_channel"
    private const val CHANNEL_NAME = "Gentle Self-Care Reminders"
    private const val CHANNEL_DESC = "Supportive, non-judgmental reminders for hydration, movement, skincare, and rest."

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ) {
        createNotificationChannel(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (_: SecurityException) {
            // Ignored if permission was revoked
        }
    }

    fun scheduleDailyReminder(
        context: Context,
        requestCode: Int,
        hour24: Int,
        minute: Int,
        title: String,
        message: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.EXTRA_ID, requestCode)
            putExtra(ReminderBroadcastReceiver.EXTRA_TITLE, title)
            putExtra(ReminderBroadcastReceiver.EXTRA_MESSAGE, message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour24)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (_: SecurityException) {
            // Fallback if alarm scheduling restricted
        }
    }

    fun cancelReminder(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    data class GentleNotificationSample(
        val id: Int,
        val category: String,
        val title: String,
        val body: String
    )

    val notificationSamples = listOf(
        GentleNotificationSample(
            2001,
            "Morning",
            "☀️ Good morning!",
            "Start your day with a little movement."
        ),
        GentleNotificationSample(
            2002,
            "Hydration",
            "💧 Hydration break",
            "Take a moment for some water."
        ),
        GentleNotificationSample(
            2003,
            "Movement",
            "🧘 Your 10-minute movement session is ready.",
            "Move at your own pace whenever you have a moment."
        ),
        GentleNotificationSample(
            2004,
            "Skincare",
            "✨ Evening glow routine",
            "A few minutes for yourself."
        ),
        GentleNotificationSample(
            2005,
            "Study",
            "📚 Study reset",
            "Stand up, relax your shoulders, and take a short break."
        ),
        GentleNotificationSample(
            2006,
            "Sleep",
            "🌙 Time to wind down",
            "Tomorrow starts with the rest you get tonight."
        ),
        GentleNotificationSample(
            2007,
            "Confidence",
            "🌸 One minute for yourself",
            "How did you feel today?"
        )
    )
}
