package com.elf.donordarah.ui.detail_schedulle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.Schedulle
import kotlinx.android.synthetic.main.activity_detail_schedulle.*

class DetailSchedulleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedulle)
        setUpDetail()
    }

    private fun setUpDetail(){
        getPassedSchedulle()?.let {
            foto.load(it.image)
            nama_tempat.text = it.location
            hari.text = it.day
            tanggal.text = it.date
            jam_mulai.text = it.start_time
            jam_selesai.text = it.end_time
            alamat.text = it.address
        }
    }

    private fun getPassedSchedulle() = intent.getParcelableExtra<Schedulle>("SCHEDULLE")
}