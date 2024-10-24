package com.example.dicodingevents.ui.home

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.R
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.databinding.FragmentHomeBinding
import com.example.dicodingevents.ui.adapter.EventCardItemAdapter
import com.example.dicodingevents.ui.adapter.EventItemAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

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
        if (Section.UPCOMING == section) {
            binding.includeListEventsCard.apply {
                rvEventsCard.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyStateContainer.emptyStateContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        } else {
            binding.includeListEvents.apply {
                rvEvents.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyStateContainer.emptyStateContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showErrorState(isError: Boolean, section: Enum<Section>) {
        if (Section.UPCOMING == section) {
            binding.includeListEventsCard.apply {
                errorStateContainer.errorStateContainer.visibility = if (isError) View.VISIBLE else View.GONE
                if (isError) {
                    rvEventsCard.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                }
            }
        } else {
            binding.includeListEvents.apply {
                errorStateContainer.errorStateContainer.visibility = if (isError) View.VISIBLE else View.GONE
                if (isError) {
                    rvEvents.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean, section: Enum<Section>) {
        if (Section.UPCOMING == section) {
            binding.includeListEventsCard.apply {
                loadingIndicatorEventsCard.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    rvEventsCard.visibility = View.GONE
                    errorStateContainer.errorStateContainer.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                } else {
                    rvEventsCard.visibility = View.VISIBLE
                }
            }
        } else {
            binding.includeListEvents.apply {
                loadingIndicatorEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    rvEvents.visibility = View.GONE
                    errorStateContainer.errorStateContainer.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                } else {
                    rvEvents.visibility = View.VISIBLE
                }
            }
        }
    }

    enum class Section {UPCOMING, FINISHED}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}