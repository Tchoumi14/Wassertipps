package com.zeitform.wasserapp.notif

data class AlarmData(var hour: Int, var min: Int) {

    fun getTimeData(): AlarmData {
        return AlarmData(hour, min)
    }
}