package com.elf.donordarah.repositories

import android.media.Image
import com.elf.donordarah.models.CreateSubmission
import com.elf.donordarah.models.Submission
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.Constants
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

interface SubmissionContract{
    fun fetchSubmissions(token : String, listener : ArrayResponse<Submission>)
    fun createSubmission(token: String, createSubmission: CreateSubmission, image: String, listener : SingleResponse<CreateSubmission>)
    fun updateSubmission(token: String, id : String, createSubmission: CreateSubmission, image: String, listener : SingleResponse<CreateSubmission>)
}

class SubmissionRepository (private val api : ApiService) : SubmissionContract{
    override fun fetchSubmissions(token: String, listener: ArrayResponse<Submission>) {
        api.fetchSubmissions(token).enqueue(object : Callback<WrappedListResponse<Submission>>{
            override fun onFailure(call: Call<WrappedListResponse<Submission>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Submission>>,
                response: Response<WrappedListResponse<Submission>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun createSubmission(token: String, createSubmission: CreateSubmission, image: String, listener: SingleResponse<CreateSubmission>) {
        val file = File(image)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val img = MultipartBody.Part.createFormData("foto", file.name, requestBodyForFile)
        api.createSubmission(token, hasMapString(createSubmission), img).enqueue(object : Callback<WrappedResponse<CreateSubmission>>{
            override fun onFailure(call: Call<WrappedResponse<CreateSubmission>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<CreateSubmission>>,
                response: Response<WrappedResponse<CreateSubmission>>
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

    override fun updateSubmission(token: String, id: String, createSubmission: CreateSubmission, image: String, listener: SingleResponse<CreateSubmission>) {
        val file = File(image)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val img = MultipartBody.Part.createFormData("foto", file.name, requestBodyForFile)
        api.updateSubmission(token, id.toInt(), hasMapString(createSubmission), img).enqueue(object : Callback<WrappedResponse<CreateSubmission>>{
            override fun onFailure(call: Call<WrappedResponse<CreateSubmission>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<CreateSubmission>>,
                response: Response<WrappedResponse<CreateSubmission>>
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

    fun hasMapString(createSubmission: CreateSubmission): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map["nama_tempat"] = Constants.createPartFromString(createSubmission.location!!)
        map["nama_acara"] = Constants.createPartFromString(createSubmission.event!!)
        map["jumlah_peserta"] = Constants.createPartFromString(createSubmission.participants.toString())
        map["hari"] = Constants.createPartFromString(createSubmission.day!!)
        map["tanggal"] = Constants.createPartFromString(createSubmission.date!!)
        map["jam_mulai"] = Constants.createPartFromString(createSubmission.start_time!!)
        map["jam_selesai"] = Constants.createPartFromString(createSubmission.end_time!!)
        map["alamat"] = Constants.createPartFromString(createSubmission.address!!)
        map["penanggung_jawab"] = Constants.createPartFromString(createSubmission.person_in_charge!!)
        return map
    }
}