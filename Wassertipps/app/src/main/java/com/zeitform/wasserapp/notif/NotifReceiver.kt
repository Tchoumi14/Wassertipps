package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeitform.wasserapp.prefmanagers.AlarmDataManager

class NotifReceiver: BroadcastReceiver() {

    private lateinit var alarmDataManager: AlarmDataManager
    override fun onReceive(context: Context, intent: Intent?) {
        var stringID = intent?.extras!!.getInt("ID")
        Log.d("At notifReceiver", stringID.toString())
        //alarmDataManager = AlarmDataManager(context)
        if(context != null){
            NotificationHelper.createNotification(context, stringID.toString())
        }
    }
}