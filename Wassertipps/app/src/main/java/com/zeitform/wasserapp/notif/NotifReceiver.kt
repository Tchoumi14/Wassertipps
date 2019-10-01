package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeitform.wasserapp.prefmanagers.AlarmDataManager
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager

class NotifReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        var stringID = intent?.extras!!.getInt("ID")
        Log.d("At notifReceiver", stringID.toString())
        var alarmArray = AlarmDataManagerHelper.getFromAlarmDataManager(context)
        if(alarmArray[alarmArray.size-1].id == stringID){
            Log.d("Last intent id", stringID.toString())
            Log.d("Last entry", alarmArray[alarmArray.size-1].id.toString())
            AlarmDataManagerHelper.clearSavedData(context)
            RechnerDataManager(context).mitteilungenSwitch = false
        }
        if(context != null){
            NotificationHelper.createNotification(context, stringID.toString())
        }
    }
}