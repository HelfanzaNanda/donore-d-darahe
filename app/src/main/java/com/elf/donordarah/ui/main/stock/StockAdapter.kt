package com.elf.donordarah.ui.main.stock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elf.donordarah.R
import com.elf.donordarah.models.Stok

class StockAdapter (private var stocks : MutableList<Stok>, private var context: Context)
    : RecyclerView.Adapter<StockAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(stok: Stok, context: Context){
            with(itemView){

            }
        }
    }

    fun changeList(c : List<Stok>){
        stocks.clear()
        stocks.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false))
    }

    override fun getItemCount(): Int = stocks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stocks[position], context)
}