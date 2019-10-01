package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeitform.wasserapp.prefmanagers.AlarmDataManager
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager

class NotifReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if(context != null){
            var intentId = intent?.extras!!.getInt("ID")
            Log.d("At notifReceiver", intentId.toString())
            val alarmArray = AlarmDataManagerHelper.getFromAlarmDataManager(context)
            for(alarmData in alarmArray){
                if(alarmData.id == intentId){
                    NotificationHelper.createNotification(context, alarmData.waterMl)
                }
            }
            checkEndOfAlarms(context, alarmArray, intentId)
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