package com.zeitform.wasserapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.Spanned
import com.android.billingclient.api.Purchase
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


    //Purchase data
    val purchaseData = MutableLiveData<MutableList<Purchase>?>()  //MutableLiveData of type 'MutableList<Purchase>'

}