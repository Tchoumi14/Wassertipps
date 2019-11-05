package com.zeitform.wasserapp

import android.Manifest
import android.content.DialogInterface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.text.HtmlCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.billingclient.api.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.zeitform.wasserapp.billing.BillingConstants
import com.zeitform.wasserapp.billing.BillingManager
import com.zeitform.wasserapp.internalfragments.*
import com.zeitform.wasserapp.location.LocationCheck
import com.zeitform.wasserapp.navfragments.*
import com.zeitform.wasserapp.notif.NotificationHelper
import com.zeitform.wasserapp.prefmanagers.DataManager
import com.zeitform.wasserapp.viewmodel.SharedViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import java.net.URL
import java.util.concurrent.Executors



private const val PERMISSION_REQUEST = 10
const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

class MainActivity : AppCompatActivity(), HomeFragment.OnFragmentInteractionListener,
    WasserinfoFragment.OnFragmentInteractionListener,
    FaqFragment.OnFragmentInteractionListener,
    RechnerFragment.OnFragmentInteractionListener, KontaktFragment.OnFragmentInteractionListener, TippsHaerteFragment.OnFragmentInteractionListener,
TippsNitratFragment.OnFragmentInteractionListener, TippsFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener, KontaktSubFragment.OnFragmentInteractionListener{

    private var doubleBackToExitPressedOnce = false
    private var datenschutzText: String = "Daten konnten nicht geladen werden. Bitte überprüfen Sie Ihre Internetverbindung."
    private var nutzungsbedingungText:String = "Daten konnten nicht geladen werden. Bitte überprüfen Sie Ihre Internetverbindung."
    private lateinit var mInterstitialAd: InterstitialAd
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
    private lateinit var navView: BottomNavigationView
    private lateinit var textMessage: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarText: TextView
    private lateinit var listHaerteItem: Array<String>
    private lateinit var listNitratItem: Array<String>
    private lateinit var wasserInfoList: Array<String>
    private lateinit var wasserInfoTitlebarText: Array<String>
    private lateinit var kontaktList: Array<String>
    private lateinit var billingManager: BillingManager
    private var fragment1: Fragment = HomeFragment()
    private var fragment2: Fragment = WasserinfoFragment()
    private var fragment3: Fragment = FaqFragment()
    private var fragment4: Fragment = RechnerFragment()
    private var fragment5: Fragment = KontaktFragment()
    private val fragmentHaerte: Fragment = TippsHaerteFragment()
    private val fragmentNitrat: Fragment = TippsNitratFragment()
    private val fragmentTipps: Fragment = TippsFragment()
    private val fragmentInfos: Fragment = InfoFragment()
    private val fragmentKontaktSub: Fragment = KontaktSubFragment()
    private lateinit var prevFragment: Fragment
    private val fm: FragmentManager = supportFragmentManager
    var active = fragment1
    private val mHandler: Handler = Handler()


    override fun onFragmentInteraction(uri: Uri) {
        System.out.println(uri.toString())
    }
    override fun setBillingManager(billingManager: BillingManager) {
        //println("Billing manager set at main")
        this.billingManager = billingManager
    }
    override fun getBillingManager(): BillingManager {
        return billingManager
    }
    override fun updateRechnerStatus(isVisible: Boolean) {
        println("At update rechner")
        val f = fm.findFragmentByTag("4") as RechnerFragment
        f.updateVisibility(isVisible)
        updateRechnerNavIcon(isVisible) //update rechner bottomnav icon
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
        var index = listHaerteItem.indexOf(button)
        var hartArrayContent:Array<String>
        when(hart){
            1 -> {
                hartArrayContent= resources.getStringArray(R.array.hart_1_content)

                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(hartArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            2 -> {
                hartArrayContent= resources.getStringArray(R.array.hart_2_content)

                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(hartArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            3 -> {
                hartArrayContent= resources.getStringArray(R.array.hart_3_content)

                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(hartArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
        }


        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentTipps).commit()
        active=fragmentTipps
        //generate tipps page
    }
    override fun generateNitratTipps(button: String) {
        val index = listNitratItem.indexOf(button)
        val nitratArrayContent:Array<String>
        when(nitrat){
            1 -> {
                nitratArrayContent= resources.getStringArray(R.array.nitrat_1_content)

                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(nitratArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            2 -> {
                nitratArrayContent= resources.getStringArray(R.array.nitrat_2_content)

                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(nitratArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
            3 -> {
                nitratArrayContent= resources.getStringArray(R.array.nitrat_3_content)

                sharedViewModel?.tippsContent?.postValue(HtmlCompat.fromHtml(nitratArrayContent[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
            }
        }
        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentTipps).commit()
        active=fragmentTipps
        //generate tipps page
    }
    override fun generateWasserinfos(buttonBox: String) {
        //Log.d("Info", "Wasserinfo"+wasserInfoList.indexOf(buttonBox)+" - ")
        val index = wasserInfoList.indexOf(buttonBox)
        val infoTitleArray:Array<String> = resources.getStringArray(R.array.wasserinfos_titles)
        val infoContentArray:Array<String> = resources.getStringArray(R.array.wasserinfos_content)
        toolbarText.text = wasserInfoTitlebarText[index]
        sharedViewModel?.infoTitle?.postValue(HtmlCompat.fromHtml(infoTitleArray[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
        sharedViewModel?.infoContent?.postValue(HtmlCompat.fromHtml(infoContentArray[index], HtmlCompat.FROM_HTML_MODE_COMPACT))

        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentInfos).commit()
        active=fragmentInfos
    }
    override fun generateKontaktSubPage(buttonBox: String) {
        val index = kontaktList.indexOf(buttonBox)
        val kontaktTitleArray:Array<String> = resources.getStringArray(R.array.kontakt_titlebar_text)
        val kontaktContentArray:Array<String> = resources.getStringArray(R.array.kontakt_sub_content)
        toolbarText.text = kontaktTitleArray[index]
        if(index == 0){
            sharedViewModel?.kontaktSubContent?.postValue(HtmlCompat.fromHtml(kontaktContentArray[index], HtmlCompat.FROM_HTML_MODE_COMPACT))
        }
        if(index == 1){
            sharedViewModel?.kontaktSubContent?.postValue(HtmlCompat.fromHtml(datenschutzText, HtmlCompat.FROM_HTML_MODE_COMPACT))
        }
        if(index == 2){
            sharedViewModel?.kontaktSubContent?.postValue(HtmlCompat.fromHtml(nutzungsbedingungText, HtmlCompat.FROM_HTML_MODE_COMPACT))
        }

        prevFragment = active
        fm.beginTransaction().hide(active).show(fragmentKontaktSub).commit()
        active=fragmentKontaktSub
    }
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onBackPressed() {
        //Log.d("Back", "On back pressed!")
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
            if (doubleBackToExitPressedOnce){
                super.onBackPressed()
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Bitte klicken Sie erneut auf ZURÜCK zum Beenden", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(Runnable { mRunnable }, 2000)
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Create notification channel
        NotificationHelper.createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false, getString(R.string.app_name), "Wassertipps app notification channel.")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }
        loadText()

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) {}

        // Create the InterstitialAd and set it up.
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = AD_UNIT_ID
            adListener = (object : AdListener() {
                override fun onAdLoaded() {
                    //Toast.makeText(this@MainActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                    showInterstitial()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    //Toast.makeText(this@MainActivity, "onAdFailedToLoad() with error code: $errorCode",Toast.LENGTH_SHORT).show()
                }

                override fun onAdClosed() {
                    //
                }
            })
        }
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        navView = findViewById(R.id.nav_view)
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
            //Log.d("Saved data", "empty")
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
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                //getLocation()
                fetchLocation()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            //Toast.makeText(this, "Android < 6", Toast.LENGTH_SHORT).show()
            fetchLocation()
        }
    }

    private fun fetchLocation(){
        val locData = LocationCheck(this.applicationContext)
        locData.checkLocation()
        val location = locData.getLocation()
        val lat = location.latitude
        val lon = location.longitude
        Log.d("Lat - Lon :", " "+lat+" - "+lon+"")
        getData(lat, lon)
    }
    private fun loadText(){
        Executors.newSingleThreadExecutor().execute {
            try{
                datenschutzText = URL("https://app.wassertipps.de/app/datenschutz-app.html").readText()
            } catch (e: Exception){
                Log.d("Data fetch failed", "Check internet connection or the server status.")
            }
        }
        Executors.newSingleThreadExecutor().execute {
            try{
                nutzungsbedingungText = URL("https://app.wassertipps.de/app/nutzungsbedingungen-app.html").readText()
            } catch (e: Exception){
                Log.d("Data fetch failed", "Check internet connection or the server status.")
            }

        }
    }
    /**
     * remove runnable from handler before closing the app
     */
    override fun onDestroy(){
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }
    private fun updateRechnerNavIcon(value: Boolean){
        println("At update rechner navicon")
        val item = navView.menu.findItem(R.id.navigation_wasserbedarf)
        if(value){
            item.icon = ContextCompat.getDrawable(this,R.drawable.icon_trinken_color)
        } else {
            item.icon = ContextCompat.getDrawable(this,R.drawable.icon_trinken_grey)
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
        if(jsonArray!!.length()==0){
            println("Außerhalb Deutschland")
            //show location popup
            sharedViewModel?.isOutsideDeutschland?.postValue(true) //triggers popup in HomeFragment
        } else {
            Log.d("Response ",jsonArray!!.optJSONObject(0).toString())
            val city =jsonArray!!.optJSONObject(0).optString("city")
            val max = jsonArray!!.optJSONObject(0).optString("max")
            val nmax = jsonArray!!.optJSONObject(0).optString("nmax")
            val desc = jsonArray!!.optJSONObject(0).optString("desc")

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
            } catch (e: Exception){
                Log.d("Data fetch failed", "Check internet connection or the server status.")
            }
            if(responseText!=""){
                Log.d("-Result:", responseText)
                parseJson(responseText)
            } else {
                Log.d("Data fetch failed -2", "Check internet connection or the server status."+responseText)
                //show internet popup
                sharedViewModel?.isInternetUnavailable?.postValue(true) //triggers popup in HomeFragment
            }
        }
    }

    // Show the ad if it's ready. Otherwise toast and restart the game.
    private fun showInterstitial() {
        if (mInterstitialAd.isLoaded && !billingManager.isProPurchased) {
            mInterstitialAd.show()
        } else {
            //Toast.makeText(this, "Ad wasn't loaded.", Toast.LENGTH_SHORT).show()
            //startGame()
        }
    }


}

