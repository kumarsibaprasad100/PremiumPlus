package com.example.premiumplus.viewModels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MapViewModel:ViewModel() {
   val allAddress = MutableLiveData<Address>()

    fun getAddressAndSetFromLatLng(
        latitude: Double?,
        longitude: Double,
        applicationContext: Context
    ) {
        val geocoder = Geocoder(applicationContext, Locale.ENGLISH)
        try {
            val addresses = geocoder.getFromLocation(latitude!!, longitude, 1)
            if (addresses!!.size > 0) {
                val fetchedAddress = addresses!![0]
                Log.i("tag", "AddressList $addresses")
                allAddress.postValue(fetchedAddress)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("tag", "AddressList- Could not get address..!" + e.message)
        }
    }

}