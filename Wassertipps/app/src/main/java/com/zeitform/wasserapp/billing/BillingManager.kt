package com.zeitform.wasserapp.billing

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.Purchase.PurchasesResult
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.zeitform.wasserapp.viewmodel.SharedViewModel
import java.io.IOException
import kotlin.properties.Delegates


class BillingManager(var activity: Activity): PurchasesUpdatedListener {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var billingClient: BillingClient
    private var mIsServiceConnected: Boolean = false
    var mPurchases: MutableList<Purchase>? = ArrayList()
    private var BASE_64_ENCODED_PUBLIC_KEY: String = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjy0WC/xn5aBjn/iaqZfjVxCJ4brNva7A8VCajk8bExnUsqsBMgHnznb7RuhPHSPoliUiXUAwMekfiiRUuKoipf7dwSiup7eawr+px95m40lHIumb+mcrbUpfPJ2nxJqryja8lDRyu/2qb0BYHsn7NrOj8oOkZmtk3pPMMWO2JJ2LgXzZn2uE/SYkbr1E4cTgMAPLs96R1LzXtuw3lFwiLAqCLx8icf2YPgXdiv7N/7mDl+dhgR44A/GjxpaSpwmNomg2d6EddU5IUwd47BFkhdquiEptvCSS/WF7fJs6KgUZnP5hoXZvEQuf/FSuLZuPbls5eDgR9dZDdbkXNK9QAQIDAQAB"

    var productList: ArrayList<SkuDetails> = ArrayList()

    interface InterfaceRefreshList {
        fun refreshListRequest()
    }
    var refreshListListeners = ArrayList<InterfaceRefreshList>()
    // fires off every time value of the property changes
    var isProPurchased: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        // do your stuff here
        refreshListListeners.forEach {
            it.refreshListRequest()
        }
    }
    fun setViewModel(sharedViewModel: SharedViewModel){
        this.sharedViewModel = sharedViewModel
    }
    fun setupBillingClient() {
        billingClient = BillingClient
            .newBuilder(activity)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        startServiceConnection(Runnable {
            mIsServiceConnected = true
            loadProduct()
            queryPurchases()
        })

    }
    private fun startServiceConnection(executeOnSuccess: Runnable){
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult?) {
                if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
                    println("BILLING | startConnection | RESULT OK - 1")
                    executeOnSuccess.run()
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
        val queryToExecute = Runnable {
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
        executeServiceRequest(queryToExecute)
    }

    fun initiatePurchaseFlow(skuDetails: SkuDetails){
        val purchaseFlowRequest = Runnable {
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            println("Purchase flow response :"+responseCode.responseCode)
            queryPurchases()
        }
        executeServiceRequest(purchaseFlowRequest)
    }

    fun queryPurchases(){
        val queryToExecute = Runnable {
            val time = System.currentTimeMillis()
            val purchasesResult = billingClient.queryPurchases(SkuType.INAPP)
            Log.i(TAG, "Querying purchases elapsed time: " + (System.currentTimeMillis() - time) + "ms")
           if (purchasesResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "purchase code OK")
            } else {
                Log.w(TAG, "queryPurchases() got an error response code: " + purchasesResult.getResponseCode())
            }
            onQueryPurchasesFinished(purchasesResult)
        }

        executeServiceRequest(queryToExecute)
    }
    /**
     * Handle a result from querying of purchases and report an updated list to the listener
     */
    private fun onQueryPurchasesFinished(result: PurchasesResult) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.w(
                TAG, "Billing client was null or result code (" + result.responseCode
                        + ") was bad - quitting"
            )
            return
        }

        Log.d(TAG, "Query inventory was successful.")

        // Update the UI and purchases inventory with new list of purchases

        onPurchasesUpdated(result.billingResult, result.purchasesList)
    }
    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        println("onPurchase")
        println("Billing result :"+billingResult)
        println("purchases made :"+purchases)
        if(billingResult!!.responseCode == BillingClient.BillingResponseCode.OK) {
            if (purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                    //consumeAsync(purchase.purchaseToken)
                }
            }
            //mBillingUpdatesListener.onPurchasesUpdated(mPurchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping")
        } else {
            Log.w(TAG, "onPurchasesUpdated() got unknown resultCode: "+billingResult!!.responseCode)
        }
    }

    /**
     * Handles the purchase
     *
     * Note: Notice that for each purchase, we check if signature is valid on the client.
     * It's recommended to move this check into your backend.
     * See [Security.verifyPurchase]
     *
     * @param purchase Purchase to be handled
     */
    private fun handlePurchase(purchase: Purchase) {
        if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
            Log.i(TAG, "Got a purchase: $purchase; but signature is bad. Skipping...")
            return
        }

        Log.d(TAG, "Got a verified purchase: $purchase")
        mPurchases?.clear()
        mPurchases?.add(purchase)
        if(purchase.sku==BillingConstants.SKU_PRO){
            setIsProPurchased(true)
        } else {
            setIsProPurchased(false)
        }
    }
    /**
     * Runs the thread
     */
    private fun executeServiceRequest(runnable: Runnable) {
        if (mIsServiceConnected) {
            runnable.run()
            println("Running a thread.")
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            println("Reconnecting to billing.")
            startServiceConnection(runnable)
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     *
     * Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     *
     */
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (BASE_64_ENCODED_PUBLIC_KEY.contains("CONSTRUCT_YOUR")) {
            throw RuntimeException("Please update your app's public key at: " + "BASE_64_ENCODED_PUBLIC_KEY")
        }

        try {
            return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature)
        } catch (e: IOException) {
            Log.e(TAG, "Got an exception trying to validate a purchase: $e")
            return false
        }

    }
    private fun consumeAsync(purchaseToken: String){
        // Generating Consume Response listener
        val onConsumeListener = object : ConsumeResponseListener {
            override fun onConsumeResponse(billingResult: BillingResult?, purchaseToken: String?) {
                println("Consumed!! :"+billingResult.toString())
                print("Consumed token :"+purchaseToken)
            }

        }

        // Creating a runnable from the request to use it inside our connection retry policy below
        val consumeRequest = Runnable {
            // Consume the purchase async
            val consumeParams = ConsumeParams.newBuilder()
                consumeParams.setPurchaseToken(purchaseToken)
                consumeParams.setDeveloperPayload("Testing consume.")
            billingClient.consumeAsync(consumeParams.build(), onConsumeListener)
        }

        executeServiceRequest(consumeRequest)
    }

    /**
     * Set true if pro version is purchased (SKU: com.zeitform.wasserapp.wassertipps_product )
     */
    private fun setIsProPurchased(value : Boolean){
        isProPurchased = value
    }
}