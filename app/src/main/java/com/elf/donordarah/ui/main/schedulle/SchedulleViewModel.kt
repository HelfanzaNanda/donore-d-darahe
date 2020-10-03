package com.elf.donordarah.ui.main.schedulle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.Schedulle
import com.elf.donordarah.repositories.SchedulleRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent
import com.google.gson.GsonBuilder

class SchedulleViewModel (private val schedulleRepository: SchedulleRepository) : ViewModel(){
    private val state : SingleLiveEvent<SchedulleState> = SingleLiveEvent()
    private val schedulles = MutableLiveData<List<Schedulle>>()

    private fun isLoading(b : Boolean) { state.value = SchedulleState.Loading(b) }
    private fun toast(m : String){ state.value = SchedulleState.ShowToast(m) }

    fun fecthSchedulles(){
        isLoading(true)
        schedulleRepository.fetchSchedulle(object : ArrayResponse<Schedulle>{
            override fun onSuccess(datas: List<Schedulle>?) {
                isLoading(false)
                datas?.let { schedulles.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToSchedulles() = schedulles
}

sealed class SchedulleState{
    data class Loading(var state : Boolean = false) : SchedulleState()
    data class ShowToast(var message : String) : SchedulleState()
}