package com.zeitform.wasserapp.prefmanagers

import android.content.Context
import android.content.SharedPreferences

class RechnerDataManager(internal var _context: Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    // shared pref mode
    internal var PRIVATE_MODE = 0

    var gewicht: String?
        get() = pref.getString(GEWICHT,"70")
        set(gewichtData) {
            editor.putString(GEWICHT, gewichtData)
            editor.commit()
        }
    var alter: String?
        get() = pref.getString(ALTER,"29")
        set(alterData) {
            editor.putString(ALTER, alterData)
            editor.commit()
        }

    var sport: Boolean
        get() = pref.getBoolean(SPORT,false)
        set(sportData) {
            editor.putBoolean(SPORT, sportData)
            editor.commit()
        }

    var stillendefrauen: Boolean
        get() = pref.getBoolean(STILLENDE_FRAUEN,false)
        set(stillendefrauenData) {
            editor.putBoolean(STILLENDE_FRAUEN, stillendefrauenData)
            editor.commit()
        }

    var wasserProTag: String?
        get() = pref.getString(WATER_PRO_TAG,"0")
        set(waterProTagData) {
            editor.putString(WATER_PRO_TAG, waterProTagData)
            editor.commit()
        }

    var aufwachen: String?
        get() = pref.getString(AUFWACHEN_ZEIT,"00 : 00")
        set(aufwachenData) {
            editor.putString(AUFWACHEN_ZEIT, aufwachenData)
            editor.commit()
        }

    var einschlafen: String?
        get() = pref.getString(EINSCHLAFEN_ZEIT,"00 : 00")
        set(einschlafenData) {
            editor.putString(EINSCHLAFEN_ZEIT, einschlafenData)
            editor.commit()
        }
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {

        // Shared preferences file name
        private const val PREF_NAME = "wassertipps-RechnerData"

        private const val GEWICHT= "gewicht-data"
        private const val ALTER = "alter-data"
        private const val SPORT = "sport-data"
        private const val STILLENDE_FRAUEN = "stillendefrauen-data"
        private const val WATER_PRO_TAG = "WaterProTag-data"
        private const val AUFWACHEN_ZEIT = "aufwachen-data"
        private const val EINSCHLAFEN_ZEIT = "einschlafen-data"
    }

}