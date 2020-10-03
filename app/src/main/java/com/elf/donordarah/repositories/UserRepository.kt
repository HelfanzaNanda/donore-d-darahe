package com.elf.donordarah.repositories

import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.models.User
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import com.elf.donordarah.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserContract{
    fun fetchDonors(token : String, listener : ArrayResponse<Pendonor>)
    fun login(email : String, pass : String, listener : SingleResponse<User>)
    fun register(nama : String, email: String, pass: String, role : String, listener: SingleResponse<User>)
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
                        if (body?.status!!) listener.onSuccess(body.data)else listener.onFailure(
                            Error(body.message)
                        )
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun register(
        nama: String,
        email: String,
        pass: String,
        role: String,
        listener: SingleResponse<User>
    ) {
        api.register(nama, email, pass, role).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data)else listener.onFailure(
                            Error(body.message)
                        )
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}

