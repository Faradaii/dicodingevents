package com.example.dicodingevents.ui.upcoming

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
import com.example.dicodingevents.databinding.FragmentUpcomingBinding
import com.example.dicodingevents.ui.ViewModelFactory
import com.example.dicodingevents.ui.adapter.EventCardItemAdapter

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels {
            factory
        }

        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showErrorState(isError = false)
                        showEmptyState(isEmpty = false)
                        showLoading(isLoading = true)
                    }
                    is Result.Success -> {
                        val dicodingEvents = result.data
                        showLoading(isLoading = false)
                        showErrorState(isError = false)
                        if (dicodingEvents.isEmpty()) {
                            showEmptyState(isEmpty = true)
                        } else {
                            setEventsData(dicodingEvents)
                        }
                    }
                    is Result.Error -> {
                        showLoading(isLoading = false)
                        showEmptyState(isEmpty = false)
                        showErrorState(isError = true)
                        Toast.makeText(
                            context,
                            "Please check your connection!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.includeListEvents.rvEventsCard.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setEventsData(dicodingEvents: List<DicodingEventEntity>){
        val adapter = EventCardItemAdapter()
        adapter.submitList(dicodingEvents)
        binding.includeListEvents.rvEventsCard.adapter = adapter

    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.includeListEvents.apply {
            rvEventsCard.visibility = if (isEmpty) View.GONE else View.VISIBLE
            emptyStateContainer.emptyStateContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorState(isError: Boolean) {
        binding.includeListEvents.apply {
            errorStateContainer.errorStateContainer.visibility = if (isError) View.VISIBLE else View.GONE
            if (isError) {
                emptyStateContainer.emptyStateContainer.visibility = View.GONE
                rvEventsCard.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.includeListEvents.apply {
            progressBar.loadingStateContainer.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                rvEventsCard.visibility = View.GONE
                emptyStateContainer.emptyStateContainer.visibility = View.GONE
                errorStateContainer.errorStateContainer.visibility = View.GONE
            } else {
                rvEventsCard.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}