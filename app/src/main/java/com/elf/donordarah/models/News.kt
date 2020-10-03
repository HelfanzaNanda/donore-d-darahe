package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("category") var category : String? = null,
    @SerializedName("content") var content : String? = null,
    @SerializedName("image") var image : String? = null
) : Parcelable