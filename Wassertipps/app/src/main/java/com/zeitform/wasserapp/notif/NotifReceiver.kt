package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotifReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("At notifReceiver", "--")
        if(context != null){
            NotificationHelper.createNotification(context)
        }
    }
}