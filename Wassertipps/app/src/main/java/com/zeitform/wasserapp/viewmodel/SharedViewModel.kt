package com.zeitform.wasserapp.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.Spanned
import com.android.billingclient.api.Purchase
import com.zeitform.wasserapp.location.LocationData
import org.json.JSONArray
import org.json.JSONObject

class SharedViewModel: ViewModel() {
    val serverData = MutableLiveData<JSONObject>()
    val completeData = MutableLiveData<JSONArray>()
    val isReduced = MutableLiveData<Boolean>()

    val locationData = MutableLiveData<LocationData>()
    val isInternetUnavailable = MutableLiveData<Boolean>()
    val isOutsideDeutschland = MutableLiveData<Boolean>()

    //tippsFragment
    val tippsTitle = MutableLiveData<Spanned>()
    val tippsContent = MutableLiveData<String>()
    //infoFragment
    val infoTitle = MutableLiveData<Spanned>()
    val infoContent = MutableLiveData<Spanned>()
    //kontaktSubFragment
    val kontaktSubContent = MutableLiveData<Spanned>()
    val url = MutableLiveData<String>()


    //Purchase data
    val purchaseData = MutableLiveData<MutableList<Purchase>?>()  //MutableLiveData of type 'MutableList<Purchase>'

}