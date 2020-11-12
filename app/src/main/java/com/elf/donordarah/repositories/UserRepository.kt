package com.elf.donordarah.repositories

import android.media.Image
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.models.User
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import com.elf.donordarah.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface UserContract{
    fun fetchDonors(token : String, listener : ArrayResponse<Pendonor>)
    fun login(email : String, pass : String, listener : SingleResponse<User>)
    fun register(nama : String, email: String, pass: String, role : String, listener: SingleResponse<User>)
    fun profile(token: String, listener: SingleResponse<User>)
    fun updateUser(token: String, user: User, listener: SingleResponse<User>)
    fun updatePhoto(token: String, image: String, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract{
    override fun fetchDonors(token: String, listener: ArrayResponse<Pendonor>) {
        api.fetchDonors(token).enqueue(object : Callback<WrappedListResponse<Pendonor>>{
            override fun onFailure(call: Call<WrappedListResponse<Pendonor>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Pendonor>>,
                response: Response<WrappedListResponse<Pendonor>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun login(email: String, pass: String, listener: SingleResponse<User>) {
        api.login(email, pass).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data)else listener.onFailure(Error("tidak dapat login. pastikan email dan password benar dan sudah di verifikasi")
                        )
                    }
                    else -> listener.onFailure(Error("masukkan email dan password yang benar"))
                }
            }

        })
    }

    override fun register(nama: String, email: String, pass: String, role: String, listener: SingleResponse<User>) {
        api.register(nama, email, pass, role).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data)else listener.onFailure(Error("tidak dapat register")
                        )
                    }
                    else -> listener.onFailure(Error("gagal register, mungkin email sudah pernah di daftarkan"))
                }
            }
        })
    }

    override fun profile(token: String, listener: SingleResponse<User>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updateUser(token: String, user: User, listener: SingleResponse<User>) {
        val g = GsonBuilder().create()
        val json = g.toJson(user)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.updateUser(token, body).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updatePhoto(token: String, image: String, listener: SingleResponse<User>) {
        val file = File(image)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val img = MultipartBody.Part.createFormData("foto", file.name, requestBodyForFile)
        api.updatePhoto(token, img).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}

