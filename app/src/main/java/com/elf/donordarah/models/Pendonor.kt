package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pendonor(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("ktp") var ktp : String? = null,
    @SerializedName("nama") var nama : String? = null,
    @SerializedName("kabupaten") var kabupaten : String? = null,
    @SerializedName("kecamatan") var kecamatan : String? = null,
    @SerializedName("desa") var desa : String? = null,
    @SerializedName("alamat") var address : String? = null,
    @SerializedName("jenis_kelamin") var gender : String? = null,
    @SerializedName("tempat_lahir") var place_of_birth : String? = null,
    @SerializedName("tanggal_lahir") var date_of_birth : String? = null,
    @SerializedName("pekerjaan") var working : String? = null,
    @SerializedName("nama_ibu") var mother_name : String? = null,
    @SerializedName("status_nikah") var status : String? = null,
    @SerializedName("phone") var phone : String? = null,
    @SerializedName("gol_dar") var blood_type : String? = null
) : Parcelable