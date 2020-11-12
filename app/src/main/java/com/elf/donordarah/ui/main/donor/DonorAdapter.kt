package com.elf.donordarah.ui.main.donor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elf.donordarah.R
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.ui.add_edit_donor.AddEditDonorActivity
import kotlinx.android.synthetic.main.item_donor.view.*

class DonorAdapter (private var donors : MutableList<Pendonor>, private var context: Context)
    :RecyclerView.Adapter<DonorAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(user: Pendonor, context: Context){
            with(itemView){
                nama.text = user.nama
                jenis_kelamin.text = user.gender
                if (user.blood_type == null || user.rhesus == null){
                    gol_rhesus.text = "-"
                }else{
                    gol_rhesus.text = "${user.blood_type} ${user.rhesus}"
                }
                alamat.text = user.address
                setOnClickListener {
                    context.startActivity(Intent(context, AddEditDonorActivity::class.java).apply {
                        putExtra("DONOR", user)
                        putExtra("UPDATE", true)
                    })
                }
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
