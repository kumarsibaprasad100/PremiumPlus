package com.example.premiumplus.responseModel

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ClientResponse(

	@field:SerializedName("data")
	val data: ArrayList<DataItem>? = null,

	@field:SerializedName("Success")
	val success: String? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("Landline")
	val landline: String? = null,

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("ContactPersonName")
	val contactPersonName: String? = null,

	@field:SerializedName("Latitude")
	val latitude: String? = null,

	@field:SerializedName("CustomerID")
	val customerID: Int? = null,

	@field:SerializedName("CustomerName")
	val customerName: String? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("Longitude")
	val longitude: String? = null,

	@field:SerializedName("Location")
	val location: String? = null
) : Parcelable
