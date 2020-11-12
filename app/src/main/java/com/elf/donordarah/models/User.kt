package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("image") var image : String? = null,
    @SerializedName("role") var role : String? = null,
    @SerializedName("api_token") var token : String? = null
) : Parcelable