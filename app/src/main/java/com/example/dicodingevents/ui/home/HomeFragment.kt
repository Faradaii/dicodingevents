package com.example.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicodingevents.R
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.databinding.FragmentHomeBinding
import com.example.dicodingevents.ui.adapter.EventCardItemAdapter
import com.example.dicodingevents.ui.adapter.EventItemAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.isUpcomingLoading.observe(viewLifecycleOwner) {
            showLoading(it, Section.UPCOMING)
        }

        homeViewModel.isFinishedLoading.observe(viewLifecycleOwner) {
            showLoading(it, Section.FINISHED)
        }

        homeViewModel.isUpcomingError.observe(viewLifecycleOwner) {
            showErrorState(it, Section.UPCOMING)
        }

        homeViewModel.isFinishedError.observe(viewLifecycleOwner) {
            showErrorState(it, Section.FINISHED)
        }

        homeViewModel.isUpcomingEmpty.observe(viewLifecycleOwner) {
            showEmptyState(it, Section.UPCOMING)
        }

        homeViewModel.isFinishedEmpty.observe(viewLifecycleOwner) {
            showEmptyState(it, Section.FINISHED)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.includeListEvents.rvEvents.layoutManager = layoutManager

        val layoutManagerCard = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.includeListEventsCard.rvEventsCard.layoutManager = layoutManagerCard

        homeViewModel.listFinished.observe(requireActivity()) { dicodingevent ->
            setEventsData(dicodingevent)
        }

        homeViewModel.listUpcoming.observe(requireActivity()) { dicodingevent ->
            setEventsCardData(dicodingevent)
        }

        Glide.with(binding.root.context).load(R.drawable.dicoding_logo).transform(RoundedCorners(50)).into(binding.ivUserProfile)

        return root
    }

    private fun setEventsData(dicodingEvents: List<DicodingEvent>){
        val adapter = EventItemAdapter()
        adapter.submitList(dicodingEvents)
        binding.includeListEvents.rvEvents.adapter = adapter
    }

    private fun setEventsCardData(dicodingEvents: List<DicodingEvent>){
        val adapter = EventCardItemAdapter()
        adapter.submitList(dicodingEvents)
        binding.includeListEventsCard.rvEventsCard.adapter = adapter
    }

    private fun showEmptyState(isEmpty: Boolean, section: Enum<Section>) {
        if (isEmpty) {
            if (Section.UPCOMING == section) {
                binding.includeListEventsCard.rvEventsCard.visibility = View.GONE
                binding.includeListEventsCard.emptyStateContainer.emptyStateContainer.visibility = View.VISIBLE
            } else {
                binding.includeListEvents.rvEvents.visibility = View.GONE
                binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.VISIBLE
            }
        } else {
            if (Section.UPCOMING == section) {
                binding.includeListEventsCard.rvEventsCard.visibility = View.VISIBLE
                binding.includeListEventsCard.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            } else {
                binding.includeListEvents.rvEvents.visibility = View.VISIBLE
                binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            }
        }
    }

    private fun showErrorState(isError: Boolean, section: Enum<Section>) {
        if (isError) {
            if (Section.UPCOMING == section) {
                binding.includeListEventsCard.rvEventsCard.visibility = View.GONE
                binding.includeListEventsCard.errorStateContainer.errorStateContainer.visibility = View.VISIBLE
                binding.includeListEventsCard.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            } else {
                binding.includeListEvents.rvEvents.visibility = View.GONE
                binding.includeListEvents.errorStateContainer.errorStateContainer.visibility = View.VISIBLE
                binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            }
        } else {
            if (Section.UPCOMING == section) {
                binding.includeListEventsCard.errorStateContainer.errorStateContainer.visibility = View.GONE
                binding.includeListEventsCard.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            } else {
                binding.includeListEvents.errorStateContainer.errorStateContainer.visibility = View.GONE
                binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean, section: Enum<Section>) {
        if (isLoading) {
            if (Section.UPCOMING == section) {
                binding.includeListEventsCard.loadingIndicatorEventsCard.visibility = View.VISIBLE
                binding.includeListEventsCard.rvEventsCard.visibility = View.GONE
                binding.includeListEventsCard.errorStateContainer.errorStateContainer.visibility = View.GONE
                binding.includeListEventsCard.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            } else {
                binding.includeListEvents.loadingIndicatorEvents.visibility = View.VISIBLE
                binding.includeListEvents.rvEvents.visibility = View.GONE
                binding.includeListEvents.errorStateContainer.errorStateContainer.visibility = View.GONE
                binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            }
        } else {
            if (Section.UPCOMING == section) {
                binding.includeListEventsCard.loadingIndicatorEventsCard.visibility = View.GONE
                binding.includeListEventsCard.rvEventsCard.visibility = View.VISIBLE
            } else {
                binding.includeListEvents.loadingIndicatorEvents.visibility = View.GONE
                binding.includeListEvents.rvEvents.visibility = View.VISIBLE
            }
        }
    }

    enum class Section {UPCOMING, FINISHED}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}