package com.zeitform.wasserapp.notif

data class AlarmData(var id: Int, var hour: Int, var min: Int, var waterMl: Int) {

    fun getTimeData(): AlarmData {
        return AlarmData(id, hour, min, waterMl)
    }
}