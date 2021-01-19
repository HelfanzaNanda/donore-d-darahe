package com.elf.donordarah.ui.add_edit_donor

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.CreatePendonor
import com.elf.donordarah.models.CreateSubmission
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.models.UserCapil
import com.elf.donordarah.repositories.DonorRepository
import com.elf.donordarah.repositories.SubmissionRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent
import com.elf.donordarah.utils.SingleResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import java.text.SimpleDateFormat
import java.util.*

class AddEditDonorViewModel (private val donorRepository: DonorRepository) : ViewModel(){
    private val state : SingleLiveEvent<AddEditDonorState> = SingleLiveEvent()
    private val currentSelectedDate = MutableLiveData<String>()
    private val currentSelectedDay = MutableLiveData<String>()
    private val userCapil = MutableLiveData<UserCapil>()

    private fun isLoading(b : Boolean) { state.value = AddEditDonorState.Loading(b) }
    private fun toast(m : String){ state.value = AddEditDonorState.ShowToast(m) }
    private fun success(){ state.value = AddEditDonorState.Success }


    @SuppressLint("SimpleDateFormat")
    fun setCurrentDate(myCalendar: Calendar){
        val myFormat = "yyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        currentSelectedDate.value = sdf.format(myCalendar.time)
        currentSelectedDay.value = SimpleDateFormat("EEEE").format(myCalendar.time)
    }

    fun createPendonor(token : String, createPendonor: CreatePendonor){
        isLoading(true)
        donorRepository.createDonor(token, createPendonor, object :
            SingleResponse<CreatePendonor> {
            override fun onSuccess(data: CreatePendonor?) {
                isLoading(false)
                data?.let { success() }
            }
            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun updateSubmission(token : String, id : String, createPendonor: CreatePendonor){
        isLoading(true)
        donorRepository.updateDonor(token, id, createPendonor, object :
            SingleResponse<CreatePendonor> {
            override fun onSuccess(data: CreatePendonor?) {
                isLoading(false)
                data?.let { success() }
            }
            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun validate(createPendonor: CreatePendonor) : Boolean{
        state.value = AddEditDonorState.Reset
        if (createPendonor.ktp.isNullOrEmpty()){
            state.value = AddEditDonorState.Validate(ktp = "no ktp tidak boleh kosong")
            return false
        }
        if (createPendonor.nama.isNullOrEmpty()){
            state.value = AddEditDonorState.Validate(name = "nama tidak boleh kosong")
            return false
        }
        if (createPendonor.address.isNullOrEmpty()){
            state.value = AddEditDonorState.Validate(address = "alamat tidak boleh kosong")
            return false
        }

        if (createPendonor.place_of_birth.isNullOrEmpty()){
            state.value = AddEditDonorState.Validate(ttl = "tempat lahir tidak boleh kosong")
            return false
        }

        if (createPendonor.mother_name.isNullOrEmpty()){
            state.value = AddEditDonorState.Validate(nameMother = "nama ibu tidak boleh kosong")
            return false
        }

        if (createPendonor.phone.isNullOrEmpty()){
            state.value = AddEditDonorState.Validate(phone = "telepon tidak boleh kosong")
            return false
        }
        return true
    }

    fun fetchUser(nik : String){
        isLoading(true)
        donorRepository.getUser(nik, object : SingleResponse<UserCapil>{
            override fun onSuccess(data: UserCapil?) {
                isLoading(false)
                data?.let {
                    userCapil.postValue(it)
                }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToCurrentDate() = currentSelectedDate
    fun listenToCurrentDay() = currentSelectedDay
    fun listenToPendonor() = userCapil
    fun listenToState() = state
}

sealed class AddEditDonorState{
    data class Loading(var state : Boolean = false) : AddEditDonorState()
    data class ShowToast(var message : String) : AddEditDonorState()
    object Reset : AddEditDonorState()
    data class Validate(
        var ktp : String? = null,
        var name : String? = null,
        var address : String? = null,
        var place : String? = null,
        var ttl : String? = null,
        var nameMother : String? = null,
        var phone : String? = null
    ) : AddEditDonorState()
    object Success : AddEditDonorState()
}