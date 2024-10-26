package com.example.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.Result
import com.example.dicodingevents.data.local.entity.DicodingEventEntity
import com.example.dicodingevents.databinding.FragmentHomeBinding
import com.example.dicodingevents.ui.ViewModelFactory
import com.example.dicodingevents.ui.adapter.EventCardItemAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showErrorState(isError = false, Section.UPCOMING)
                        showEmptyState(isEmpty = false, Section.UPCOMING)
                        showLoading(isLoading = true, Section.UPCOMING)
                    }
                    is Result.Success -> {
                        val dicodingEvents = result.data
                        showLoading(isLoading = false, Section.UPCOMING)
                        showErrorState(isError = false, Section.UPCOMING)
                        if (dicodingEvents.isEmpty()) {
                            showEmptyState(isEmpty = true, Section.UPCOMING)
                        } else {
                            setEventsCardDataHorizontal(dicodingEvents)
                        }
                    }
                    is Result.Error -> {
                        showLoading(isLoading = false, Section.UPCOMING)
                        showEmptyState(isEmpty = false, Section.UPCOMING)
                        showErrorState(isError = true, Section.UPCOMING)
                        Toast.makeText(
                            context,
                            "Please check your connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showErrorState(isError = false, Section.FINISHED)
                        showEmptyState(isEmpty = false, Section.FINISHED)
                        showLoading(isLoading = true, Section.FINISHED)
                    }
                    is Result.Success -> {
                        val dicodingEvents = result.data
                        showLoading(isLoading = false, Section.FINISHED)
                        showErrorState(isError = false, Section.FINISHED)
                        if (dicodingEvents.isEmpty()) {
                            showEmptyState(isEmpty = true, Section.FINISHED)
                        } else {
                            setEventsCardDataVertical(dicodingEvents)
                        }
                    }
                    is Result.Error -> {
                        showLoading(isLoading = false, Section.FINISHED)
                        showEmptyState(isEmpty = false, Section.FINISHED)
                        showErrorState(isError = true, Section.FINISHED)
                        Toast.makeText(
                            context,
                            "Please check your connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.listEventsCardHorizontal.rvEventsCard.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.listEventsCardVertical.rvEventsCard.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        }
    }

    private fun setEventsCardDataHorizontal(dicodingEvents: List<DicodingEventEntity>){
        if (dicodingEvents.isEmpty()) {
            showEmptyState(isEmpty = true, Section.UPCOMING)
        }

        val adapter = EventCardItemAdapter()
        adapter.submitList(dicodingEvents)
        binding.listEventsCardHorizontal.rvEventsCard.adapter = adapter

    }

    private fun setEventsCardDataVertical(dicodingEvents: List<DicodingEventEntity>){
        val adapter = EventCardItemAdapter()
        adapter.submitList(dicodingEvents)
        binding.listEventsCardVertical.rvEventsCard.adapter = adapter

    }

    private fun showEmptyState(isEmpty: Boolean, section: Enum<Section>) {
        if (Section.UPCOMING == section) {
            binding.listEventsCardHorizontal.apply {
                rvEventsCard.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyStateContainer.emptyStateContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        } else {
            binding.listEventsCardVertical.apply {
                rvEventsCard.visibility = if (isEmpty) View.GONE else View.VISIBLE
                emptyStateContainer.emptyStateContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showErrorState(isError: Boolean, section: Enum<Section>) {
        if (Section.UPCOMING == section) {
            binding.listEventsCardHorizontal.apply {
                errorStateContainer.errorStateContainer.visibility = if (isError) View.VISIBLE else View.GONE
                if (isError) {
                    rvEventsCard.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                }
            }
        } else {
            binding.listEventsCardVertical.apply {
                errorStateContainer.errorStateContainer.visibility = if (isError) View.VISIBLE else View.GONE
                if (isError) {
                    rvEventsCard.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean, section: Enum<Section>) {
        if (Section.UPCOMING == section) {
            binding.listEventsCardHorizontal.apply {
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    rvEventsCard.visibility = View.GONE
                    errorStateContainer.errorStateContainer.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                } else {
                    rvEventsCard.visibility = View.VISIBLE
                }
            }
        } else {
            binding.listEventsCardVertical.apply {
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    rvEventsCard.visibility = View.GONE
                    errorStateContainer.errorStateContainer.visibility = View.GONE
                    emptyStateContainer.emptyStateContainer.visibility = View.GONE
                } else {
                    rvEventsCard.visibility = View.VISIBLE
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