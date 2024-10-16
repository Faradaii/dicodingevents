package com.example.dicodingevents.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.databinding.FragmentFinishedBinding
import com.example.dicodingevents.ui.adapter.EventItemAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val finishedViewModel =
            ViewModelProvider(this).get(FinishedViewModel::class.java)

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
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
                        finishedViewModel.getFinished()
                        return@setOnEditorActionListener true
                    }

                    // handle back button
                    searchView.editText.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            searchBar.setText("")
                            finishedViewModel.getFinished()
                        }
                    }

                    searchBar.setText(searchView.text)
                    finishedViewModel.getFinishedWithQuery(searchView.text.toString())

                    false
                }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishedViewModel.isError.observe(viewLifecycleOwner) {
            showErrorState(it)
        }

        finishedViewModel.isEmpty.observe(viewLifecycleOwner) {
            showEmptyState(it)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.includeListEvents.rvEvents.layoutManager = layoutManager

        finishedViewModel.listFinished.observe(viewLifecycleOwner) { dicodingevent ->
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
        if (isEmpty) {
            binding.includeListEvents.rvEvents.visibility = View.GONE
            binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.VISIBLE
        } else {
            binding.includeListEvents.rvEvents.visibility = View.VISIBLE
            binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
        }
    }

    private fun showErrorState(isError: Boolean) {
        if (isError) {
            binding.includeListEvents.errorStateContainer.errorStateContainer.visibility = View.VISIBLE
            binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            binding.includeListEvents.rvEvents.visibility = View.GONE
        } else {
            binding.includeListEvents.errorStateContainer.errorStateContainer.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.includeListEvents.loadingIndicatorEvents.visibility = View.VISIBLE
            binding.includeListEvents.rvEvents.visibility = View.GONE
            binding.includeListEvents.emptyStateContainer.emptyStateContainer.visibility = View.GONE
            binding.includeListEvents.errorStateContainer.errorStateContainer.visibility = View.GONE
        } else {
            binding.includeListEvents.rvEvents.visibility = View.VISIBLE
            binding.includeListEvents.loadingIndicatorEvents.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}