package com.zeitform.wasserapp.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receives the BOOT_COMPLETED action and schedules the alarms after reboot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Reschedule every alarm here
    }

}