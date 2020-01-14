package com.zeitform.wasserapp.prefmanagers

import android.content.Context
import android.content.SharedPreferences

class DataManager(internal var _context: Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    // shared pref mode
    internal var PRIVATE_MODE = 0

    var savedData: String?
        get() = pref.getString(SAVED_DATA,"")
        set(dataJsonObj) {
            editor.putString(SAVED_DATA, dataJsonObj)
            editor.commit()
        }
    var isReduced: Boolean
        get() = pref.getBoolean(IS_REDUCED,false)
        set(value) {
            editor.putBoolean(IS_REDUCED, value)
            editor.commit()
        }

    //suche page
    var favlistData: String?
        get() = pref.getString(FAV_DATA,"")
        set(dataJsonArr) {
            editor.putString(FAV_DATA, dataJsonArr)
            editor.commit()
        }
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {

        // Shared preferences file name
        private const val PREF_NAME = "wassertipps-savedData"

        private const val SAVED_DATA = "saved-data"
        private const val IS_REDUCED = "saved-isreduced"

        private const val FAV_DATA = "favorite-list-data"
    }

}