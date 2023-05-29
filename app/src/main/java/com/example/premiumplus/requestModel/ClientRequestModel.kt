package com.example.premiumplus.requestModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClientRequestModel(
    @field:SerializedName("SubscriptionID")
    val subscriptionID: String? = null,

    @field:SerializedName("UserId")
    val userId: String? = null,

    @field:SerializedName("EnableStatus")
    val enableStatus: String? = null
) : Parcelable
