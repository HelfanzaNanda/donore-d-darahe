package com.elf.donordarah.webservices

import com.elf.donordarah.models.*
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
        @Field("password")  pass : String,
        @Field("role") role : String
    ) : Call<WrappedResponse<User>>

    @GET("api/user/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @GET("api/chart")
    fun chart(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Chart>>

    @Headers("Content-Type: application/json")
    @POST("api/user/update")
    fun updateUser(
        @Header("Authorization") token : String,
        @Body body: RequestBody
    ) : Call<WrappedResponse<User>>

    @Multipart
    @POST("api/user/upload")
    fun updatePhoto(
        @Header("Authorization") token : String,
        @Part image: MultipartBody.Part
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


    @Multipart
    @POST("api/submission/add")
    fun createSubmission(
        @Header("Authorization") token : String,
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ) : Call<WrappedResponse<CreateSubmission>>

    @Multipart
    @POST("api/submission/{id}/update")
    fun updateSubmission(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ) : Call<WrappedResponse<CreateSubmission>>

    @Headers("Content-Type: application/json")
    @POST("api/pendonor/add")
    fun createPendonor(
        @Header("Authorization") token : String,
        @Body body: RequestBody
    ) : Call<WrappedResponse<CreatePendonor>>

    @Headers("Content-Type: application/json")
    @POST("api/pendonor/{id}/update")
    fun updatePendonor(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Body body: RequestBody
    ) : Call<WrappedResponse<CreatePendonor>>
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

interface CapilApiService {

//    @GET("http://103.12.164.52:8185/ws_server/get_json/diskominfo/NEWNIK?USER_ID=kominfo&PASSWORD=123456&NIK={nik}")
//    fun getUser(
//        @Path("nik") nik : Int
//    ) : Call<WrappedNik<Pendonor>>

    @POST("NEWNIK")
    fun getUser(@Query("USER_ID") userId : String,
                @Query("PASSWORD") password : String,
                @Query("NIK") nik : String
    ) : Call<ResponseBody>
}