package com.elf.donordarah.ui.main.schedulle

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.donordarah.R
import com.elf.donordarah.models.Schedulle
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_schedulle.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SchedulleFragment : Fragment(R.layout.fragment_schedulle){

    private val schedulleViewModel : SchedulleViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        requireView().recycler_view.apply {
            adapter = SchedulleAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun observe() {
        observeState()
        observeSchedulles()
    }

    private fun observeState() = schedulleViewModel.listenToState().observe(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeSchedulles() = schedulleViewModel.listenToSchedulles().observe(viewLifecycleOwner, Observer { handleSchedulles(it) })
    private fun fecthSchedulles() = schedulleViewModel.fecthSchedulles()

    private fun handleSchedulles(list: List<Schedulle>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is SchedulleAdapter){
                    adapter.changeList(it)
                }
            }
        }
    }

    private fun handleUiState(state: SchedulleState?) {
        state?.let {
            when(it){
                is SchedulleState.Loading -> handleLoading(it.state)
                is SchedulleState.ShowToast -> requireActivity().toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fecthSchedulles()
    }
}
