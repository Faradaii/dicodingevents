package com.example.dicodingevents.ui.event_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicodingevents.R
import com.example.dicodingevents.data.response.DicodingEvent

import com.example.dicodingevents.databinding.FragmentEventDetailBinding
import com.example.dicodingevents.utils.DateTimeUtils

class EventDetailFragment : Fragment() {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val eventDetailViewModel =
            ViewModelProvider(this).get(EventDetailViewModel::class.java)

        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val eventId = arguments?.getInt(ARG_EVENT_ID) ?: 0

        if (eventId != 0 && savedInstanceState == null) {
            eventDetailViewModel.getDetailEvent(eventId)
        }

        eventDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        eventDetailViewModel.isError.observe(viewLifecycleOwner) { isError ->
            showErrorState(isError)
        }

        eventDetailViewModel.detailEvent.observe(viewLifecycleOwner) {
            setEvent(it)
        }

        return root
    }

    @SuppressLint("SetTextI18n")
    private fun setEvent(event: DicodingEvent) {
        binding.tvEventName.text = event.name
        binding.tvEventCategory.text = event.category
        binding.btEventStatus.text = if (event.quota - event.registrants == 0) "Full" else "Available"
        binding.btEventSlot.text = "${event.registrants}/${event.quota} (${event.quota - event.registrants} seat available)"
        binding.tvEventDate.text = "▶️: ${DateTimeUtils.formatDateWithTimezone(event.beginTime)}\n⏹️: ${DateTimeUtils.formatDateWithTimezone(event.endTime)}"
        binding.tvEventOwner.text = SpannableStringBuilder("${event.ownerName} *").apply {
            setSpan(ImageSpan(binding.root.context, R.drawable.baseline_verified_24), event.ownerName.length+1, event.ownerName.length+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvEventDescription.text = Html.fromHtml(event.description)
        binding.btEventRegister.setOnClickListener {

            if (event.quota - event.registrants == 0) {
                Toast.makeText(binding.root.context, "Sorry, this event is full/closed", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(intent)
            }
        }

        Glide.with(binding.root.context)
            .load(event.mediaCover)
            .transform(RoundedCorners(50))
            .placeholder(R.drawable.baseline_image_placeholder_24)
            .error(R.drawable.baseline_broken_image_24)
            .into(binding.ivEventMediaCover)

        Glide.with(binding.root.context)
            .load(R.drawable.dicoding_logo)
            .transform(RoundedCorners(999))
            .placeholder(R.drawable.baseline_image_placeholder_24)
            .error(R.drawable.baseline_broken_image_24)
            .into(binding.ivLogoOwner)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.scView.visibility = View.GONE
            binding.bottomNavigationView.visibility = View.GONE
            binding.errorStateContainer.errorStateContainer.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.scView.visibility = View.VISIBLE
            binding.bottomNavigationView.visibility = View.VISIBLE
        }
    }

    private fun showErrorState(isError: Boolean) {
        if (isError) {
            binding.scView.visibility = View.GONE
            binding.bottomNavigationView.visibility = View.GONE
            binding.errorStateContainer.errorStateContainer.visibility = View.VISIBLE
        } else {
            binding.errorStateContainer.errorStateContainer.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_EVENT_ID = "eventId"

        fun newInstance(eventId: Int): EventDetailFragment {
            val fragment = EventDetailFragment()
            val args = Bundle()
            args.putInt(ARG_EVENT_ID, eventId)
            fragment.arguments = args
            return fragment
        }
    }

}