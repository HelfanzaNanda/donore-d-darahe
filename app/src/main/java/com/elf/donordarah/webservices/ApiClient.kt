package com.elf.donordarah.webservices

import com.elf.donordarah.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null
        private var retrofitCapil : Retrofit? = null

        private val opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient(): Retrofit {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(Constants.ENDPOINT)
                    //baseUrl(BuildConfig.ENDPOINT)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            } else {
                retrofit!!
            }
        }

        private fun getClientCapil() : Retrofit {
            return if(retrofitCapil == null){
                retrofitCapil = Retrofit.Builder().apply {
                    baseUrl(Constants.ENDPOINTCAPIL)
                    client(opt)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofitCapil!!
            }else{
                retrofitCapil!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)
        fun instanceCapil() = getClientCapil().create(CapilApiService::class.java)
    }
}