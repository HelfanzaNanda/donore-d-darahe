package com.elf.donordarah.ui.main.stock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.donordarah.R
import com.elf.donordarah.models.Stok
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_stock.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StockFragment : Fragment(R.layout.fragment_stock){

    private val stockViewModel : StockViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        requireView().recycler_view.apply {
            adapter = StockAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observe(){
        observeState()
        observeStocks()
    }

    private fun observeState() = stockViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeStocks() = stockViewModel.listenToStock().observe(viewLifecycleOwner, Observer { handleStocks(it) })
    private fun fetchStocks() = stockViewModel.fecthStock()

    private fun handleStocks(list: List<Stok>?) {
        list?.let {
            requireView().recycler_view.adapter?.let {adapter ->
                if (adapter is StockAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(stockState: StockState?) {
        stockState?.let {
            when(it){
                is StockState.Loading -> handleLoading(it.state)
                is StockState.ShowToast -> requireActivity().toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fetchStocks()
    }
}