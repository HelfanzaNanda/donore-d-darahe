package com.elf.donordarah.ui.add_edit_submission

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.CreateSubmission
import com.elf.donordarah.models.User
import com.elf.donordarah.repositories.SubmissionRepository
import com.elf.donordarah.utils.SingleLiveEvent
import com.elf.donordarah.utils.SingleResponse
import java.io.File
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class AddEditSubmissionViewModel (private val submissionRepository: SubmissionRepository) : ViewModel(){
    private val state : SingleLiveEvent<AddEditSubmissionState> = SingleLiveEvent()
    private val currentSelectedDate = MutableLiveData<String>()
    private val currentSelectedDay = MutableLiveData<String>()
    private val currentSelectedStartTime = MutableLiveData<String>()
    private val currentSelectedEndTime = MutableLiveData<String>()
    private val imageSelected = MutableLiveData<String>()

    private fun isLoading(b : Boolean) { state.value = AddEditSubmissionState.Loading(b) }
    private fun toast(m : String){ state.value = AddEditSubmissionState.ShowToast(m) }
    private fun success(){ state.value = AddEditSubmissionState.Success }


    @SuppressLint("SimpleDateFormat")
    fun setCurrentDate(myCalendar: Calendar){
        val myFormat = "yyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        currentSelectedDate.value = sdf.format(myCalendar.time)
        currentSelectedDay.value = SimpleDateFormat("EEEE").format(myCalendar.time)
    }

    fun setCurrentStartTime(hour: Int, minute: Int) {
         currentSelectedStartTime.value = "$hour:$minute"
    }

    fun setCurrentEndTime(hour: Int, minute: Int) {
        currentSelectedEndTime.value = "$hour:$minute"
    }

    fun setImageSelected(image : String){
        imageSelected.value = image
    }

    fun createSubmission(token : String, createSubmission: CreateSubmission, image: String){
        isLoading(true)
        submissionRepository.createSubmission(token, createSubmission, image, object : SingleResponse<CreateSubmission>{
            override fun onSuccess(data: CreateSubmission?) {
                isLoading(false)
                data?.let { success() }
            }
            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun updateSubmission(token : String, id : String, createSubmission: CreateSubmission, image: String){
        isLoading(true)
        submissionRepository.updateSubmission(token, id, createSubmission, image, object : SingleResponse<CreateSubmission>{
            override fun onSuccess(data: CreateSubmission?) {
                isLoading(false)
                data?.let { success() }
            }
            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun validate(createSubmission: CreateSubmission) : Boolean{
        state.value = AddEditSubmissionState.Reset
        if (createSubmission.location!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(loc = "nama tempat tidak boleh kosong")
            return false
        }
        if (createSubmission.event!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(event = "nama acara tidak boleh kosong")
            return false
        }
        if (createSubmission.participants!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(participants = "jumlah peserta tidak boleh kosong")
            return false
        }
        if (createSubmission.date!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(date = "tanggal tidak boleh kosong")
            return false
        }
        if (createSubmission.start_time!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(startTime = "jam mulai tidak boleh kosong")
            return false
        }

        if (createSubmission.end_time!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(startTime = "jam selesai tidak boleh kosong")
            return false
        }

        if (createSubmission.person_in_charge!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(pj = "penanggung jawab tidak boleh kosong")
            return false
        }

        if (createSubmission.address!!.isEmpty()){
            state.value = AddEditSubmissionState.Validate(address = "alamat tidak boleh kosong")
            return false
        }
        return true
    }

    fun listenToCurrentStartTime() = currentSelectedStartTime
    fun listenToCurrentEndTime() = currentSelectedEndTime
    fun listenToCurrentDate() = currentSelectedDate
    fun listenToCurrentDay() = currentSelectedDay
    fun listenToState() = state
    fun listenToImageSelected() = imageSelected
}

sealed class AddEditSubmissionState{
    data class Loading(var state : Boolean = false) : AddEditSubmissionState()
    data class ShowToast(var message : String) : AddEditSubmissionState()
    object Reset : AddEditSubmissionState()
    data class Validate(
        var loc : String? = null,
        var event : String? = null,
        var participants : String? = null,
        var day : String? = null,
        var date : String? = null,
        var startTime : String? = null,
        var endTime : String? = null,
        var pj : String? = null,
        var address : String? = null,
        var city : String? = null
    ) : AddEditSubmissionState()
    object Success : AddEditSubmissionState()
}