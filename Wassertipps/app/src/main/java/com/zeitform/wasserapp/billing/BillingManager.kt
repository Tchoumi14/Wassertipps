package com.zeitform.wasserapp.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.zeitform.wasserapp.navfragments.HomeFragment

class BillingManager(var context: Activity): PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private var mIsServiceConnected: Boolean = false

    fun setupBillingClient() {
        billingClient = BillingClient
            .newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult?) {
                if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
                    println("BILLING | startConnection | RESULT OK - 1")
                    mIsServiceConnected = true
                } else {
                    println("BILLING | startConnection | RESULT: "+billingResult?.responseCode)
                }

            }
            override fun onBillingServiceDisconnected() {
                println("BILLING | onBillingServiceDisconnected | DISCONNECTED")
                mIsServiceConnected = false
            }
        })
    }

    fun loadProduct(): MutableList<SkuDetails>{
        var skuDetails: MutableList<SkuDetails> = ArrayList()
        if (billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(BillingConstants.getSkuList())
                .setType(BillingClient.SkuType.INAPP)
                .build()
            billingClient.querySkuDetailsAsync(params) { responseCode, skuDetailsList ->
                if (responseCode.responseCode == BillingClient.BillingResponseCode.OK) {
                    println("querySkuDetailsAsync, responseCode: $responseCode")
                    //println(skuDetailsList)
                    skuDetails = skuDetailsList
                    //var f = fragment1 as HomeFragment
                    //f.initProductData(skuDetailsList)
                } else {
                    println("Can't querySkuDetailsAsync, responseCode: $responseCode")
                }
            }
        } else {
            println("Billing Client not ready")
        }
        return skuDetails
    }

    fun initiatePurchaseFlow(skuDetails: SkuDetails){
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(context, flowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}