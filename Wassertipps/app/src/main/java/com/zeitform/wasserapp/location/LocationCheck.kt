package com.zeitform.wasserapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class LocationCheck(val mContext: Context) {


    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    @SuppressLint("MissingPermission")
     fun checkLocation(){
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {

                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    200F,
                    gpsListener)
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
                    200F,
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
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }
    private val networkListener = object: LocationListener {
        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                locationNetwork = location
            }
            Log.d("Location from network ",locationNetwork.toString())
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }

    fun getLocation(): LocationData {
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
        removeListeners()
        return LocationData(latitude, longitude)
    }

    fun removeListeners(){
        locationManager.removeUpdates(gpsListener)
        locationManager.removeUpdates(networkListener)
    }
}