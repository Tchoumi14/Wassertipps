package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager
import java.time.Clock
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class NotifReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
            val intentId = intent?.extras!!.getInt("ID")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val hour = current.hour
            val min = current.minute
            val timeId = Integer.parseInt(String.format("%02d", hour)+""+String.format("%02d", min))
            checkNotificationValidity(context, timeId, intentId)
        } else {
            val date = Calendar.getInstance(Locale.getDefault())
            val hour = date.get(Calendar.HOUR_OF_DAY)
            val min = date.get(Calendar.MINUTE)
            val timeId = Integer.parseInt(String.format("%02d", hour)+""+String.format("%02d", min))
            checkNotificationValidity(context, timeId, intentId)
        }
    }

    private fun checkNotificationValidity(context: Context, timeId: Int, intentId: Int){
        Log.d("At notifReceiver", intentId.toString())
        Log.d("Time id", timeId.toString())
        Log.d("intent id", intentId.toString())
        if(intentId >= timeId - 10 && intentId <= timeId){
            val alarmArray = AlarmDataManagerHelper.getFromAlarmDataManager(context)
            if(alarmArray.size!=0){
                for(alarmData in alarmArray){
                    if(alarmData.id == intentId){
                        NotificationHelper.createNotification(context, alarmData.waterMl)
                    }
                }
                checkEndOfAlarms(context, alarmArray, intentId)
            } else {
                Log.d("Array null ", "at notifReceiver ")
            }
        }
    }
    /**
     * Checks if the broadcast received is from the last intent (last notification).
     * If yes, clears the saved alarm data and turns the Mitteilung switch off.
     * @param context
     * @param alarmArray - saved alarm data
     * @param intentId   - id of the intent
     */
    private fun checkEndOfAlarms(context: Context, alarmArray: ArrayList<AlarmData>, intentId: Int){
        if(alarmArray[alarmArray.size-1].id == intentId){
            Log.d("Last intent id", intentId.toString())
            Log.d("Last entry", alarmArray[alarmArray.size-1].id.toString())
            AlarmDataManagerHelper.clearSavedData(context)
            RechnerDataManager(context).mitteilungenSwitch = false
        }
    }

}