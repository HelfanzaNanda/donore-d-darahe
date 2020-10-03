package com.elf.donordarah.ui.add_edit_submission

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.elf.donordarah.R
import com.elf.donordarah.models.CreateSubmission
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.toast
import kotlinx.android.synthetic.main.activity_add_edit_submission.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class AddEditSubmissionActivity : AppCompatActivity(), OnDateSetListener {

    private val addEditSubmissionViewModel : AddEditSubmissionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_submission)
        setDateAndTime()
        observe()
        add()
    }

    private fun setDateAndTime(){
        openDatePicker()
        setStartTime()
        setEndTime()
    }

    private fun observe(){
        observeState()
        observeCurrentDate()
        observeCurrentDay()
        observeCurrentStartTime()
        observeCurrentEndTime()
    }

    private fun setStartTime() {
        jam_mulai.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker =
                TimePickerDialog(this@AddEditSubmissionActivity,
                    OnTimeSetListener { _, selectedHour, selectedMinute ->
                        setCurrentStartTime(selectedHour, selectedMinute)
                    }, hour, minute, true
                )
            mTimePicker.setTitle("Pilih Jam Selesai")
            mTimePicker.show()
        }
    }

    private fun setEndTime() {
        jam_selesai.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker =
                TimePickerDialog(this@AddEditSubmissionActivity,
                    OnTimeSetListener { _, selectedHour, selectedMinute ->
                        setCurrentEndTime(selectedHour, selectedMinute)
                    }, hour, minute, true
                )
            mTimePicker.setTitle("Pilih Jam Selesai")
            mTimePicker.show()
        }
    }

    private fun openDatePicker(){
        tanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }


    private fun add(){
        fab_simpan.setOnClickListener {
            val token  = Constants.getToken(this@AddEditSubmissionActivity)
            val loc = nama_tempat.text.toString().trim()
            val event = nama_acara.text.toString().trim()
            val participants =jumlah_peserta.text.toString().trim()
            val day = hari.text.toString().trim()
            val date = tanggal.text.toString().trim()
            val startTime = jam_mulai.text.toString().trim()
            val endTime = jam_selesai.text.toString().trim()
            val pj = penanggung_jawab.text.toString().trim()
            val address = alamat.text.toString().trim()
            val city = penanggung_jawab.text.toString().trim()
            if (addEditSubmissionViewModel.validate(loc, event, participants, date, startTime, endTime, pj, city, address)){
                val sub = CreateSubmission(location = loc, event = event, participants = participants, day = day,
                    date = date, start_time = startTime, end_time = endTime, person_in_charge = pj, address = address, city = city)
                addEditSubmissionViewModel.createSubmission(token, sub)
            }
        }
    }

    private fun setCalendarValue(calendar: Calendar) = addEditSubmissionViewModel.setCurrentDate(calendar)
    private fun setCurrentStartTime(hour: Int, minute: Int){ addEditSubmissionViewModel.setCurrentStartTime(hour, minute) }
    private fun setCurrentEndTime(hour: Int, minute: Int){ addEditSubmissionViewModel.setCurrentEndTime(hour, minute) }

    private fun observeState() = addEditSubmissionViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeCurrentDate() = addEditSubmissionViewModel.listenToCurrentDate().observe(this, Observer { handleCurrentDate(it) })
    private fun observeCurrentDay() = addEditSubmissionViewModel.listenToCurrentDay().observe(this, Observer { handleCurrentDay(it) })
    private fun observeCurrentStartTime() = addEditSubmissionViewModel.listenToCurrentStartTime().observe(this, Observer { handleCurrentStartTime(it) })
    private fun observeCurrentEndTime() = addEditSubmissionViewModel.listenToCurrentEndTime().observe(this, Observer { handleCurrentEndTime(it) })

    private fun handleUiState(state: AddEditSubmissionState?) {
        state?.let {
            when(it){
                is AddEditSubmissionState.Loading -> handleLoading(it.state)
                is AddEditSubmissionState.ShowToast -> toast(it.message)
                is AddEditSubmissionState.Reset -> handleReset()
                is AddEditSubmissionState.Success -> handleSuccess()
                is AddEditSubmissionState.Validate -> handleValidate(it)
            }
        }
    }

    private fun handleValidate(validate: AddEditSubmissionState.Validate) {
        validate.loc?.let { setErrLoc(it) }
        validate.event?.let { setErrEvent(it) }
        validate.participants?.let { setErrParcip(it) }
        validate.date?.let { setErrDate(it) }
        validate.startTime?.let { setErrStartTime(it) }
        validate.endTime?.let { setErrEndTime(it) }
        validate.pj?.let { setErrPj(it) }
        validate.address?.let { setErrAddress(it) }
    }

    private fun handleSuccess() {
        finish()
    }

    private fun handleReset() {
        setErrLoc(null)
        setErrEvent(null)
        setErrParcip(null)
        setErrDate(null)
        setErrStartTime(null)
        setErrEndTime(null)
        setErrPj(null)
        setErrAddress(null)
    }

    private fun handleLoading(b: Boolean) {
        fab_simpan.isEnabled = !b
    }

    private fun handleCurrentDay(s: String?) = setTextOfDayField(s)
    private fun handleCurrentDate(s: String?) = setTextOfDateField(s)
    private fun handleCurrentStartTime(s : String?) = setTextOfStartTime(s)
    private fun handleCurrentEndTime(s : String?) = setTextOfEndTime(s)

    private fun setTextOfDayField(s: String?) = if (s != null) hari.setText(s) else hari.text?.clear()
    private fun setTextOfDateField(s: String?) = if(s != null) tanggal.setText(s) else tanggal.text?.clear()
    private fun setTextOfStartTime(s : String?)  = if (s != null) jam_mulai.setText(s) else jam_mulai.text?.clear()
    private fun setTextOfEndTime(s : String?)  = if (s != null) jam_selesai.setText(s) else jam_selesai.text?.clear()

    private fun setErrLoc(err : String?){ layout_nama_tempat.error = err }
    private fun setErrEvent(err : String?){ nama_acara.error = err }
    private fun setErrParcip(err : String?){ jumlah_peserta.error = err }
    private fun setErrDate(err : String?){ tanggal.error = err }
    private fun setErrStartTime(err : String?){ jam_mulai.error = err }
    private fun setErrEndTime(err : String?){ jam_selesai.error = err }
    private fun setErrAddress(err : String?){ alamat.error = err }
    private fun setErrPj(err : String?){ penanggung_jawab.error = err }


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        setCalendarValue(calendar)
    }
}