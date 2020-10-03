package com.elf.donordarah.ui.submission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.donordarah.R
import com.elf.donordarah.models.Submission
import com.elf.donordarah.ui.add_edit_submission.AddEditSubmissionActivity
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.activity_submission.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubmissionActivity : AppCompatActivity() {

    private val submissionViewModel : SubmissionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission)
        setUpRecyclerView()
        goToAddSubmission()
        observe()
    }

    private fun goToAddSubmission(){
        fab.setOnClickListener {
            startActivity(Intent(this@SubmissionActivity, AddEditSubmissionActivity::class.java))
        }
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = SubmissionAdapter(mutableListOf(), this@SubmissionActivity)
            layoutManager = LinearLayoutManager(this@SubmissionActivity)
        }
    }

    private fun observe() {
        observeState()
        observeSubmissions()
    }

    private fun observeState() = submissionViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeSubmissions() = submissionViewModel.listenToSubmissions().observe(this, Observer { handleSubmissions(it) })
    private fun fetchSubmissions() = submissionViewModel.fetchSubmissions(Constants.getToken(this@SubmissionActivity))

    private fun handleSubmissions(list: List<Submission>?) {
        list?.let {
            recycler_view.adapter?.let {adapter ->
                if (adapter is SubmissionAdapter) adapter.changeList(it)
            }
        }
    }

    private fun handleUiState(state: SubmissionState?) {
        state?.let {
            when(it){
                is SubmissionState.Loading -> handleLoading(it.state)
                is SubmissionState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        fab.isEnabled = !b
        if (b) loading.visible() else loading.gone()
    }

    override fun onResume() {
        super.onResume()
        fetchSubmissions()
    }
}