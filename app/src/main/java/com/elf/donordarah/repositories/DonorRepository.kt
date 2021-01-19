package com.elf.donordarah.repositories

import com.elf.donordarah.models.CreatePendonor
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.models.UserCapil
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.SingleResponse
import com.elf.donordarah.webservices.ApiClient
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.CapilApiService
import com.elf.donordarah.webservices.WrappedResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface DonorContract{
    fun createDonor(token : String, createPendonor: CreatePendonor,  listener : SingleResponse<CreatePendonor>)
    fun updateDonor(token : String, id : String, createPendonor: CreatePendonor, listener : SingleResponse<CreatePendonor>)
    fun getUser(nik : String, listener: SingleResponse<UserCapil>)
}

class DonorRepository(private val api: ApiService = ApiClient.Companion.instance(),
                      private val capilApi : CapilApiService = ApiClient.Companion.instanceCapil()) : DonorContract{

    fun hasMapString(createPendonor: CreatePendonor): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map["ktp"] = Constants.createPartFromString(createPendonor.ktp!!)
        map["nama"] = Constants.createPartFromString(createPendonor.nama!!)
        map["alamat"] = Constants.createPartFromString(createPendonor.address!!)
        map["jenis_kelamin"] = Constants.createPartFromString(createPendonor.gender!!)
        map["tempat_lahir"] = Constants.createPartFromString(createPendonor.place_of_birth!!)
        map["tanggal_lahir"] = Constants.createPartFromString(createPendonor.date_of_birth!!)
        map["pekerjaan"] = Constants.createPartFromString(createPendonor.working!!)
        map["nama_ibu"] = Constants.createPartFromString(createPendonor.mother_name!!)
        map["status_nikah"] = Constants.createPartFromString(createPendonor.status!!)
        map["phone"] = Constants.createPartFromString(createPendonor.phone!!)
        map["gol_dar"] = Constants.createPartFromString(createPendonor.blood_type!!)
        map["rhesus"] = Constants.createPartFromString(createPendonor.rhesus!!)
        return map
    }

    override fun createDonor(token: String, createPendonor: CreatePendonor, listener: SingleResponse<CreatePendonor>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createPendonor)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createPendonor(token, body).enqueue(object : Callback<WrappedResponse<CreatePendonor>>{
            override fun onFailure(call: Call<WrappedResponse<CreatePendonor>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<CreatePendonor>>,
                response: Response<WrappedResponse<CreatePendonor>>
            ) {
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

    override fun updateDonor(token: String,id: String,createPendonor: CreatePendonor,listener: SingleResponse<CreatePendonor>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createPendonor)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.updatePendonor(token, id.toInt(), body).enqueue(object : Callback<WrappedResponse<CreatePendonor>>{
            override fun onFailure(call: Call<WrappedResponse<CreatePendonor>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<CreatePendonor>>,
                response: Response<WrappedResponse<CreatePendonor>>
            ) {
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

    override fun getUser(nik: String, listener: SingleResponse<UserCapil>) {
        capilApi.getUser(Constants.DEF_USER_ID, Constants.DEF_PASSWORD, nik).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("onfailure ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when{
                    response.isSuccessful -> {
                        val jsonObject = JSONObject(response.body()?.string())
                        val totalElement = jsonObject.getInt("totalElements")
                        if (totalElement == 0){
                            val message = jsonObject["content"] as JSONObject
                            listener.onFailure(Error(message.getString("RESPON") + ". NIK anda tidak terdaftar sebagai warga Kota Tegal."))
                        }else{
                            val multipleResult = jsonObject.getJSONArray("content")
                            val jsonObj = multipleResult.getJSONObject(0)
                            listener.onSuccess(Gson().fromJson(jsonObj.toString(), UserCapil::class.java))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}