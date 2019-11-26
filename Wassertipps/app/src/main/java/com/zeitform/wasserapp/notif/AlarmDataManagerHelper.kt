package com.zeitform.wasserapp.notif

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.zeitform.wasserapp.prefmanagers.AlarmDataManager
import com.google.gson.reflect.TypeToken



object AlarmDataManagerHelper {

    fun saveToAlarmDataManager(context: Context, alarmArray: ArrayList<AlarmData>){
        val alarmDataManager = AlarmDataManager(context)
        val gson = Gson()
        val arrayToString = gson.toJson(alarmArray)
        alarmDataManager.alarmArrayString = ""
        alarmDataManager.alarmArrayString = arrayToString
    }

    fun getFromAlarmDataManager(context: Context): ArrayList<AlarmData>{
        val alarmDataManager = AlarmDataManager(context)
        val arrayAsString = alarmDataManager.alarmArrayString
        val gson = Gson()
        val type = object : TypeToken<ArrayList<AlarmData>>() {

        }.type
        return gson.fromJson(arrayAsString, type)
    }
    fun clearSavedAlarmDataManager(context: Context){
        val alarmDataManager = AlarmDataManager(context)
        alarmDataManager.alarmArrayString= ""
        Log.d("clear saved data",alarmDataManager.alarmArrayString )
    }
}