package com.example.dicodingevents.ui.event_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dicodingevents.R
import com.example.dicodingevents.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {
    private lateinit var activityEventDetailBinding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
    }
}