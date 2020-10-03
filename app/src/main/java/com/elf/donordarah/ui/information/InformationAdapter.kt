package com.elf.donordarah.ui.information

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.Information
import com.elf.donordarah.ui.detail_information.DetailInformationActivity
import kotlinx.android.synthetic.main.item_information.view.*

class InformationAdapter (private var informations : MutableList<Information>, private var context: Context)
    :RecyclerView.Adapter<InformationAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(information: Information, context: Context){
            with(itemView){
                title.text = information.title
                content.text = information.content
                image.load(information.image)
                setOnClickListener {
                    context.startActivity(Intent(context, DetailInformationActivity::class.java).apply {
                        putExtra("INFORMATION", information)
                    })
                }
            }
        }
    }

    fun changeList(c : List<Information>){
        informations.clear()
        informations.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_information, parent, false))
    }

    override fun getItemCount(): Int = informations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(informations[position], context)
}