package com.zeitform.wasserapp

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.billingclient.api.*
import com.zeitform.wasserapp.billing.BillingConstants
import com.zeitform.wasserapp.internalfragments.*
import com.zeitform.wasserapp.location.LocationCheck
import com.zeitform.wasserapp.navfragments.*
import com.zeitform.wasserapp.prefmanagers.DataManager
import com.zeitform.wasserapp.viewmodel.SharedViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors

private const val PERMISSION_REQUEST = 10

class MainActivity : AppCompatActivity(), PurchasesUpdatedListener, HomeFragment.OnFragmentInteractionListener,
    WasserinfoFragment.OnFragmentInteractionListener,
    FaqFragment.OnFragmentInteractionListener,
    RechnerFragment.OnFragmentInteractionListener, KontaktFragment.OnFragmentInteractionListener, TippsHaerteFragment.OnFragmentInteractionListener,
TippsNitratFragment.OnFragmentInteractionListener, TippsFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener, KontaktSubFragment.OnFragmentInteractionListener{


    private lateinit var billingClient: BillingClient
    var hart: Int = 1
    var nitrat: Int = 1
    private lateinit var savedResponse: JSONObject
    private var savedCity: String? = null
    private var savedMax: String? = null
    private var savedNmax: String? = null
    private var savedDesc: String? = null
    private var isReducedSaved: Boolean = false
    private var sharedViewModel: SharedViewModel? = null
    private var dataManager: DataManager? = null
    private var jsonArray:JSONArray? = JSONArray()
    private var url: String? = null
    private var city: String? = null
    private var unit: String? = null
    private var land: String? = null
    private var faktor: Double? = 0.0
    private var desc: String? = null
    private var max: Double? = 0.0
    private var min: Double? = 0.0
    private var nmax: Double? = 0.0
    private var nmin: Double? = 0.0
    private var near: Int? = 0
    private var zip: String? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private lateinit var textMessage: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarText: TextView
    private lateinit var listHaerteItem: Array<String>
    private lateinit var listNitratItem: Array<String>
    private lateinit var wasserInfoList: Array<String>
    private lateinit var wasserInfoTitlebarText: Array<String>
    private lateinit var kontaktList: Array<String>
    private val fragment1: Fragment = HomeFragment()
    private val fragment2: Fragment = WasserinfoFragment()
    private val fragment3: Fragment = FaqFragment()
    private val fragment4: Fragment = RechnerFragment()
    private val fragment5: Fragment = KontaktFragment()
    private val fragmentHaerte: Fragment = TippsHaerteFragment()
    private val fragmentNitrat: Fragment = TippsNitratFragment()
    private val fragmentTipps: Fragment = TippsFragment()
    private val fragmentInfos: Fragment = InfoFragment()
    private val fragmentKontaktSub: Fragment = KontaktSubFragment()
    private lateinit var prevFragment: Fragment
    private val fm: FragmentManager = supportFragmentManager
    var active = fragment1


    override fun onFragmentInteraction(uri: Uri) {
        // TODO Implement
        System.out.println(uri.toString())
    }
    override fun openTippsHaerte() {
        if(sharedViewModel?.serverData?.value==null){
            createToast("Die Daten wurden noch nicht geladen.")
        } else {
            toolbarText.setText(R.string.title_tipps_haerte)
            fm.beginTransaction().hide(active).show(fragmentHaerte).commit()
            active =fragmentHaerte
        }
    }
    override fun openTippsNitrat() {
        if(sharedViewModel?.serverData?.value==null){
            createToast("Die Daten wurden noch nicht geladen.")
        } else {
            toolbarText.setText(R.string.title_tipps_nitrat)
            fm.beginTransaction().hide(active).show(fragmentNitrat).commit()
            active=fragmentNitrat
        }
    }
    override fun generateHaerteTipps(button: String) {
        Log.d("Tipps", "Härte"+listHaerteItem.indexOf(button)+" - "+hart)
        var index = listHaerteItem.indexOf(button)
        var hartArrayTitle:Array<String>
        var hartArrayContent:Array<String>
        when(hart){
            1 -> {
                hartArrayTitle= resources.getStringArray(R.array.hart_1_title)
                hartArrayContent= resources.getStringArray(R.array.hart_1_content)

                Log.d("Hart1","Hart1")
                Log.d("title",hartArrayTitle[index])
                Log.d("text",hartArrayContent[index])
                //toolbarText.text = hartArrayTitle[index]
                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(hartArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            2 -> {
                hartArrayTitle= resources.getStringArray(R.array.hart_2_title)
                hartArrayContent= resources.getStringArray(R.array.hart_2_content)

                Log.d("Hart2","Hart2")
                Log.d("title",hartArrayTitle[index])
                Log.d("text",hartArrayContent[index])
                //toolbarText.text = hartArrayTitle[index]
                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(hartArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            3 -> {
                hartArrayTitle= resources.getStringArray(R.array.hart_3_title)
                hartArrayContent= resources.getStringArray(R.array.hart_3_content)

                Log.d("Hart3","Hart3")
                Log.d("title",hartArrayTitle[index])
                Log.d("text",hartArrayContent[index])
                //toolbarText.text = hartArrayTitle[index]
                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(hartArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
        }


        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentTipps).commit()
        active=fragmentTipps
        //generate tipps page
    }
    override fun generateNitratTipps(button: String) {
        Log.d("Tipps", "Nitrat"+listNitratItem.indexOf(button)+" - "+nitrat)
        var index = listNitratItem.indexOf(button)
        var nitratArrayTitle:Array<String>
        var nitratArrayContent:Array<String>
        when(nitrat){
            1 -> {
                nitratArrayTitle= resources.getStringArray(R.array.nitrat_1_title)
                nitratArrayContent= resources.getStringArray(R.array.nitrat_1_content)

                Log.d("nitrat1","nitrat1")
                Log.d("title",nitratArrayTitle[index])
                Log.d("text",nitratArrayContent[index])
                //toolbarText.text = hartArrayTitle[index]
                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(nitratArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            2 -> {
                nitratArrayTitle= resources.getStringArray(R.array.nitrat_2_title)
                nitratArrayContent= resources.getStringArray(R.array.nitrat_2_content)

                Log.d("nitrat1","nitrat1")
                Log.d("title",nitratArrayTitle[index])
                Log.d("text",nitratArrayContent[index])
                //toolbarText.text = hartArrayTitle[index]
                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(nitratArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            3 -> {
                nitratArrayTitle= resources.getStringArray(R.array.nitrat_3_title)
                nitratArrayContent= resources.getStringArray(R.array.nitrat_3_content)

                Log.d("nitrat1","nitrat1")
                Log.d("title",nitratArrayTitle[index])
                Log.d("text",nitratArrayContent[index])
                //toolbarText.text = hartArrayTitle[index]
                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(nitratArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
        }
        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentTipps).commit()
        active=fragmentTipps
        //generate tipps page
    }
    override fun generateWasserinfos(button: String) {
        Log.d("Info", "Wasserinfo"+wasserInfoList.indexOf(button)+" - ")
        var index = wasserInfoList.indexOf(button)
        var infoTitleArray:Array<String> = resources.getStringArray(R.array.wasserinfos_titles)
        var infoContentArray:Array<String> = resources.getStringArray(R.array.wasserinfos_content)
        toolbarText.text = wasserInfoTitlebarText[index]
        sharedViewModel?.infoTitle?.postValue(HtmlCompat.fromHtml(infoTitleArray[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
        sharedViewModel?.infoContent?.postValue(HtmlCompat.fromHtml(infoContentArray[index], HtmlCompat.FROM_HTML_MODE_COMPACT))

        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentInfos).commit()
        active=fragmentInfos
    }
    override fun generateKontaktSubPage(button: String) {
        var index = kontaktList.indexOf(button)
        var kontaktTitleArray:Array<String> = resources.getStringArray(R.array.kontakt_titlebar_text)
        var kontaktContentArray:Array<String> = resources.getStringArray(R.array.kontakt_sub_content)
        toolbarText.text = kontaktTitleArray[index]
        sharedViewModel?.kontaktSubContent?.postValue(HtmlCompat.fromHtml(kontaktContentArray[index], HtmlCompat.FROM_HTML_MODE_COMPACT))

        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentKontaktSub).commit()
        active=fragmentKontaktSub
    }
    override fun onBackPressed() {
        Log.d("Back", "On back pressed!")
        if(active==fragmentHaerte || active == fragmentNitrat){
            fm.beginTransaction().hide(active).show(fragment1).commit()
            active = fragment1
            var city = sharedViewModel?.serverData?.value?.optString("city")
            toolbarText.text= getString(R.string.title_home)+city
        } else if(active == fragmentTipps|| active == fragmentInfos || active == fragmentKontaktSub){
            fm.beginTransaction().hide(active).show(prevFragment).commit()
            active = prevFragment
            if(active == fragment2){
                toolbarText.text = resources.getString(R.string.title_wasserinfos)
            } else if(active == fragment5){
                toolbarText.text = resources.getString(R.string.title_kontakt)
            }
        }else
        {
            super.onBackPressed()
        }

    }
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                //toolbar.setTitle(R.string.title_home)
                var city = sharedViewModel?.serverData?.value?.optString("city")
                toolbarText.text= getString(R.string.title_home)+city
                fm.beginTransaction().hide(active).show(fragment1).commit()
                active = fragment1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wasserinfo -> {
                textMessage.setText(R.string.title_wasserinfos)
                //toolbar.setTitle(R.string.title_wasserinfos)
                toolbarText.text = resources.getString(R.string.title_wasserinfos)
                fm.beginTransaction().hide(active).show(fragment2).commit()
                active = fragment2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_help -> {
                textMessage.setText(R.string.title_help)
                //toolbar.setTitle(R.string.title_help)
                toolbarText.text = resources.getString(R.string.title_help)
                fm.beginTransaction().hide(active).show(fragment3).commit()
                active = fragment3
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wasserbedarf -> {
                textMessage.setText(R.string.title_wasserbedarf)
                //toolbar.setTitle(R.string.title_wasserbedarf)
                toolbarText.text = resources.getString(R.string.title_wasserbedarf)
                fm.beginTransaction().hide(active).show(fragment4).commit()
                active = fragment4
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_kontakt -> {
                textMessage.setText(R.string.title_kontakt)
                //toolbar.setTitle(R.string.title_kontakt)
                toolbarText.text = resources.getString(R.string.title_kontakt)
                fm.beginTransaction().hide(active).show(fragment5).commit()
                active = fragment5
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBillingClient()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

        dataManager = DataManager(this)
        if (dataManager!!.savedData != null) {
            try {
                savedResponse = JSONObject(dataManager!!.savedData)
                savedCity = savedResponse.optString("city")
                savedMax = savedResponse.optString("max")
                savedNmax = savedResponse.optString("nmax")
                savedDesc = savedResponse.optString("desc")

                isReducedSaved = dataManager!!.isReduced
            } catch (e: JSONException) {
                Log.d("Exception", e.toString())
            }
        } else {
            Log.d("Saved data", "empty")
        }
        textMessage = findViewById(R.id.appbar_message)
        toolbar = findViewById(R.id.toolbar)
        toolbarText = findViewById(R.id.toolbar_text)
        //toolbar.setTitle(R.string.title_home)
        toolbarText.text = resources.getString(R.string.title_home)

        listHaerteItem = resources.getStringArray(R.array.haerte_tipps)
        listNitratItem = resources.getStringArray(R.array.nitrat_tipps)

        wasserInfoList = resources.getStringArray(R.array.wasserinfos_array)
        wasserInfoTitlebarText = resources.getStringArray(R.array.wasserinfos_titlebar_text)

        kontaktList = resources.getStringArray(R.array.kontakt_array)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fm.beginTransaction().add(R.id.main_container, fragmentKontaktSub, "9").hide(fragmentKontaktSub).commit()
        fm.beginTransaction().add(R.id.main_container, fragmentInfos, "8").hide(fragmentInfos).commit()
        fm.beginTransaction().add(R.id.main_container, fragmentTipps, "7").hide(fragmentTipps).commit()
        fm.beginTransaction().add(R.id.main_container, fragmentHaerte, "7").hide(fragmentHaerte).commit()
        fm.beginTransaction().add(R.id.main_container, fragmentNitrat, "6").hide(fragmentNitrat).commit()
        fm.beginTransaction().add(R.id.main_container, fragment5, "5").hide(fragment5).commit()
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit()
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit()

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        sharedViewModel!!.serverData.observe(this, Observer {
            it?.let {
                if(active == fragment1){
                    //toolbar.title = "Wassertipps für "+it.optString("city")
                    var city = it.optString("city")
                    toolbarText.text = getString(R.string.title_home)+city
                }
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                //getLocation()
                val locData = LocationCheck(this.applicationContext).checkLocation()
                val lat = locData.getLocation().latitude
                val lon = locData.getLocation().longitude
                Log.d("Lat - Lon :", " "+lat+" - "+lon+"")
                getData(lat, lon)
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            Toast.makeText(this, "Android < 6", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupBillingClient() {
        billingClient = BillingClient
            .newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult?) {
                if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
                    println("BILLING | startConnection | RESULT OK - 1")
                    loadProduct()
                } else {
                    println("BILLING | startConnection | RESULT: "+billingResult?.responseCode)
                }

            }
            override fun onBillingServiceDisconnected() {
                println("BILLING | onBillingServiceDisconnected | DISCONNECTED")
            }
        })
    }
    override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                    println(skuDetailsList)
                    //initProductAdapter(skuDetailsList)
                } else {
                    println("Can't querySkuDetailsAsync, responseCode: $responseCode")
                }
            }
        } else {
            println("Billing Client not ready")
        }
    }
    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    private fun parseJson(jsonResponse: String){
        jsonArray = JSONArray(jsonResponse) //has all the response
        Log.d("Response ",jsonArray!!.optJSONObject(0).toString())
        val city =jsonArray!!.optJSONObject(0).optString("city")
        val max = jsonArray!!.optJSONObject(0).optString("max")
        val nmax = jsonArray!!.optJSONObject(0).optString("nmax")
        val desc = jsonArray!!.optJSONObject(0).optString("desc")
        if(jsonArray!!.length()!=0){
            sharedViewModel?.completeData?.postValue(jsonArray) //saves the complete result
            if(savedCity!= null && savedCity == city && savedMax == max && savedNmax == nmax && savedDesc == desc){
                sharedViewModel?.serverData?.postValue(savedResponse) //use saved data
                sharedViewModel?.url?.postValue(savedResponse.optString("url")) //saves URL
                sharedViewModel?.isReduced?.postValue(isReducedSaved)
                Log.d("Saved data" ,"saved")
            } else {
                sharedViewModel?.serverData?.postValue(jsonArray!!.optJSONObject(0)) //saves only the first entry
                sharedViewModel?.url?.postValue(jsonArray!!.optJSONObject(0).optString("url")) //saves URL
                sharedViewModel?.isReduced?.postValue(false)
                Log.d("New data" ,"new")
            }
            //sharedViewModel?.isReduced?.postValue(false)
            Log.d("CITY:", city+","+unit+","+faktor+","+land+","+near+","+max+","+min+","+nmax+","+nmin+","+zip+","+url+","+desc)
            if(jsonArray!!.getJSONObject(0).getString("max").toDouble()==0.0){
                Log.d("Max = 0","Water hardness not available! Open no hardness page")
                //used the response in jsonArray to show up to 5 entries.
            } else {
                Log.d("Max!= 0","Water hardness available!")
                //show data
            }
        } else {
            //Show location popup
        }
    }
    fun createToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }
    private fun getData(latitude: Double?, longitude:Double?){
        Executors.newSingleThreadExecutor().execute {
            var responseText = ""
            try {
                responseText = URL("https://wassertipps.de/cgi-bin/server.pl?json_data={\"lon\":" + longitude + ",\"lat\":" + latitude + '}').readText()
            } catch (e: AccessDeniedException){
                Log.d("Data fetch failed", "Check internet connection or the server status.")
            }
            if(responseText!=""){
                Log.d("Result:", responseText)
                parseJson(responseText)
            } else {
                Log.d("Data fetch failed -2", "Check internet connection or the server status.")
            }
        }
    }
}

