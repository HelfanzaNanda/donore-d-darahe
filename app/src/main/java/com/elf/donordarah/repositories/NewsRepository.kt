package com.elf.donordarah.repositories

import com.elf.donordarah.models.News
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface NewsContract{
    fun fetchNews(listener : ArrayResponse<News>)
}

class NewsRepository(private val api : ApiService) : NewsContract{
    override fun fetchNews(listener: ArrayResponse<News>) {
        api.fetchNews().enqueue(object : Callback<WrappedListResponse<News>>{
            override fun onFailure(call: Call<WrappedListResponse<News>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<News>>, response: Response<WrappedListResponse<News>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}