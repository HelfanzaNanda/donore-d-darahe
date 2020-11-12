package com.elf.donordarah.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.elf.donordarah.R
import com.elf.donordarah.ui.information.InformationActivity
import com.elf.donordarah.ui.news.NewsActivity
import com.elf.donordarah.ui.submission.SubmissionActivity
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.alertBasic
import com.elf.donordarah.utils.ext.enabled
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(R.layout.fragment_home){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToActivity()
        checkRole()
    }

    private fun goToActivity(){
        goToNews()
        goToInformation()
    }

    private fun goToNews() {
        requireView().link_berita.setOnClickListener {
            startActivity(Intent(requireActivity(), NewsActivity::class.java))
        }
    }

    private fun goToInformation(){
        requireView().link_informasi.setOnClickListener {
            startActivity(Intent(requireActivity(), InformationActivity::class.java))
        }
    }

    private fun goToSubmission(){
        requireView().link_pengajuan.setOnClickListener {
            startActivity(Intent(requireActivity(), SubmissionActivity::class.java))
        }
    }

    private fun checkRole(){
        if (role() == "Pendonor"){
            requireView().link_pengajuan.setOnClickListener {
                requireActivity().alertBasic("anda bukan instansi")
            }
        }else{
            requireView().link_pengajuan.enabled()
            goToSubmission()
        }
    }

    private fun role() = Constants.getRole(requireActivity())
}