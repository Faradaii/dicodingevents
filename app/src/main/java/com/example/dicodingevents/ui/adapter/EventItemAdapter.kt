package com.example.dicodingevents.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicodingevents.R
import com.example.dicodingevents.ui.event_detail.EventDetailActivity
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.databinding.EventItemBinding

class EventItemAdapter : ListAdapter<DicodingEvent, EventItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
    class MyViewHolder(private val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventsItem: DicodingEvent){
            binding.tvEventName.text = eventsItem.name
            binding.tvEventSummary.text = eventsItem.summary
            binding.tvEventCityName.text = eventsItem.cityName
            Glide.with(binding.root.context)
                .load(eventsItem.imageLogo)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.baseline_image_placeholder_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(binding.ivEventImage)

            binding.root.setOnClickListener {
                val intent = Intent(it.context, EventDetailActivity::class.java)
                intent.putExtra(EventDetailActivity.EXTRA_ID, eventsItem.id)
                it.context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DicodingEvent>() {
            override fun areItemsTheSame(oldItem: DicodingEvent, newItem: DicodingEvent): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DicodingEvent, newItem: DicodingEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}