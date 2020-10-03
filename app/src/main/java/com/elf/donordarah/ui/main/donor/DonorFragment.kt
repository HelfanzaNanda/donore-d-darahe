package com.elf.donordarah.ui.main.donor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.donordarah.R
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.fragment_donor.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DonorFragment : Fragment(R.layout.fragment_donor){

    private val donorViewModel : DonorViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        requireView().recycler_view.apply {
            adapter = DonorAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }


    private fun observe() {
        observeState()
        observeDonors()
    }

    private fun observeState() = donorViewModel.listenToState().observe(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeDonors() = donorViewModel.listenToDonors().observe(viewLifecycleOwner, Observer { handleDonors(it) })
    private fun fetchDonors()  = donorViewModel.fecthDonors(Constants.getToken(requireActivity()))

    private fun handleDonors(list: List<Pendonor>?) {
        list?.let {
            requireView().recycler_view.adapter?.let {adapter ->
                if (adapter is DonorAdapter) adapter.changeList(it)
            }
        }
    }

    private fun handleUiState(donorState: DonorState?) {
        donorState?.let {
            when(it){
                is DonorState.Loading -> handleLoading(it.state)
                is DonorState.ShowToast -> requireActivity().toast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b) requireView().loading.visible() else requireView().loading.gone()
    }


    override fun onResume() {
        super.onResume()
        fetchDonors()
    }
}