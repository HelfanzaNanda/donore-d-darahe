package com.elf.donordarah.ui.submission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.Submission
import com.elf.donordarah.repositories.SubmissionRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent

class SubmissionViewModel (private val submissionRepository: SubmissionRepository) : ViewModel(){
    private val state : SingleLiveEvent<SubmissionState> = SingleLiveEvent()
    private val submissions = MutableLiveData<List<Submission>>()

    private fun isLoading(b : Boolean) { state.value = SubmissionState.Loading(b) }
    private fun toast(m : String){ state.value = SubmissionState.ShowToast(m) }

    fun fetchSubmissions(token : String){
        isLoading(true)
        submissionRepository.fetchSubmissions(token, object : ArrayResponse<Submission>{
            override fun onSuccess(datas: List<Submission>?) {
                isLoading(false)
                datas?.let { submissions.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
    fun listenToSubmissions() = submissions
}

sealed class SubmissionState{
    data class Loading(var state : Boolean = false) : SubmissionState()
    data class ShowToast(var message : String) : SubmissionState()
}