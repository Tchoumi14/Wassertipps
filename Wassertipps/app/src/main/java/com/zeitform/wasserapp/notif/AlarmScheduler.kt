package com.zeitform.wasserapp.notif

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeitform.wasserapp.R
import java.util.*
import java.util.Calendar.*


object AlarmScheduler {


    /**
     * Schedules all the alarms for [ReminderData].
     *
     * @param context      current application context
     * @param reminderData ReminderData to use for the alarm
     */
    fun scheduleAlarmsForReminder(context: Context) {
        Log.d("At scheduler", "--")
        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Schedule the alarms based on the days to administer the medicine
       /* val days = context.resources.getStringArray(R.array.days)
        if (reminderData.days != null) {
            for (index in reminderData.days!!.indices) {

                val day = reminderData.days!![index]
                if (day != null) {

                    // get the PendingIntent for the alarm
                    val alarmIntent = createPendingIntent(context, reminderData, day)

                    // schedule the alarm
                    val dayOfWeek = getDayOfWeek(days, day)
                    scheduleAlarm(reminderData, dayOfWeek, alarmIntent, alarmMgr)
                }
            }
        } */
        val alarmIntent = createPendingIntent(context)
        scheduleAlarm(alarmIntent, alarmMgr)
    }

    private fun scheduleAlarm(alarmIntent: PendingIntent, alarmMgr: AlarmManager){
        // get the AlarmManager reference

        // Set up the time to schedule the alarm
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(HOUR_OF_DAY, 16)
        datetimeToAlarm.set(MINUTE, 24)
        datetimeToAlarm.set(SECOND, 0)
        datetimeToAlarm.set(MILLISECOND, 0)
        datetimeToAlarm.set(DAY_OF_WEEK, 1)
        Log.d("Scheduled!", "--")
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,datetimeToAlarm.timeInMillis,24*60*60*1000, alarmIntent)
        return
    }

    /**
     * Creates a [PendingIntent] for the Alarm using the [ReminderData]
     *
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     * @param day          String representation of the day
     */
    private fun createPendingIntent(context: Context): PendingIntent {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, NotifReceiver::class.java).apply {
            action = context.getString(R.string.app_name)
            //type = "$day-${reminderData.name}-${reminderData.medicine}-${reminderData.type.name}"
            //putExtra(ReminderDialog.KEY_ID, reminderData.id)
        }

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}