package com.elf.donordarah.ui.main.stock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.Stok
import com.elf.donordarah.repositories.StockRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent

class StockViewModel (private val stockRepository: StockRepository) : ViewModel(){
    private val state : SingleLiveEvent<StockState> = SingleLiveEvent()
    private val stock = MutableLiveData<List<Stok>>()

    private fun isLoading(b : Boolean) { state.value = StockState.Loading(b) }
    private fun toast(m : String){ state.value = StockState.ShowToast(m) }

    fun fecthStock(){
        isLoading(true)
        stockRepository.fetchStock(object : ArrayResponse<Stok>{
            override fun onSuccess(datas: List<Stok>?) {
                isLoading(false)
                datas?.let { stock.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
    fun listenToStock() = stock
}

sealed class StockState{
    data class Loading(var state : Boolean = false) : StockState()
    data class ShowToast(var message : String) : StockState()
}