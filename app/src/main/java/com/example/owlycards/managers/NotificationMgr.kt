package com.example.owlycards.managers

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.owlycards.MainApplication
import com.example.owlycards.R
import com.example.owlycards.data.Owly
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Notification manager manages the notifications sent by Owlycards
 */
class NotificationMgr(private val context: Context) {

    /**
     * Create reminders creates the reminders. Can be run on startup as old reminders are overwritten
     */
    fun createReminders() {

        // Check if the user has allowed notifications
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Yes? Create reminder
            createNotificationChannel() // Create notification channel
            // Create reoccurring reminder
            createReoccurringWork<NotificationWorker>(7) // Repeat every 7 days
        }
    }

    /**
     * Helper that creates a notification channel.
     */
    private fun createNotificationChannel() {
        // Check minimum version S
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Setup channel
            val channel = NotificationChannel("owly_channel_1", "Owly notification channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Reminders from Owly to come practice."
            }

            // Register channel
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Creates a reoccurring worker with the given interval. Note, no delay after creation.
     */
    private inline fun <reified T: Worker> createReoccurringWork(repeatIntervalDays: Long) {
        // Setup
        val uploadWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<T>(repeatIntervalDays, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true) // Require battery to not be low
                    .build()
            )
            .build()

        // Register
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }
}

/**
 * Worker class
 */
class NotificationWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    /**
     * Runs when WorkManager initiates it
     */
    override fun doWork(): Result {

        // Init data
        val app = applicationContext as MainApplication
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val timeSinceLastClose = Calendar.getInstance().timeInMillis - app.lastClosed.timeInMillis

        // Check if
        //  - app is not in foreground,
        //  - hour is between 16:00 and 20:59 and
        //  - minimum 24 hours since the user last used the app
        if (!app.inForeground && currentHour in 16..20 && timeSinceLastClose > 1000*60*60*24) {

            // Setup notification
            val builder = NotificationCompat.Builder(context, "owly_channel_1")
                .setSmallIcon(R.drawable.owly) // Owly icon
                .setContentTitle("Message from Owly!") // Title
                .setContentText(Owly("").remind()) // Message
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priority

            val notificationManager = NotificationManagerCompat.from(context)
            // Double-check that the user still allows push notifications
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationManager.notify(1001, builder.build()) // Send notification
            }

            return Result.success()
        }
        return Result.retry() // Retry if checks failed
    }
}