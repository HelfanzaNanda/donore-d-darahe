package com.elf.donordarah.repositories

import com.elf.donordarah.models.Information
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface InformationContract{
    fun fetchInformations(listener : ArrayResponse<Information>)
}

class InformationRepository (private val api : ApiService) : InformationContract{
    override fun fetchInformations(listener: ArrayResponse<Information>) {
        api.fetchInformations().enqueue(object : Callback<WrappedListResponse<Information>>{
            override fun onFailure(call: Call<WrappedListResponse<Information>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Information>>,
                response: Response<WrappedListResponse<Information>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}