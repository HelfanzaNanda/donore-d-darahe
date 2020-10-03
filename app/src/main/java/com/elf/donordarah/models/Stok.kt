package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stok(
    @SerializedName("id") var id : Int? = null
) : Parcelable