package com.elf.donordarah.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.donordarah.models.News
import com.elf.donordarah.repositories.NewsRepository
import com.elf.donordarah.utils.ArrayResponse
import com.elf.donordarah.utils.SingleLiveEvent

class NewsViewModel (private val newsRepository: NewsRepository) : ViewModel(){
    private val state : SingleLiveEvent<NewsState> = SingleLiveEvent()
    private val news = MutableLiveData<List<News>>()

    private fun isLoading(b : Boolean) { state.value = NewsState.Loading(b) }
    private fun toast(m : String){ state.value = NewsState.ShowToast(m) }

    fun fecthNews(){
        isLoading(true)
        newsRepository.fetchNews(object : ArrayResponse<News>{
            override fun onSuccess(datas: List<News>?) {
                isLoading(false)
                datas?.let { news.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
    fun listenToNews() = news
}

sealed class NewsState{
    data class Loading(var state : Boolean = false) : NewsState()
    data class ShowToast(var message : String) : NewsState()
}