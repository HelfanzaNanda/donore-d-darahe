package com.elf.donordarah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Submission(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama_tempat") var location : String? = null,
    @SerializedName("nama_acara") var event : String? = null,
    @SerializedName("jumlah_peserta") var participants : String? = null,
    @SerializedName("foto") var image : String? = null,
    @SerializedName("hari") var day : String? = null,
    @SerializedName("tanggal") var date : String? = null,
    @SerializedName("alamat") var address : String? = null,
    @SerializedName("jam_mulai") var start_time : String? = null,
    @SerializedName("jam_selesai") var end_time : String? = null,
    @SerializedName("penanggung_jawab") var person_in_charge : String? = null,
    @SerializedName("kota") var city : String? = null,
    @SerializedName("status") var status : String? = null
) : Parcelable

@Parcelize
data class CreateSubmission(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama_tempat") var location : String? = null,
    @SerializedName("nama_acara") var event : String? = null,
    @SerializedName("jumlah_peserta") var participants : String? = null,
    @SerializedName("foto") var image : String? = null,
    @SerializedName("hari") var day : String? = null,
    @SerializedName("tanggal") var date : String? = null,
    @SerializedName("alamat") var address : String? = null,
    @SerializedName("jam_mulai") var start_time : String? = null,
    @SerializedName("jam_selesai") var end_time : String? = null,
    @SerializedName("penanggung_jawab") var person_in_charge : String? = null,
    @SerializedName("kota") var city : String? = null,
    @SerializedName("status") var status : String? = null
) : Parcelable