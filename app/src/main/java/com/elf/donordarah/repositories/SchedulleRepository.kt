package com.elf.donordarah.repositories

import com.elf.donordarah.models.Schedulle
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SchedulleContract{
    fun fetchSchedulle(listener : ArrayResponse<Schedulle>)
}

class SchedulleRepository (private val api : ApiService) : SchedulleContract{
    override fun fetchSchedulle(listener: ArrayResponse<Schedulle>) {
        api.fetchSchedulle().enqueue(object : Callback<WrappedListResponse<Schedulle>> {
            override fun onFailure(call: Call<WrappedListResponse<Schedulle>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Schedulle>>, response: Response<WrappedListResponse<Schedulle>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}