package com.elf.donordarah.repositories

import com.elf.donordarah.models.Stok
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface StokContract{
    fun fetchStock(listener : ArrayResponse<Stok>)
}

class StockRepository (private val api : ApiService) : StokContract{
    override fun fetchStock(listener: ArrayResponse<Stok>) {
        api.fetchStock().enqueue(object : Callback<WrappedListResponse<Stok>>{
            override fun onFailure(call: Call<WrappedListResponse<Stok>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Stok>>,
                response: Response<WrappedListResponse<Stok>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}