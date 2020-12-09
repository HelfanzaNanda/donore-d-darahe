package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Chart(
    @SerializedName("month") var month : String? = null,
    @SerializedName("count") var count : Int? = null
) : Parcelable