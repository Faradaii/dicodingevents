package com.example.dicodingevents.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.dicodingevents.R
import com.example.dicodingevents.utils.SettingPreferences
import com.example.dicodingevents.utils.dataStore
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenCustom : AppCompatActivity() {

    private lateinit var settingPreferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_custom)

        settingPreferences = SettingPreferences.getInstance(dataStore)

        supportActionBar?.hide()
        lifecycleScope.launch {
            settingPreferences.getThemeSetting().collect { isDarkModeActive ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainNavigation::class.java))
            finish()
        }, SPLASH_DURATION)
    }

    companion object {
        private const val SPLASH_DURATION = 1000L
    }
}