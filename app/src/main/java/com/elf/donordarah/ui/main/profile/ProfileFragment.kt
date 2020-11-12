package com.elf.donordarah.ui.main.profile

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.User
import com.elf.donordarah.ui.login.LoginActivity
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import com.fxn.pix.Options
import com.fxn.pix.Pix
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class ProfileFragment : Fragment(R.layout.fragment_profile){

    private val profileViewModel : ProfileViewModel by viewModel()
    companion object{ private const val CODE_PIX = 101 }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout()
        openPix()
        observe()
        currentUser()
        updateUser()
    }

    private fun observe(){
        observeState()
        observeCurrentUser()
    }

    private fun observeState() = profileViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCurrentUser() = profileViewModel.listenCurrentUser().observe(viewLifecycleOwner, Observer { handleCurrentUser(it) })

    private fun handleCurrentUser(user: User?) {
        user?.let {
            requireView().nama.setText(it.nama)
            requireView().email.setText(it.email)
            if (it.image.isNullOrEmpty()){
                requireView().profile_image.load(R.drawable.ic_user_black)
            }else{
                requireView().profile_image.load("uploads/${it.image}")
            }
            //requireView().phone.setText(it.)
            requireView().role.setText(it.role)
        }
    }

    private fun handleUiState(state: ProfileState?) {
        state?.let {
            when(it){
                is ProfileState.Loading -> handleLoading(it.state)
                is ProfileState.ShowToast -> requireActivity().toast(it.message)
                is ProfileState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {
        requireActivity().toast("berhasil")
        currentUser()
    }

    private fun handleLoading(state: Boolean) {
        requireView().btn_logout.isEnabled = !state
        requireView().btn_foto.isEnabled = !state
        requireView().fab_simpanProfile.isEnabled = !state
        if (state) requireView().loading.visible() else requireView().loading.gone()
    }

    private fun logout(){
        requireView().btn_logout.setOnClickListener {
            Constants.clearToken(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun updateUser(){
        requireView().fab_simpanProfile.setOnClickListener {
            val token = Constants.getToken(requireActivity())
            val name = requireView().nama.text.toString().trim()
            val user = User(nama = name)
            profileViewModel.updateProfile(token, user)
        }
    }

    private fun openPix() {
        requireView().btn_foto.setOnClickListener {
            val opt = Options.init().apply {
                requestCode = CODE_PIX
                count = 1
            }
            Pix.start(this@ProfileFragment, opt)
        }
    }
    private fun currentUser() = profileViewModel.fetchCurrentUser(Constants.getToken(requireActivity()))

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_PIX) {
            val returnValue = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            returnValue?.let {
                requireView().profile_image.load(File(it[0]))
                val token = Constants.getToken(requireActivity())
                profileViewModel.updatePhoto(token, it[0])
            }
            println(returnValue)
        }
    }
}