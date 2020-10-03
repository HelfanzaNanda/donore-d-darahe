package com.elf.donordarah.webservices

import com.elf.donordarah.models.*
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") pass : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/user/register")
    fun register(
        @Field("nama") nama : String,
        @Field("email") email : String,
        @Field("password") pass : String,
        @Field("role") role : String
    ) : Call<WrappedResponse<User>>

    @GET("api/news")
    fun fetchNews() : Call<WrappedListResponse<News>>

    @GET("api/schedulle")
    fun fetchSchedulle() : Call<WrappedListResponse<Schedulle>>

    @GET("api/information")
    fun fetchInformations() : Call<WrappedListResponse<Information>>

    @GET("api/pendonor")
    fun fetchDonors(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Pendonor>>

    @GET("api/stock")
    fun fetchStock() : Call<WrappedListResponse<Stok>>

    @GET("api/submission")
    fun fetchSubmissions(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Submission>>

    @Headers("Content-Type: application/json")
    @POST("api/submission/add")
    fun createSubmission(
        @Header("Authorization") token : String,
        @Body body : RequestBody
    ) : Call<WrappedResponse<CreateSubmission>>
}

data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)