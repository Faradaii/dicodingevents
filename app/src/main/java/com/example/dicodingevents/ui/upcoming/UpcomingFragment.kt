package com.example.dicodingevents.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.databinding.FragmentUpcomingBinding
import com.example.dicodingevents.ui.adapter.EventItemAdapter

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val upcomingViewModel =
            ViewModelProvider(this)[UpcomingViewModel::class.java]

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->

                    //handle search with empty string
                    if (textView.text.toString().isEmpty()) {
                    searchBar.setText("")
                    searchView.hide()
                    upcomingViewModel.getUpcoming()
                    return@setOnEditorActionListener true
                    }

                    // handle back button
                    searchView.editText.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            searchBar.setText("")
                            upcomingViewModel.getUpcoming()
                        }
                    }

                    searchBar.setText(searchView.text)
                    upcomingViewModel.getUpcomingWithQuery(searchView.text.toString())

                    false
                }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        upcomingViewModel.isError.observe(viewLifecycleOwner) {
            showErrorState(it)
        }

        upcomingViewModel.isEmpty.observe(viewLifecycleOwner) {
            showEmptyState(it)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.includeListEvents.rvEvents.layoutManager = layoutManager

        upcomingViewModel.listUpcoming.observe(viewLifecycleOwner) { dicodingevent ->
            setEventsData(dicodingevent)
        }
        return root
    }

    private fun setEventsData(dicodingEvents: List<DicodingEvent>){
        val adapter = EventItemAdapter()
        adapter.submitList(dicodingEvents)
        binding.includeListEvents.rvEvents.adapter = adapter
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.includeListEvents.apply {
            rvEvents.visibility = if (isEmpty) View.GONE else View.VISIBLE
            emptyStateContainer.emptyStateContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorState(isError: Boolean) {
        binding.includeListEvents.apply {
            errorStateContainer.errorStateContainer.visibility = if (isError) View.VISIBLE else View.GONE
            if (isError) {
                emptyStateContainer.emptyStateContainer.visibility = View.GONE
                rvEvents.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.includeListEvents.apply {
            loadingIndicatorEvents.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                rvEvents.visibility = View.GONE
                emptyStateContainer.emptyStateContainer.visibility = View.GONE
                errorStateContainer.errorStateContainer.visibility = View.GONE
            } else {
                rvEvents.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}