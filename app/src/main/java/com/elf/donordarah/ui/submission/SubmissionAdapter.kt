package com.elf.donordarah.ui.submission

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elf.donordarah.R
import com.elf.donordarah.models.Submission
import com.elf.donordarah.ui.add_edit_submission.AddEditSubmissionActivity
import kotlinx.android.synthetic.main.item_submission.view.*

class SubmissionAdapter (private val submission : MutableList<Submission>, private var context: Context)
    : RecyclerView.Adapter<SubmissionAdapter.ViewHolder>(){
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(submission: Submission, context: Context){
            with(itemView){
                nama_tempat.text = submission.location
                hari.text = submission.day
                tanggal.text = submission.date
                jam_mulai.text = submission.start_time
                jam_selesai.text = submission.end_time
                setOnClickListener {
                    context.startActivity(Intent(context, AddEditSubmissionActivity::class.java).apply {
                        putExtra("SUBMISSION", submission)
                        putExtra("UPDATE", true)
                    })
                }

            }
        }
    }

    fun changeList(c : List<Submission>){
        submission.clear()
        submission.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_submission, parent, false))
    }

    override fun getItemCount(): Int = submission.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(submission[position], context)
}