package com.zeitform.wasserapp.notif

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager
import android.widget.Toast
import java.util.logging.Handler


class BootJobIntentService: JobIntentService() {


    //private lateinit var context: Context
    private val mHandler: android.os.Handler = android.os.Handler()
    companion object{
        val JOB_ID = 0x01
        fun enqueueWork(context: Context, work: Intent) {
            NotificationHelper.createNotification(context, "Test notification enqueue", 0)
            enqueueWork(context, BootJobIntentService::class.java, JOB_ID, work)
        }
}
    override fun onHandleWork(intent: Intent) {
        val rechnerDataManager: RechnerDataManager
        println("Wasserapp - Device reboot Completed || Intent action :"+intent.action)
        rechnerDataManager = RechnerDataManager(this@BootJobIntentService)
        val alarmTimes = AlarmDataManagerHelper.getFromAlarmDataManager(this@BootJobIntentService)
        if(alarmTimes.size!=0 && rechnerDataManager.mitteilungenSwitch){
            println("Saved Alarms :"+alarmTimes)
            for(alarmTime in alarmTimes){
                AlarmScheduler.scheduleAlarmsForReminder(this@BootJobIntentService, alarmTime)
            }
        }
        //NotificationHelper.createNotification(this@BootJobIntentService, "Boot complete!", 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Boot complete!" , "complete")
        println("Boot complete!")
        //showToast("Boot complete!")
    }
    // Helper for showing tests
    fun showToast(text: CharSequence) {
        mHandler.post(Runnable { Toast.makeText(this@BootJobIntentService, text, Toast.LENGTH_SHORT).show() })
    }
}