package com.elf.donordarah.ui.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.elf.donordarah.R
import com.elf.donordarah.ui.intro.IntroActivity
import com.elf.donordarah.ui.main.donor.DonorFragment
import com.elf.donordarah.ui.main.home.HomeFragment
import com.elf.donordarah.ui.main.profile.ProfileFragment
import com.elf.donordarah.ui.main.schedulle.SchedulleFragment
import com.elf.donordarah.ui.main.stock.StockFragment
import com.elf.donordarah.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{ var navStatus = -1 }
    private var fragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ navigation.selectedItemId = R.id.action_home }
//        Thread(Runnable {
//            if (Constants.isFirstTime(this@MainActivity)) {
//                runOnUiThread { startActivity(Intent(this@MainActivity, IntroActivity::class.java).also {
//                    finish()
//                })}
//            }
//        }).start()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.action_home -> {
                if(navStatus != 0){
                    fragment = HomeFragment()
                    navStatus = 0
                }
            }
            R.id.action_jadwal -> {
                if(navStatus != 1){
                    fragment = SchedulleFragment()
                    navStatus = 1
                }
            }

            R.id.action_donor -> {
                if(navStatus != 2){
                    fragment = DonorFragment()
                    navStatus = 2
                }
            }

            R.id.action_stock -> {
                if(navStatus != 3){
                    fragment = StockFragment()
                    navStatus = 3
                }
            }

            R.id.action_profile -> {
                if(navStatus != 4){
                    fragment = ProfileFragment()
                    navStatus = 4
                }
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = HomeFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }
}