package com.zeitform.wasserapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.zeitform.wasserapp.viewmodel.SharedViewModel

class LocationCheck(val mContext: Context, sharedViewModel: SharedViewModel?) {

    private var sharedViewModel: SharedViewModel? = null
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    init {
        this.sharedViewModel = sharedViewModel
    }
    @SuppressLint("MissingPermission")
     fun checkLocation(){
        var criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.POWER_HIGH
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {

                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    1000,
                    100F,
                    criteria,
                    gpsListener,
                    null)
               val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
                Log.d("Loc GPS",locationGps.toString())
                //locationManager.removeUpdates(gpsListener)
            }
            if (hasNetwork) {

                Log.d("CodeAndroidLocation", "hasNetwork")
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    100F,
                    networkListener)
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation

                Log.d("Loc Network",locationNetwork.toString())

                //locationManager.removeUpdates(networkListener)
            }
        } else {
            //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            //Handle no location data
            Log.d("NO LOCATION" ,"no location data")
        }
    }
    private val gpsListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                locationGps = location
            }
            Log.d("Location from GPS ",locationGps.toString())
            //Toast.makeText(mContext, "Location from GPS "+locationGps.toString(), Toast.LENGTH_LONG).show()
            locationManager.removeUpdates(this)
            checkBestLocation()
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //println("GPS status| Provider:"+provider+"- Status:"+status)
        }

        override fun onProviderEnabled(provider: String?) {
            //println("GPS status| Provider enabled:"+provider)
        }

        override fun onProviderDisabled(provider: String?) {
            //println("GPS status| Provider disabled:"+provider)
        }

    }
    private val networkListener = object: LocationListener {
        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                locationNetwork = location
            }
            Log.d("Location from network ",locationNetwork.toString())
            //Toast.makeText(mContext, "Location from Network "+locationNetwork.toString(), Toast.LENGTH_LONG).show()
            locationManager.removeUpdates(this)
            checkBestLocation()
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //println("Network status| Provider:"+provider+"- Status:"+status)
        }

        override fun onProviderEnabled(provider: String?) {
            //println("Network status| Provider enabled:"+provider)
        }

        override fun onProviderDisabled(provider: String?) {
            //println("Network status| Provider disabled:"+provider)
        }

    }

    fun checkBestLocation() {
        if (locationGps != null && locationNetwork != null) {
            if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                latitude = locationNetwork!!.latitude
                longitude = locationNetwork!!.longitude
            } else {
                Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                latitude = locationGps!!.latitude
                longitude = locationGps!!.longitude
                //createToast("GPS Location: "+locationGps!!.latitude+" | Lon: "+locationGps!!.longitude)

            }
        } else if(locationGps==null && locationNetwork!= null){
            Log.d("CodeAndroidLocation", "Using Network Latitude : " + locationNetwork!!.latitude)
            Log.d("CodeAndroidLocation", "Using Network Longitude : " + locationNetwork!!.longitude)
            latitude = locationNetwork!!.latitude
            longitude = locationNetwork!!.longitude
        } else if(locationGps!=null && locationNetwork== null){
            Log.d("CodeAndroidLocation", "Using GPS Latitude : " + locationGps!!.latitude)
            Log.d("CodeAndroidLocation", "Using GPS Longitude : " + locationGps!!.longitude)
            latitude = locationGps!!.latitude
            longitude = locationGps!!.longitude
        }
        updateLocationViewModel(LocationData(latitude, longitude))
    }

    private fun updateLocationViewModel(locationData: LocationData) {
        sharedViewModel?.locationData?.postValue(locationData)
    }
    fun removeListeners(){
        locationManager.removeUpdates(gpsListener)
        locationManager.removeUpdates(networkListener)
    }
}