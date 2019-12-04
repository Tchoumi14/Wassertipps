package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager
import java.lang.ref.WeakReference

/**
 * Receives the BOOT_COMPLETED action and schedules the alarms after reboot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        if(intent?.action.equals(Intent.ACTION_LOCKED_BOOT_COMPLETED) || intent?.action.equals(Intent.ACTION_BOOT_COMPLETED) ||
            intent?.action.equals("android.intent.action.QUICKBOOT_POWERON") || intent?.action.equals("com.htc.intent.action.REBOOT")){
            val rechnerDataManager: RechnerDataManager
            println("Wasserapp - Device reboot Completed || Intent action :"+intent?.action)
            rechnerDataManager = RechnerDataManager(context!!)
            val alarmTimes = AlarmDataManagerHelper.getFromAlarmDataManager(context)
            if(alarmTimes.size!=0 && rechnerDataManager.mitteilungenSwitch){
                println("Saved Alarms :"+alarmTimes)
                for(alarmTime in alarmTimes){
                    AlarmScheduler.scheduleAlarmsForReminder(context, alarmTime)
                }
            }

        } else {
            println("Unrecognised boot action- Wasserapp")
            val rechnerDataManager: RechnerDataManager
            println("Wasserapp - Device reboot Completed || Intent action :"+intent?.action)
            rechnerDataManager = RechnerDataManager(context!!)
            val alarmTimes = AlarmDataManagerHelper.getFromAlarmDataManager(context)
            if(alarmTimes.size!=0 && rechnerDataManager.mitteilungenSwitch){
                println("Saved Alarms :"+alarmTimes)
                for(alarmTime in alarmTimes){
                    AlarmScheduler.scheduleAlarmsForReminder(context, alarmTime)
                }
            }
        }

    }


}