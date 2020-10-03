package com.elf.donordarah.ui.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elf.donordarah.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
    }
}