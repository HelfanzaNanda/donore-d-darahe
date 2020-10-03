package com.elf.donordarah.repositories

import com.elf.donordarah.models.CreateSubmission
import com.elf.donordarah.models.Submission
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleResponse
import com.elf.donordarah.webservices.ApiService
import com.elf.donordarah.webservices.WrappedListResponse
import com.elf.donordarah.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SubmissionContract{
    fun fetchSubmissions(token : String, listener : ArrayResponse<Submission>)
    fun createSubmission(token: String, createSubmission: CreateSubmission, listener : SingleResponse<CreateSubmission>)
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

    override fun createSubmission(
        token: String,
        createSubmission: CreateSubmission,
        listener: SingleResponse<CreateSubmission>
    ) {
        val g = GsonBuilder().create()
        val json = g.toJson(createSubmission)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.createSubmission(token, body).enqueue(object : Callback<WrappedResponse<CreateSubmission>>{
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

}