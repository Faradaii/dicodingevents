package com.example.dicodingevents.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingevents.R

@SuppressLint("CustomSplashScreen")
class SplashScreenCustom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_custom)
        supportActionBar?.hide()
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainNavigation::class.java))
            finish()
        }, 2000)
    }
}