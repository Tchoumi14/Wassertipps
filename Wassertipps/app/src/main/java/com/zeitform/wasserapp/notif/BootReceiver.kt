package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager

/**
 * Receives the BOOT_COMPLETED action and schedules the alarms after reboot.
 */
class BootReceiver : BroadcastReceiver() {

    private lateinit var rechnerDataManager: RechnerDataManager
    override fun onReceive(context: Context, intent: Intent?) {
        // Reschedule every alarm here
        Log.d("Device reboot", "Completed")
        rechnerDataManager = RechnerDataManager(context)
        val alarmTimes = AlarmDataManagerHelper.getFromAlarmDataManager(context)
        if(alarmTimes.size!=0 && rechnerDataManager.mitteilungenSwitch){
            for(alarmTime in alarmTimes){
                AlarmScheduler.scheduleAlarmsForReminder(context, alarmTime)
            }
        }
    }

}