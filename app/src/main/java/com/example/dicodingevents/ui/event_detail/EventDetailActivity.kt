package com.example.dicodingevents.ui.event_detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.dicodingevents.R
import com.example.dicodingevents.databinding.ActivityEventDetailBinding
import com.example.dicodingevents.ui.MainNavigation
import com.example.dicodingevents.utils.SettingPreferences
import com.example.dicodingevents.utils.dataStore
import kotlinx.coroutines.launch

class EventDetailActivity : AppCompatActivity() {
    private lateinit var activityEventDetailBinding: ActivityEventDetailBinding
    private lateinit var settingPreferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingPreferences = SettingPreferences.getInstance(dataStore)

        lifecycleScope.launch {
            settingPreferences.getThemeSetting().collect { isDarkModeActive ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        activityEventDetailBinding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(activityEventDetailBinding.root)

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val name = intent.getStringExtra(EXTRA_NAME)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.event_detail_container, EventDetailFragment.newInstance(id))
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val isFromNotification = intent.getBooleanExtra("EXTRA_FROM_NOTIFICATION", false)

        if (isFromNotification) {
            val intent = Intent(this, MainNavigation::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
            return true
        } else {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
    }
}