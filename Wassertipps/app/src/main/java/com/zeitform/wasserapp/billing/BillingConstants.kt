package com.zeitform.wasserapp.billing

import java.util.Arrays.asList
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.BillingClient
import java.util.*


object BillingConstants {
    // SKUs for our products: the pro (non-consumable).
    //Add more variables if needed
    val SKU_PRO = "com.zeitform.wasserapp.wassertipps_product"

    //add the variables to this array
    private val IN_APP_SKUS = arrayOf(SKU_PRO)

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    fun getSkuList(): List<String> {
           return IN_APP_SKUS.toList()
    }
}