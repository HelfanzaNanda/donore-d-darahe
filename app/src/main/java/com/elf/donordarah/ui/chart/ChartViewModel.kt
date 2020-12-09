package com.elf.donordarah.ui.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.Chart
import com.elf.donordarah.repositories.UserRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent

class ChartViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<ChartState> = SingleLiveEvent()
    private val charts = MutableLiveData<List<Chart>>()

    private fun isLoading(b : Boolean){ state.value = ChartState.Loading(b) }
    private fun toast(m : String){ state.value = ChartState.ShowToast(m) }

    fun chart(token : String){
        isLoading(true)
        userRepository.chart(token, object : ArrayResponse<Chart>{
            override fun onSuccess(datas: List<Chart>?) {
                isLoading(false)
                datas?.let { charts.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToCharts() = charts
}

sealed class ChartState{
    data class Loading(var state : Boolean = false) : ChartState()
    data class ShowToast(var message : String) : ChartState()
}
