package com.elf.donordarah.ui.add_edit_submission

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import coil.api.load
import com.elf.donordarah.R
import com.elf.donordarah.models.CreateSubmission
import com.elf.donordarah.models.Submission
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.toast
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog
import kotlinx.android.synthetic.main.activity_add_edit_submission.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*


class AddEditSubmissionActivity : AppCompatActivity(), OnDateSetListener {

    private val addEditSubmissionViewModel : AddEditSubmissionViewModel by viewModel()
    companion object{ const val IMAGE_REQ = 101 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_submission)
        openPix()
        setDateAndTime()
        observe()
        createOrUpdate()
    }

    private fun setDateAndTime(){
        openDatePicker()
        jam_mulai.setOnClickListener { openTimePickerStartTime() }
        jam_selesai.setOnClickListener { openTimePickerEndTime() }
    }

    private fun observe(){
        observeState()
        observeCurrentDate()
        observeCurrentDay()
        observeCurrentStartTime()
        observeCurrentEndTime()
    }

    private fun openPix(){
        fabChoosePic.setOnClickListener {
            Pix.start(this@AddEditSubmissionActivity, Options.init().setRequestCode(IMAGE_REQ))
        }
    }

    private fun openTimePickerStartTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(this@AddEditSubmissionActivity, OnTimeSetListener { _, i, i2 ->
                if (i < 8) alertError("maaf, jam yang di masukan harus lebih dari jam 8", true)
                setCurrentStartTime(i, i2)
            }, hour, minute, true)
        mTimePicker.setTitle("Pilih Jam Selesai")
        mTimePicker.show()
    }

    private fun openTimePickerEndTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        val mTimePicker: TimePickerDialog
        mTimePicker =
            TimePickerDialog(this@AddEditSubmissionActivity, OnTimeSetListener { _, i, i2 ->
                if (i > 16) alertError("maaf, jam yang di masukan harus kurang dari jam 16 atau jam 4 sore", false)
                setCurrentEndTime(i, i2)
            }, hour, minute, true)
        mTimePicker.setTitle("Pilih Jam Selesai")
        mTimePicker.show()
    }

    private fun alertError(m: String, b : Boolean) {
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("ya"){dialogInterface, _ ->
                dialogInterface.dismiss()
                if (b) openTimePickerStartTime() else openTimePickerEndTime()

            }
        }.show()
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

    @SuppressLint("SetTextI18n")
    private fun createOrUpdate(){
        if (!getPassedUpdate()){
            txt_title.text = "Tambah Pengajuan"
            fab_simpan.setOnClickListener {
                val token  = Constants.getToken(this@AddEditSubmissionActivity)
                val image = getImageSelected()
                if (image.isNullOrEmpty()){
                    toast("masukan gambar dahulu")
                }else{
                    if (addEditSubmissionViewModel.validate(getField())){
                        addEditSubmissionViewModel.createSubmission(token, getField(), image)
                    }
                }

            }
        }else{
            txt_title.text = "Update Pengajuan"
            setUpDetailSubmission()
            fab_simpan.setOnClickListener {
                val token  = Constants.getToken(this@AddEditSubmissionActivity)
                val id = getPassedSubmission()?.id.toString()
                val image = getImageSelected()
                if (image.isNullOrEmpty()){
                    toast("masukan gambar dahulu")
                }else{
                    if (addEditSubmissionViewModel.validate(getField())){
                        addEditSubmissionViewModel.updateSubmission(token, id, getField(), image)
                    }
                }
            }
        }
    }

    private fun getField() : CreateSubmission {
        val loc = nama_tempat.text.toString().trim()
        val event = nama_acara.text.toString().trim()
        val participants = jumlah_peserta.text.toString().trim()
        val day = hari.text.toString().trim()
        val date = tanggal.text.toString().trim()
        val startTime = jam_mulai.text.toString().trim()
        val endTime = jam_selesai.text.toString().trim()
        val pj = penanggung_jawab.text.toString().trim()
        val address = alamat.text.toString().trim()
        val city = "Kota Tegal"
        val sub = CreateSubmission(location = loc, event = event, participants = participants, day = day,
            date = date, start_time = startTime, end_time = endTime, person_in_charge = pj, address = address,
            city = city)
        return sub
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDetailSubmission() {
        getPassedSubmission()?.let {
            nama_tempat.setText(it.location)
            nama_acara.setText(it.event)
            jumlah_peserta.setText(it.participants)
            hari.setText(it.day)
            tanggal.setText(it.date)
            jam_mulai.setText(it.start_time)
            jam_selesai.setText(it.end_time)
            penanggung_jawab.setText(it.person_in_charge)
            alamat.setText(it.address)
            foto.load(File(it.image))
            city.setText("Kota Tegal")
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
    private fun getImageSelected() = addEditSubmissionViewModel.listenToImageSelected().value
    //private fun observeImageSelected() = addEditSubmissionViewModel.listenToImageSelected().observe(this, Observer { handleImageSelected(it) })

    private fun handleUiState(state: AddEditSubmissionState?) {
        state?.let {
            when(it){
                is AddEditSubmissionState.Loading -> handleLoading(it.state)
                is AddEditSubmissionState.ShowToast -> toast(it.message)
                is AddEditSubmissionState.Reset -> handleReset()
                is AddEditSubmissionState.Success -> alert("berhasil")
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

    private fun alert(m : String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("ya"){dialogInterface, i ->
                dialogInterface.dismiss()
                finish()
            }
        }.show()
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

    private fun getPassedSubmission() = intent.getParcelableExtra<Submission>("SUBMISSION")
    private fun getPassedUpdate() = intent.getBooleanExtra("UPDATE", false)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null){
            val image = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            image?.let {
                addEditSubmissionViewModel.setImageSelected(it[0])
                foto.load(File(it[0]))
            }

        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        setCalendarValue(calendar)
    }
}