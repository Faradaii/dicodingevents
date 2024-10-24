package com.example.dicodingevents.ui.event_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dicodingevents.R
import com.example.dicodingevents.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {
    private lateinit var activityEventDetailBinding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityEventDetailBinding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(activityEventDetailBinding.root)

        val id = intent.getIntExtra(EXTRA_ID, 0)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Event Detail"


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.event_detail_container, EventDetailFragment.newInstance(id))
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}