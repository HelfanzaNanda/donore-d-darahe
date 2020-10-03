package com.elf.donordarah.ui.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.donordarah.R
import com.elf.donordarah.models.News
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsActivity : AppCompatActivity() {

    private val newsViewModel : NewsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        supportActionBar?.title = "News"
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = NewsAdapter(mutableListOf(), this@NewsActivity)
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
    }

    private fun observe(){
        observeState()
        observeNews()
    }

    private fun observeState() = newsViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeNews() = newsViewModel.listenToNews().observe(this, Observer { handleNews(it) })
    private fun fetchNews() = newsViewModel.fecthNews()

    private fun handleNews(list: List<News>?) {
        list?.let {
            recycler_view.adapter?.let {adapter ->
                if (adapter is NewsAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(state: NewsState?) {
        state?.let {
            when(it){
                is NewsState.Loading -> handleLoading(it.state)
                is NewsState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fetchNews()
    }
}