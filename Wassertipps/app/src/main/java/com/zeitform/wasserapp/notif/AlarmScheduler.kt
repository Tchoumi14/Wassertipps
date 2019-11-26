package com.zeitform.wasserapp.notif

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeitform.wasserapp.R
import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList


object AlarmScheduler {


    /**
     * Schedules all the alarms for [ReminderData].
     *
     * @param context      current application context
     * @param reminderData ReminderData to use for the alarm
     */
    fun scheduleAlarmsForReminder(context: Context, alarmData: AlarmData) {
        Log.d("At scheduler", "--")
        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = createPendingIntent(context, alarmData)
        scheduleAlarm(alarmIntent, alarmMgr, alarmData)
    }

    private fun scheduleAlarm(alarmIntent: PendingIntent, alarmMgr: AlarmManager, alarmData: AlarmData){
        // get the AlarmManager reference

        // Set up the time to schedule the alarm
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(HOUR_OF_DAY, alarmData.hour)
        datetimeToAlarm.set(MINUTE, alarmData.min)
        datetimeToAlarm.set(SECOND, 0)
        datetimeToAlarm.set(MILLISECOND, 0)
        Log.d("Scheduled for ", alarmData.hour.toString()+":"+alarmData.min)
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,datetimeToAlarm.timeInMillis,AlarmManager.INTERVAL_DAY, alarmIntent)
    }

    /**
     * Creates a [PendingIntent] for the Alarm using the [ReminderData]
     *
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     * @param day          String representation of the day
     */
    private fun createPendingIntent(context: Context, alarmData: AlarmData): PendingIntent {
        // create the intent using a unique type
        val intent = Intent(context, NotifReceiver::class.java).apply {
            action = context.getString(R.string.app_name)
            putExtra("ID", alarmData.id)
        }
        return PendingIntent.getBroadcast(context, alarmData.id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    /**
     * Removes all the notification if it was previously scheduled.
     * and clears the saved alarm data
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     */
    fun removeAlarmsForReminder(context: Context) {
        Log.d("removeAlarms", AlarmDataManagerHelper.getFromAlarmDataManager(context).size.toString())
        if(AlarmDataManagerHelper.getFromAlarmDataManager(context).size != 0){
            var alarmTimes = AlarmDataManagerHelper.getFromAlarmDataManager(context)
            println("Alarm times"+alarmTimes)
            for(alarmTime in alarmTimes){
                val intent = Intent(context, NotifReceiver::class.java).apply {
                    action = context.getString(R.string.app_name)
                    putExtra("ID", alarmTime.id)
                }
                var alarmIntent = PendingIntent.getBroadcast(context, alarmTime.id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                // Check PendintIntent exists
                if(PendingIntent.getBroadcast(context, alarmTime.id, intent, PendingIntent.FLAG_NO_CREATE) != null){
                    println("Intent before cancel :"+alarmTime.id)
                }
                val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmMgr.cancel(alarmIntent)
            }
        }


    }
}