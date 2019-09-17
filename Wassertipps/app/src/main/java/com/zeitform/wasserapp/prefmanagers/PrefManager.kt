package com.zeitform.wasserapp.prefmanagers

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Lincoln on 05/05/16.
 */
class PrefManager(internal var _context: Context) {
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    // shared pref mode
    internal var PRIVATE_MODE = 0

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {

        // Shared preferences file name
        private const val PREF_NAME = "wassertipps-welcome"

        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }

}