package com.example.premiumplus.requestModel

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AddClientRequestModel(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Landline")
	val landline: String? = null,

	@field:SerializedName("UserId")
	val userId: String? = null,

	@field:SerializedName("Phone")
	val phone: String? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("Latitude")
	val latitude: String? = null,

	@field:SerializedName("Longitude")
	val longitude: String? = null,

	@field:SerializedName("ContactPerson")
	val contactPerson: String? = null,

	@field:SerializedName("Location")
	val location: String? = null
) : Parcelable
