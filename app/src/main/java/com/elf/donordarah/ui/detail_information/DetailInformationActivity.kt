package com.elf.donordarah.ui.detail_information

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.Information
import kotlinx.android.synthetic.main.activity_detail_information.*

class DetailInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_information)
        setUpDetail()
    }

    private fun setUpDetail(){
        getPassedInformation()?.let {
            image.load(it.image)
            txt_title.text = it.title
            content.text = it.content
            //tanggal.text = it.
        }
    }

    private fun getPassedInformation() = intent.getParcelableExtra<Information>("INFORMATION")
}