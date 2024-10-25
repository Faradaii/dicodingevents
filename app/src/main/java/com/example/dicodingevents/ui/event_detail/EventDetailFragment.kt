package com.example.dicodingevents.ui.event_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicodingevents.R
import com.example.dicodingevents.data.Result
import com.example.dicodingevents.data.local.entity.DicodingEventEntity
import com.example.dicodingevents.databinding.FragmentEventDetailBinding
import com.example.dicodingevents.ui.ViewModelFactory
import com.example.dicodingevents.utils.DateTimeUtils

class EventDetailFragment : Fragment() {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EventDetailViewModel by viewModels {
            factory
        }

        viewModel.getDetailEvent(ARG_EVENT_ID).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showErrorState(isError = false)
                        showLoading(isLoading = true)
                    }
                    is Result.Success -> {
                        showLoading(isLoading = false)
                        showErrorState(isError = false)
                        val dicodingEvents = result.data
                        setEvent(dicodingEvents)

                        viewModel.viewedDicodingEvent(dicodingEvents)
                    }
                    is Result.Error -> {
                        showLoading(isLoading = false)
                        showErrorState(isError = true)
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setEvent(event: DicodingEventEntity) {
        with(binding) {
            tvEventName.text = event.name
            tvEventInfo.text = "This event ${if (DateTimeUtils.isUpcomingChecker(event.endTime)) "has not started yet!" else "has ended!"}"
            tvEventCategory.text = event.category
            btEventStatus.text = if (DateTimeUtils.isUpcomingChecker(event.endTime)) (if(event.quota - event.registrants >= 0) "Available" else "Full") else "Closed"
            btEventSlot.text = "${event.registrants}/${event.quota} (${event.quota - event.registrants} seat available)"
            tvEventTime.text = DateTimeUtils.formatDateWithTimezone(event.beginTime)
            tvEventOwner.text = SpannableStringBuilder("${event.ownerName} *").apply {
                setSpan(ImageSpan(root.context, R.drawable.baseline_verified_24), event.ownerName.length+1, event.ownerName.length+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvEventDescription.text = Html.fromHtml(event.description)
            btEventRegister.setOnClickListener {

                if (event.quota - event.registrants == 0) {
                    Toast.makeText(root.context, "Sorry, this event is full/closed", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                }
            }

            Glide.with(root.context)
                .load(event.mediaCover)
                .transform(RoundedCorners(50))
                .placeholder(R.drawable.baseline_image_placeholder_24).fitCenter()
                .error(R.drawable.baseline_broken_image_24).fitCenter()
                .into(ivEventMediaCover)

            Glide.with(root.context)
                .load(R.drawable.dicoding_logo)
                .transform(RoundedCorners(999))
                .placeholder(R.drawable.baseline_image_placeholder_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(ivLogoOwner)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.scView.visibility = View.GONE
            binding.errorStateContainer.errorStateContainer.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.scView.visibility = View.VISIBLE
        }
    }

    private fun showErrorState(isError: Boolean) {
        if (isError) {
            binding.scView.visibility = View.GONE
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
        private const val ARG_EVENT_ID_LABEL = "eventId"
        private var ARG_EVENT_ID = 0

        fun newInstance(eventId: Int): EventDetailFragment {
            ARG_EVENT_ID = eventId
            val fragment = EventDetailFragment()
            val args = Bundle()
            args.putInt(ARG_EVENT_ID_LABEL, ARG_EVENT_ID)
            fragment.arguments = args
            return fragment
        }
    }
}