package com.elf.donordarah.ui.information

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.donordarah.R
import com.elf.donordarah.models.Information
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.activity_information.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InformationActivity : AppCompatActivity() {

    private val informationViewModel : InformationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        supportActionBar?.title = "Information"
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = InformationAdapter(mutableListOf(), this@InformationActivity)
            layoutManager = LinearLayoutManager(this@InformationActivity)
        }
    }

    private fun observe(){
        observeState()
        observeInformations()
    }

    private fun observeState() = informationViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeInformations() = informationViewModel.listenToInformations().observe(this, Observer { handleInformations(it) })
    private fun fetchInformations() = informationViewModel.fetchInformations()

    private fun handleInformations(list: List<Information>?) {
        list?.let {
            recycler_view.adapter?.let {adapter ->
                if (adapter is InformationAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(state: InformationState?) {
        state?.let {
            when(it){
                is InformationState.Loading -> handleLoading(it.state)
                is InformationState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fetchInformations()
    }
}