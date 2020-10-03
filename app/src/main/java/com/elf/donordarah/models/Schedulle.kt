package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedulle(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama_tempat") var location : String? = null,
    @SerializedName("foto") var image : String? = null,
    @SerializedName("hari") var day : String? = null,
    @SerializedName("tanggal") var date : String? = null,
    @SerializedName("alamat") var address : String? = null,
    @SerializedName("jam_mulai") var start_time : String? = null,
    @SerializedName("jam_selesai") var end_time : String? = null
) : Parcelable