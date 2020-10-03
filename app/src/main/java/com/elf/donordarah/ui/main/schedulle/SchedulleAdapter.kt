package com.elf.donordarah.ui.main.schedulle

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.Schedulle
import com.elf.donordarah.ui.detail_schedulle.DetailSchedulleActivity
import kotlinx.android.synthetic.main.item_schedulle.view.*

class SchedulleAdapter (private var schedulles : MutableList<Schedulle>, private var context: Context)
    : RecyclerView.Adapter<SchedulleAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(schedulle: Schedulle, context: Context){
            with(itemView){
                nama_tempat.text = schedulle.location
                alamat.text = schedulle.address
                tanggal.text = schedulle.date
                hari.text = schedulle.day
                jam_mulai.text = schedulle.start_time
                jam_selesai.text = schedulle.end_time
                setOnClickListener {
                    context.startActivity(Intent(context, DetailSchedulleActivity::class.java).apply {
                        putExtra("SCHEDULLE", schedulle)
                    })
                }
            }
        }
    }

    fun changeList(c : List<Schedulle>){
        schedulles.clear()
        schedulles.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_schedulle, parent, false))
    }

    override fun getItemCount(): Int = schedulles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(schedulles[position], context)
}