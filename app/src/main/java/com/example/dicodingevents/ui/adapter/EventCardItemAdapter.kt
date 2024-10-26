package com.example.dicodingevents.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicodingevents.R
import com.example.dicodingevents.data.local.entity.DicodingEventEntity
import com.example.dicodingevents.databinding.EventCardItemBinding
import com.example.dicodingevents.ui.event_detail.EventDetailActivity
import com.example.dicodingevents.utils.DateTimeUtils

class EventCardItemAdapter : ListAdapter<DicodingEventEntity, EventCardItemAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: EventCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventsItem: DicodingEventEntity){
            binding.apply {
                tvEventName.text = eventsItem.name
                tvEventCardDate.text = DateTimeUtils.formatDateShorter(eventsItem.beginTime)
                tvEventCityName.text = buildString {
                    append(eventsItem.cityName)
                    append(" • ")
                    append(eventsItem.category)
                }
                tvEventParticipant.text = buildString {
                    append("+")
                    append(eventsItem.registrants-3)
                    append(" Participants Registered")
                }
                Glide.with(root.context)
                    .load(eventsItem.mediaCover)
                    .transform(RoundedCorners(60))
                    .placeholder(R.drawable.baseline_image_placeholder_24)
                    .error(R.drawable.baseline_broken_image_24)
                    .into(ivEventImage)

                root.setOnClickListener {
                    val intent = Intent(it.context, EventDetailActivity::class.java)
                    intent.putExtra(EventDetailActivity.EXTRA_NAME, eventsItem.name)
                    intent.putExtra(EventDetailActivity.EXTRA_ID, eventsItem.id)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DicodingEventEntity>() {
            override fun areItemsTheSame(oldItem: DicodingEventEntity, newItem: DicodingEventEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DicodingEventEntity, newItem: DicodingEventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}