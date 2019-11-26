package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.zeitform.wasserapp.R
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
        Log.d("At notifRec :intid:", intentId.toString())
        Log.d("At notifRec :timeId:", timeId.toString())
        if(intentId >= timeId - 2 && intentId <= timeId){
            Log.d("Time id", timeId.toString())
            //Log.d("intent id", intentId.toString())
            val alarmArray = AlarmDataManagerHelper.getFromAlarmDataManager(context)
            if(alarmArray.size!=0){
                for(alarmData in alarmArray){
                    if(alarmData.id == intentId){
                        val notificationTextArray = context.resources.getStringArray(R.array.notification_text)
                        var notificationText = "Du solltest jetzt wieder ein Glas Wasser trinken"
                        if(alarmArray.indexOf(alarmData)!=0){
                            val index = (Math.floor(Math.random() * (alarmArray.size - 1) + 0)).toInt()
                            if(index < notificationTextArray.size){
                                notificationText = notificationTextArray[index]
                            }
                        }
                        NotificationHelper.createNotification(context, notificationText, alarmData.waterMl)
                    }
                }
            } else {
                Log.d("Array null ", "at notifReceiver ")
            }
        }
    }

}