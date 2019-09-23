package com.zeitform.wasserapp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.Spanned
import org.json.JSONArray
import org.json.JSONObject

class SharedViewModel: ViewModel() {
    val serverData = MutableLiveData<JSONObject>()
    val completeData = MutableLiveData<JSONArray>()
    val isReduced = MutableLiveData<Boolean>()
    //tippsFragment
    val tippsTitle = MutableLiveData<Spanned>()
    val tippsContent = MutableLiveData<Spanned>()
    //infoFragment
    val infoTitle = MutableLiveData<Spanned>()
    val infoContent = MutableLiveData<Spanned>()
    //kontaktSubFragment
    val kontaktSubContent = MutableLiveData<Spanned>()
    val url = MutableLiveData<String>()
}