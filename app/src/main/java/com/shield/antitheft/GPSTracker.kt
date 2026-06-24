package com.shield.antitheft

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class GPSTracker(private val context: Context) {
    private var locationManager: LocationManager
    private var locationCallback: ((Location?) -> Unit)? = null
    
    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    
    fun getCurrentLocation(callback: (Location?) -> Unit) {
        try {
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            
            if (!isGPSEnabled && !isNetworkEnabled) {
                callback(null)
                return
            }
            
            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationManager.removeUpdates(this)
                    callback(location)
                }
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            
            // Try GPS first (more accurate)
            if (isGPSEnabled) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null)
            } else if (isNetworkEnabled) {
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null)
            }
            
            // Timeout after 10 seconds
            android.os.Handler().postDelayed({
                locationManager.removeUpdates(listener)
                callback(null)
            }, 10000)
        } catch (e: Exception) {
            Log.e("Shield", "GPS error", e)
            callback(null)
        }
    }
    
    fun getMapsLink(location: Location): String {
        return "https://maps.google.com/?q=${location.latitude},${location.longitude}"
    }
}
