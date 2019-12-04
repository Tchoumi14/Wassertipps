package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import com.zeitform.wasserapp.R
import java.time.LocalDateTime
import java.util.*
import java.lang.ref.WeakReference


class NotifReceiver: BroadcastReceiver() {
    private lateinit var weakContext: WeakReference<Context>
    override fun onReceive(context: Context, intent: Intent?) {
        weakContext = WeakReference(context)
        val pendingResult: PendingResult = goAsync()
        val asyncTask = Task(pendingResult, weakContext, intent)
        asyncTask.execute()

        /*val intentId = intent?.extras!!.getInt("ID")
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
        }*/
    }


    private class Task(private val pendingResult: PendingResult, private val weakContext: WeakReference<Context>, private val intent: Intent?
    ) : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {
            println("Started!!")
            val intentId = intent?.extras!!.getInt("ID")
            var context = weakContext.get()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val hour = current.hour
                val min = current.minute
                val timeId = Integer.parseInt(String.format("%02d", hour)+""+String.format("%02d", min))
                if (context != null) {
                    checkNotificationValidity(context, timeId, intentId)
                } else {
                    Log.d("Error","Context null")
                }
            } else {
                val date = Calendar.getInstance(Locale.getDefault())
                val hour = date.get(Calendar.HOUR_OF_DAY)
                val min = date.get(Calendar.MINUTE)
                val timeId = Integer.parseInt(String.format("%02d", hour)+""+String.format("%02d", min))
                if (context != null) {
                    checkNotificationValidity(context, timeId, intentId)
                } else {
                    Log.d("Error","Context null")
                }
            }
            return "true"
        }
        private fun checkNotificationValidity(context: Context, timeId: Int, intentId: Int){
            Log.d("At notifRec :intid:", intentId.toString())
            Log.d("At notifRec :timeId:", timeId.toString())
            if(intentId >= timeId - 4 && intentId <= timeId){
                Log.d("Time id", timeId.toString())
                //Log.d("intent id", intentId.toString())
                val alarmArray = AlarmDataManagerHelper.getFromAlarmDataManager(context)
                if(alarmArray.size!=0){
                    for(alarmData in alarmArray){
                        if(alarmData.id == intentId){
                            val notificationTextArray = context?.resources?.getStringArray(R.array.notification_text)
                            var notificationText = "Du solltest jetzt wieder ein Glas Wasser trinken"
                            if(alarmArray.indexOf(alarmData)!=0){
                                val index = (Math.floor(Math.random() * (alarmArray.size - 1) + 0)).toInt()
                            if(index < notificationTextArray!!.size){
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
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Must call finish() so the BroadcastReceiver can be recycled.
            println("Finished!!")
            pendingResult.finish()
        }
    }



}