package com.zeitform.wasserapp.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.zeitform.wasserapp.MainActivity
import com.zeitform.wasserapp.R

object NotificationHelper {

    private const val ADMINISTER_REQUEST_CODE = 2019

    /**
     * Sets up the notification channels for API 26+.
     * Note: This uses package name + channel name to create unique channelId's.
     *
     * @param context     application context
     * @param importance  importance level for the notificaiton channel
     * @param showBadge   whether the channel should have a notification badge
     * @param name        name for the notification channel
     * @param description description for the notification channel
     */
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Creates a notification for [ReminderData] with a full notification tap and a single action.
     *
     * @param context      current application context
     * @param reminderData ReminderData for this notification
     */

    fun createNotification(context: Context, id: String) {

        // create a group notification
        //val groupBuilder = buildGroupNotification(context)

        // create the pet notification
        val notificationBuilder = buildNotificationForWasserbedarf(context, id)

        // add an action to the pet notification
        val administerPendingIntent = createPendingIntentForAction(context)
        //notificationBuilder.addAction(R.drawable.app_icon, context.getString(R.string.app_name), administerPendingIntent)
        notificationBuilder.setContentIntent(administerPendingIntent)
        // call notify for both the group and the pet notification
        val notificationManager = NotificationManagerCompat.from(context)
        //notificationManager.notify(2, groupBuilder.build())
        notificationManager.notify(1, notificationBuilder.build())
    }

    /**
     * Builds and returns the [NotificationCompat.Builder] for the group notification for a wasserbedarfrechner.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun buildGroupNotification(context: Context): NotificationCompat.Builder {
        val channelId = "${context.packageName}-wasserbedarf"
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notifications_black_24dp)
            setContentTitle(context.getString(R.string.notification_title))
            setContentText(context.getString(R.string.group_notification_for, "wasserbedarf"))
            setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.group_notification_for, "wasserbedarf")))
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(context.getString(R.string.notification_group))
        }
    }

    /**
     * Builds and returns the NotificationCompat.Builder for the Wasserbedarf notification.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun buildNotificationForWasserbedarf(context: Context, id: String): NotificationCompat.Builder {


        val channelId = "${context.packageName}-wasserbedarf"

        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.notification_template_icon_bg)
            setContentTitle(context.getString(R.string.notification_title))
            setAutoCancel(true)

            // get a drawable reference for the LargeIcon

            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.app_icon))
            setContentText("Trink ---ml Wasser!. ID is: "+id)
            setGroup("wasserbedarf")

            // note is not important so if it doesn't exist no big deal
           /* if (reminderData.note != null) {
                setStyle(NotificationCompat.BigTextStyle().bigText(reminderData.note))
            } */

            // Launches the app to open the reminder edit screen when tapping the whole notification
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                //putExtra(ReminderDialog.KEY_ID, reminderData.id)
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
    }

    /**
     * Creates the pending intent for the Administered Action for the pet notification.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun createPendingIntentForAction(context: Context): PendingIntent? {

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return PendingIntent.getActivity(context, 0, intent, 0)
        /*
            Create an Intent to update the ReminderData if Administer action is clicked
         */
        /*val administerIntent = Intent(context, AppGlobalReceiver::class.java).apply {
            action = context.getString(R.string.action_medicine_administered)
            putExtra(AppGlobalReceiver.NOTIFICATION_ID, reminderData.id)
            putExtra(ReminderDialog.KEY_ID, reminderData.id)
            putExtra(ReminderDialog.KEY_ADMINISTERED, true)
        }

        return PendingIntent.getBroadcast(context, ADMINISTER_REQUEST_CODE, administerIntent, PendingIntent.FLAG_UPDATE_CURRENT)*/
    }
}
