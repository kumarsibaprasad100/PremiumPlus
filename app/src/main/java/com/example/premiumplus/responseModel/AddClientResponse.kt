package com.example.premiumplus.responseModel

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AddClientResponse(

	@field:SerializedName("Msg")
	val msg: String? = null,

	@field:SerializedName("Success")
	val success: Int? = null
) : Parcelable
