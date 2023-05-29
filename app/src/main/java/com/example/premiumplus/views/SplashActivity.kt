package com.example.premiumplus.views

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.premiumplus.R

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE)
        val isUserLogedIn = sharedPreferences.getBoolean("isLogin",false)
        Handler().postDelayed({
            startActivity(Intent(this, if (isUserLogedIn) MainActivity::class.java else LoginActivity::class.java))
            finish()
        }, 2000)
    }
}