package com.elf.donordarah.ui.news

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.News
import com.elf.donordarah.ui.detail_news.DetailNewsActivity
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter (private var news : MutableList<News>, private var context: Context)
    :RecyclerView.Adapter<NewsAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(new : News, context: Context){
            with(itemView){
                title.text = new.title
                content.text = new.content
                image.load(new.image)
                setOnClickListener {
                    context.startActivity(Intent(context, DetailNewsActivity::class.java).apply {
                        putExtra("NEW", new)
                    })
                }
            }
        }
    }

    fun changeList(c : List<News>){
        news.clear()
        news.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(news[position], context)
}