package com.zeitform.wasserapp.viewmodel

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.android.billingclient.api.Purchase
import com.zeitform.wasserapp.billing.BillingManager

class BillingViewModel(application: Application): AndroidViewModel(application) {

    /*val purchaseData: LiveData<MutableList<Purchase>>

    private val billingManager: BillingManager
    init {
        billingManager = BillingManager.getInstance(application)
        billingManager.setupBillingClient()
        purchaseData =
    }

    fun setUpBillingClient(){
        billingManager.setupBillingClient()
    }
    fun initiateBillingFlow(activity: Activity){
        billingManager.initiatePurchaseFlow(activity, billingManager.productList[0])
    }*/
}