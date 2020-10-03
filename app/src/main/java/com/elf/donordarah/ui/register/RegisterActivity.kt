package com.elf.donordarah.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.elf.donordarah.R
import com.elf.donordarah.ui.login.LoginActivity
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        observe()
        register()
        goToLogin()
    }

    private fun observe() = registerViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun goToLogin() {
        to_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun handleUiState(registerState: RegisterState?) {
        registerState?.let {
            when(it){
                is RegisterState.Loading -> handleLoading(it.state)
                is RegisterState.ShowToast -> toast(it.message)
                is RegisterState.Reset -> handleReset()
                is RegisterState.Validate -> handleValidate(it)
                is RegisterState.Success -> handleSuccess()
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        btn_regist.isEnabled = !b
        if (b) loading.visible() else loading.gone()
    }

    private fun handleValidate(validate: RegisterState.Validate) {
        validate.name?.let { setErrorName(it) }
        validate.email?.let { setErrorEmail(it) }
        validate.password?.let { setErrorPass(it) }
    }

    private fun handleReset() {
        setErrorName(null)
        setErrorEmail(null)
        setErrorPass(null)
    }

    private fun handleSuccess() = alertRegister("berhasil registere, silahkan login")
    private fun setErrorName(err : String?){ layout_name.error = err }
    private fun setErrorEmail(err : String?){ layout_email.error = err }
    private fun setErrorPass(err : String?){ layout_pass.error = err }

    private fun alertRegister(m : String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("ya"){dialogInterface, i ->
                goToLogin()
                finish()
                dialogInterface.dismiss()
            }
        }.show()
    }

    private fun register() {
        btn_regist.setOnClickListener {
            val name = nama.text.toString().trim()
            val email = email.text.toString().trim()
            val pass = password.text.toString().trim()
            val role = "a"
            if (registerViewModel.validate(name, email, pass)){
                registerViewModel.register(name, email, pass, role)
            }
        }
    }
}