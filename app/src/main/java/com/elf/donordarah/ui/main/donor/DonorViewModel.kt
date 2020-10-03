package com.elf.donordarah.ui.main.donor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.repositories.UserRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent

class DonorViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<DonorState> = SingleLiveEvent()
    private val donors = MutableLiveData<List<Pendonor>>()

    private fun isLoading(b : Boolean) { state.value = DonorState.Loading(b) }
    private fun toast(m : String){ state.value = DonorState.ShowToast(m) }

    fun fecthDonors(token :String){
        isLoading(true)
        userRepository.fetchDonors(token, object : ArrayResponse<Pendonor>{
            override fun onSuccess(datas: List<Pendonor>?) {
                isLoading(false)
                datas?.let { donors.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
    fun listenToDonors() = donors
}

sealed class DonorState{
    data class Loading(var state : Boolean = false) : DonorState()
    data class ShowToast(var message : String) : DonorState()
}