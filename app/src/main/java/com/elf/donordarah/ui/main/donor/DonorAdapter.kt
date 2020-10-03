package com.elf.donordarah.ui.main.donor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elf.donordarah.R
import com.elf.donordarah.models.Pendonor

class DonorAdapter (private var donors : MutableList<Pendonor>, private var context: Context)
    :RecyclerView.Adapter<DonorAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(user: Pendonor, context: Context){
            with(itemView){

            }
        }
    }

    fun changeList(c : List<Pendonor>){
        donors.clear()
        donors.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_donor, parent, false))
    }

    override fun getItemCount(): Int = donors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(donors[position], context)
}
