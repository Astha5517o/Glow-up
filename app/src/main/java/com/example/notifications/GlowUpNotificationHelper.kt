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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val notificationId = intent.getIntExtra(EXTRA_ID, 1001)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "ASTHA OS"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Know what matters. Do the next right thing."

        when (action) {
            ACTION_DISMISS -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
                return
            }
            ACTION_SNOOZE -> {
                NotificationManagerCompat.from(context).cancel(notificationId)
                // Snooze for 30 minutes
                val snoozeTimeMillis = System.currentTimeMillis() + 30 * 60 * 1000L
                GlowUpNotificationHelper.scheduleOneShotTimestampReminder(
                    context = context,
                    requestCode = notificationId + 50000,
                    triggerAtMillis = snoozeTimeMillis,
                    title = title,
                    message = message
                )
                return
            }
            else -> {
                GlowUpNotificationHelper.showNotification(
                    context = context,
                    notificationId = notificationId,
                    title = title,
                    message = message
                )
            }
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_ID = "extra_id"
        const val ACTION_SNOOZE = "com.example.notifications.ACTION_SNOOZE"
        const val ACTION_DISMISS = "com.example.notifications.ACTION_DISMISS"
    }
}

class BootReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            GlowUpNotificationHelper.createNotificationChannel(context)
            // Schedule gentle daily evening check-in & morning review reminders
            GlowUpNotificationHelper.scheduleDailyReminder(
                context = context,
                requestCode = 9101,
                hour24 = 7,
                minute = 15,
                title = "Good morning, Astha",
                message = "Take a calm look at today's plan and your first focus step."
            )
            GlowUpNotificationHelper.scheduleDailyReminder(
                context = context,
                requestCode = 9102,
                hour24 = 21,
                minute = 40,
                title = "Close Today Gently",
                message = "What felt meaningful today? Set 1 clear first step for tomorrow."
            )
        }
    }
}

object GlowUpNotificationHelper {
    const val CHANNEL_ID = "astha_os_gentle_channel"
    private const val CHANNEL_NAME = "ASTHA OS Life Calendar & Reminders"
    private const val CHANNEL_DESC = "Calm, non-spammy reminders for events, preparation milestones, study blocks, rest, and evening reflection."

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

        val snoozeIntent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_SNOOZE
            putExtra(ReminderBroadcastReceiver.EXTRA_ID, notificationId)
            putExtra(ReminderBroadcastReceiver.EXTRA_TITLE, title)
            putExtra(ReminderBroadcastReceiver.EXTRA_MESSAGE, message)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 10000,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            action = ReminderBroadcastReceiver.ACTION_DISMISS
            putExtra(ReminderBroadcastReceiver.EXTRA_ID, notificationId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 20000,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(0, "Start", pendingIntent)
            .addAction(0, "Snooze 30m", snoozePendingIntent)
            .addAction(0, "Dismiss", dismissPendingIntent)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (_: SecurityException) {
            // Ignored if permission was revoked
        }
    }

    fun scheduleOneShotTimestampReminder(
        context: Context,
        requestCode: Int,
        triggerAtMillis: Long,
        title: String,
        message: String
    ) {
        if (triggerAtMillis <= System.currentTimeMillis()) return
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
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun scheduleCalendarEventReminder(
        context: Context,
        eventId: Int,
        eventTitle: String,
        dateString: String,
        hour24: Int,
        minute: Int,
        preference: String
    ) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val parsedDate = try {
            sdf.parse(dateString)
        } catch (_: Exception) {
            null
        } ?: return

        val eventCal = Calendar.getInstance().apply {
            time = parsedDate
            set(Calendar.HOUR_OF_DAY, hour24.coerceIn(0, 23))
            set(Calendar.MINUTE, minute.coerceIn(0, 59))
            set(Calendar.SECOND, 0)
        }

        // Schedule reminder on the event day (or chosen time)
        scheduleOneShotTimestampReminder(
            context = context,
            requestCode = 40000 + eventId,
            triggerAtMillis = eventCal.timeInMillis,
            title = eventTitle,
            message = "Today's scheduled event ($preference). Open ASTHA OS to view your checklist."
        )

        // Also schedule 1-day-before calm preparation reminder at 6:00 PM if applicable
        if (preference.contains("Day before", ignoreCase = true) || preference.contains("prep", ignoreCase = true)) {
            val dayBeforeCal = (eventCal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, -1)
                set(Calendar.HOUR_OF_DAY, 18)
                set(Calendar.MINUTE, 0)
            }
            scheduleOneShotTimestampReminder(
                context = context,
                requestCode = 45000 + eventId,
                triggerAtMillis = dayBeforeCal.timeInMillis,
                title = "$eventTitle starts tomorrow",
                message = "Review your preparation checklist and pack your materials tonight."
            )
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
            "Event Prep",
            "NCSC starts in 2 days",
            "Want to rehearse your presentation for 15 minutes today?"
        ),
        GentleNotificationSample(
            2002,
            "My Day, My Way",
            "Today is planned as a Family Time Day",
            "Finish your key revision early so your evening stays free."
        ),
        GentleNotificationSample(
            2003,
            "Test Reminder",
            "Physics test tomorrow",
            "Review your formula sheet and 3 logged mistakes."
        ),
        GentleNotificationSample(
            2004,
            "End-of-Day Reflection",
            "Close Today Gently",
            "What felt meaningful today? Choose 1 clear first step for tomorrow."
        ),
        GentleNotificationSample(
            2005,
            "After School",
            "Welcome home",
            "Take 15 minutes to reset before your first focus block."
        ),
        GentleNotificationSample(
            2006,
            "Drona Coaching",
            "Drona Preparation",
            "Time to review notes before Drona class."
        ),
        GentleNotificationSample(
            2007,
            "Hydration & Posture",
            "Hydration break",
            "Take a moment for water and shoulder release."
        )
    )
}
