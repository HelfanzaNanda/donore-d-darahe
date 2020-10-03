package com.elf.donordarah.ui.detail_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.News
import kotlinx.android.synthetic.main.activity_detail_news.*

class DetailNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news)
        setUpDetail()
    }

    private fun setUpDetail(){
        getPassedNews()?.let {
            image.load(it.image)
            txt_title.text = it.title
            content.text = it.content
        }
    }

    private fun getPassedNews() = intent.getParcelableExtra<News>("NEW")
}