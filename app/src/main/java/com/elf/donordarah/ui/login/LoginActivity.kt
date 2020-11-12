package com.elf.donordarah.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.elf.donordarah.R
import com.elf.donordarah.ui.main.MainActivity
import com.elf.donordarah.ui.register.RegisterActivity
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel : LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        observe()
        doLogin()
        goToRegister()

    }

    private fun observe() = loginViewModel.listenToState().observer(this, Observer { handleUIState(it) })
    private fun goToRegister(){
        link_regist.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun handleUIState(loginState: LoginState?) {
        loginState?.let {
            when(it){
                is LoginState.Loading -> handleLoading(it.state)
                is LoginState.ShowToast -> toast(it.message)
                is LoginState.Success -> handleSuccess(it.token, it.role)
                is LoginState.Reset -> handleReset()
                is LoginState.Validate -> handleValidate(it)
            }
        }
    }

    private fun handleSuccess(token: String, role : String) {
        Constants.setToken(this@LoginActivity, "Bearer $token")
        Constants.setRole(this@LoginActivity, role)
        startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }).also { finish() }

    }

    private fun handleLoading(b: Boolean) {
        btn_login.isEnabled = !b
        if (b) loading.visible() else loading.gone()
    }

    private fun handleValidate(validate: LoginState.Validate) {
        validate.email?.let { setEmailErr(it) }
        validate.password?.let { setPasswordErr(it) }
    }

    private fun handleReset() {
        setEmailErr(null)
        setPasswordErr(null)
    }

    private fun doLogin() {
        btn_login.setOnClickListener {
            val email = email.text.toString().trim()
            val pass = password.text.toString().trim()
            if (loginViewModel.validate(email, pass)){
                loginViewModel.login(email, pass)
            }
        }
    }

    private fun setEmailErr(err : String?) { layoutemail.error = err }
    private fun setPasswordErr(err : String?) { layoutpassword.error = err }

    override fun onResume() {
        super.onResume()
        if (Constants.getToken(this@LoginActivity) != "UNDEFINED"){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}