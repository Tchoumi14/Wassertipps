package com.zeitform.wasserapp.prefmanagers

import android.content.Context
import android.content.SharedPreferences

class AlarmDataManager(internal var _context: Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    // shared pref mode
    internal var PRIVATE_MODE = 0

    var alarmArrayString: String?
        get() = pref.getString(ALARM_DATA,"")
        set(dataJsonObj) {
            editor.putString(ALARM_DATA, dataJsonObj)
            editor.commit()
        }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {

        // Shared preferences file name
        private const val PREF_NAME = "wassertipps-AlarmData"

        private const val ALARM_DATA = "ALARM-data"
    }
}