package com.zeitform.wasserapp.notif

import android.content.Context
import com.google.gson.Gson
import com.zeitform.wasserapp.prefmanagers.AlarmDataManager
import com.google.gson.reflect.TypeToken



object AlarmDataManagerHelper {

    fun saveToAlarmDataManager(context: Context, alarmArray: ArrayList<AlarmData>){
        var alarmDataManager = AlarmDataManager(context)
        var gson = Gson()
        var arrayToString = gson.toJson(alarmArray)
        alarmDataManager!!.alarmArrayString = arrayToString
    }

    fun getFromAlarmDataManager(context: Context): ArrayList<AlarmData>{
        var alarmDataManager = AlarmDataManager(context)
        var arrayAsString = alarmDataManager!!.alarmArrayString
        var gson = Gson()
        val type = object : TypeToken<ArrayList<AlarmData>>() {

        }.type
        return gson.fromJson(arrayAsString, type)
    }
    fun clearSavedData(context: Context){
        var alarmDataManager = AlarmDataManager(context)
        if(alarmDataManager.alarmArrayString!=""){
            alarmDataManager.alarmArrayString= ""
        }
    }
}