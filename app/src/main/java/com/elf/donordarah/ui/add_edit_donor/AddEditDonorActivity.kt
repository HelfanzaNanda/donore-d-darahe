package com.elf.donordarah.ui.add_edit_donor

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.elf.donordarah.R
import com.elf.donordarah.models.CreatePendonor
import com.elf.donordarah.models.Pendonor
import com.elf.donordarah.models.UserCapil
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.toast
import kotlinx.android.synthetic.main.activity_add_edit_donor.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class AddEditDonorActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private val addEditDonorViewModel :AddEditDonorViewModel by viewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_donor)
        openDatePicker()
        observe()
        createOrUpdate()
        searchNik()
    }

    private fun observe() {
        observeState()
        observeCurrentDate()
        observePendonor()
    }

    private fun openDatePicker(){
        tanggal_lahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }


    private fun setCalendarValue(calendar: Calendar) = addEditDonorViewModel.setCurrentDate(calendar)
    private fun observeState() = addEditDonorViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeCurrentDate() = addEditDonorViewModel.listenToCurrentDate().observe(this, Observer { handleCurrentDate(it) })
    private fun observePendonor() = addEditDonorViewModel.listenToPendonor().observe(this, Observer { handlePendonor(it) })

    private fun handlePendonor(userCapil: UserCapil?) {
        userCapil?.let {
            ktp.setText(it.nik)
            nama.setText(it.namaLengkap)
            alamat.setText(it.alamat)
            jenis_kelamin.setText(it.jenisKelamin)
            tempat_lahir.setText(it.tempatLahir)
            tanggal_lahir.setText(it.tanggalLahir)
            pekerjaan.setText(it.pekerjaan)
            nama_ibu.setText(it.namaIbu)
            status_nikah.setText(it.statusKawin)
            when{
                it.golDar!!.equals("TIDAK TAHU") -> gol_dar.setSelection(0)
                it.golDar!!.equals("A") -> gol_dar.setSelection(1)
                it.golDar!!.equals("B") -> gol_dar.setSelection(2)
                it.golDar!!.equals("O") -> gol_dar.setSelection(3)
                it.golDar!!.equals("AB") -> gol_dar.setSelection(4)
            }
        }
    }

    private fun handleUiState(state: AddEditDonorState?) {
        state?.let {
            when(it){
                is AddEditDonorState.Loading -> handleLoading(it.state)
                is AddEditDonorState.ShowToast -> toast(it.message)
                is AddEditDonorState.Reset -> handleReset()
                is AddEditDonorState.Success -> alert("berhasil")
                is AddEditDonorState.Validate -> handleValidate(it)
            }
        }
    }

    private fun handleValidate(validate: AddEditDonorState.Validate) {
        validate.ktp?.let { setErrKtp(it) }
        validate.name?.let { setErrName(it) }
        validate.address?.let { setErrAddress(it) }
        validate.nameMother?.let { setErrNameMother(it) }
        validate.place?.let { setErrPlace(it) }
        validate.ttl?.let { setErrTTL(it) }
        validate.phone?.let { setErrPhone(it) }
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
        setErrPhone(null)
        setErrNameMother(null)
        setErrKtp(null)
        setErrName(null)
        setErrPlace(null)
        setErrTTL(null)
        setErrAddress(null)
    }

    private fun handleLoading(b: Boolean) {
        fab_simpan.isEnabled = !b
    }


    @SuppressLint("SetTextI18n")
    private fun createOrUpdate(){
        if (!getPassedUpdate()){
            txt_title.text = "Tambah Donor"
            fab_simpan.setOnClickListener {
                val token  = Constants.getToken(this@AddEditDonorActivity)
                if (addEditDonorViewModel.validate(getField())){
                    addEditDonorViewModel.createPendonor(token, getField())
                }

            }
        }else{
            txt_title.text = "Update Donor"
            setUpDetailSubmission()
            fab_simpan.setOnClickListener {
                val token  = Constants.getToken(this@AddEditDonorActivity)
                val id = getPassedDonor()?.id.toString()
                if (addEditDonorViewModel.validate(getField())){
                    addEditDonorViewModel.updateSubmission(token, id, getField())
                }
            }
        }
    }

    private fun getField() : CreatePendonor {
        val ktp = ktp.text.toString().trim()
        val nama = nama.text.toString().trim()
        val address = alamat.text.toString().trim()
        val gender = jenis_kelamin.text.toString().trim()
        val place = tempat_lahir.text.toString().trim()
        val ttl = tanggal_lahir.text.toString().trim()
        val work = pekerjaan.text.toString().trim()
        val motherName = nama_ibu.text.toString().trim()
        val status = status_nikah.text.toString().trim()
        val phone = phone.text.toString().trim()
        val goldar = gol_dar.selectedItem

        val createPendonor = CreatePendonor(ktp = ktp, nama = nama, address = address, gender = gender,
            place_of_birth = place, date_of_birth = ttl, working = work, mother_name = motherName,
            status = status, phone = phone,
            blood_type = if (goldar == "tidak tahu")  null else goldar.toString())
        return createPendonor
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDetailSubmission()
    {
        getPassedDonor()?.let {
            ktp.setText(it.ktp)
            nama.setText(it.nama)
            alamat.setText(it.address)
            tempat_lahir.setText(it.place_of_birth)
            tanggal_lahir.setText(it.date_of_birth)
            nama_ibu.setText(it.mother_name)
            phone.setText(it.phone)
        }
    }

    private fun searchNik(){
        btn_cari.setOnClickListener {
            val nik = search_ktp.text.toString().trim()
            if (nik.isNullOrEmpty()){
                toast("nik tidak boleh kosong")
            }else{
                addEditDonorViewModel.fetchUser(nik)
            }
        }
    }



    private fun handleCurrentDate(s: String?) = setTextOfDateField(s)
    private fun getPassedDonor() = intent.getParcelableExtra<Pendonor>("DONOR")
    private fun getPassedUpdate() = intent.getBooleanExtra("UPDATE", false)
    
    private fun setTextOfDateField(s: String?) = if(s != null) tanggal_lahir.setText(s) else tanggal_lahir.text?.clear()
    private fun setErrKtp(err : String?) { layout_ktp.error = err }
    private fun setErrName(err : String?) { layout_nama.error = err }
    private fun setErrAddress(err : String?) { layout_alamat.error = err }
    private fun setErrPlace(err : String?) { tempat_lahir.error = err }
    private fun setErrTTL(err : String?) { layout_ttl.error = err }
    private fun setErrNameMother(err : String?) { layout_nama_ibu.error = err }
    private fun setErrPhone(err : String?) { layout_phone.error = err }
    
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        setCalendarValue(calendar)
    }
}