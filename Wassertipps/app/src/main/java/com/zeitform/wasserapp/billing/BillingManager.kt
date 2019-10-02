package com.zeitform.wasserapp.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.zeitform.wasserapp.navfragments.HomeFragment

class BillingManager(var context: Activity): PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private var mIsServiceConnected: Boolean = false
    private var BASE_64_ENCODED_PUBLIC_KEY: String = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjy0WC/xn5aBjn/iaqZfjVxCJ4brNva7A8VCajk8bExnUsqsBMgHnznb7RuhPHSPoliUiXUAwMekfiiRUuKoipf7dwSiup7eawr+px95m40lHIumb+mcrbUpfPJ2nxJqryja8lDRyu/2qb0BYHsn7NrOj8oOkZmtk3pPMMWO2JJ2LgXzZn2uE/SYkbr1E4cTgMAPLs96R1LzXtuw3lFwiLAqCLx8icf2YPgXdiv7N/7mDl+dhgR44A/GjxpaSpwmNomg2d6EddU5IUwd47BFkhdquiEptvCSS/WF7fJs6KgUZnP5hoXZvEQuf/FSuLZuPbls5eDgR9dZDdbkXNK9QAQIDAQAB"

    var productList: ArrayList<SkuDetails> = ArrayList()

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
                    loadProduct()
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

    private fun loadProduct(){
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
                    for(skuDetail in skuDetailsList){
                        productList.add(skuDetail)
                    }
                    //var f = fragment1 as HomeFragment
                    //f.initProductData(skuDetailsList)
                } else {
                    println("Can't querySkuDetailsAsync, responseCode: $responseCode")
                }
            }
        } else {
            println("Billing Client not ready")
        }
    }

    fun initiatePurchaseFlow(skuDetails: SkuDetails){
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(context, flowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        println("onPurchase")
    }

}