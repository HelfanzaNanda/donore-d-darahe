package com.elf.donordarah.utils.ext

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

fun Context.toast(m : String) = Toast.makeText(this, m, Toast.LENGTH_SHORT).show()

//fun Context.alertNotLogin(message: String){
//    AlertDialog.Builder(this).apply {
//        setMessage(message)
//        setPositiveButton("ya"){dialogInterface, _ ->
//            startActivity(Intent(this@alertNotLogin, LoginActivity::class.java).putExtra("EXPECT_RESULT", true))
//            dialogInterface.dismiss()
//        }
//    }.show()
//}

fun Context.alertBasic(message : String) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }.show()
}

