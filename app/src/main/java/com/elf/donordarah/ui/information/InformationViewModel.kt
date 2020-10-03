package com.elf.donordarah.ui.information

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.Information
import com.elf.donordarah.repositories.InformationRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent

class InformationViewModel (private val informationRepository: InformationRepository) : ViewModel(){
    private val state : SingleLiveEvent<InformationState> = SingleLiveEvent()
    private val informations = MutableLiveData<List<Information>>()

    private fun isLoading(b : Boolean){ state.value = InformationState.Loading(b) }
    private fun toast(m : String){ state.value = InformationState.ShowToast(m) }

    fun fetchInformations(){
        isLoading(true)
        informationRepository.fetchInformations(object : ArrayResponse<Information>{
            override fun onSuccess(datas: List<Information>?) {
                isLoading(false)
                datas?.let { informations.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToInformations() = informations
}

sealed class InformationState{
    data class Loading(var state : Boolean = false) : InformationState()
    data class ShowToast(var message : String) : InformationState()
}