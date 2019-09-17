package com.zeitform.wasserapp.location

data class LocationData(val latitude: Double?, val longitude: Double?) {

    fun getLocation(): LocationData {
        return LocationData(latitude, longitude)
    }
}
