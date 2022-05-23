package com.agence.marketplace.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*

class LocationLiveData (private val context: Context): LiveData<Location?>() {
    var requestingLocationUpdates: Boolean = true
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null

    @Synchronized
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.interval = 5000
        mLocationRequest?.fastestInterval = 5000
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun startService() {
        onActive()
    }


    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient
        createLocationRequest()
        val looper = Looper.myLooper()
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest!!, mLocationCallback,
            looper!!
        )
    }

    override fun onInactive() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
        }
    }

    val fusedLocationProviderClient: FusedLocationProviderClient?
        get() {
            if (mFusedLocationClient == null) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            }
            return mFusedLocationClient
        }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val newLocation = locationResult.lastLocation
            if (newLocation != null && requestingLocationUpdates){
                value = newLocation
                onInactive()
            }
        }
    }

}