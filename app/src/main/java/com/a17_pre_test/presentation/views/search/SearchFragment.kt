package com.a17_pre_test.presentation.views.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.a17_pre_test.databinding.FragmentSearchBinding
import com.a17_pre_test.presentation.adapter.GithubUserAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by inject()

    private lateinit var adapter: GithubUserAdapter

    private val observer = Observer<SearchViewModel.State> { state ->
        Timber.d(state.toString())

        state.userFlow?.let { flow ->
            viewLifecycleOwner.lifecycleScope.launch {
                flow.collectLatest { data ->
                    adapter.submitData(data)
                }
            }
        }
    }

    private val searchNameWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            adapter.refresh()
            s?.let {
                viewModel.onIntent(SearchViewModel.Intent.LoadUserFlow(it.toString()))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupSearchName(binding)
        setupAdapter(binding)

        viewModel.stateLiveData.observe(viewLifecycleOwner, observer)

        return binding.root
    }

    private fun setupSearchName(binding: FragmentSearchBinding) {
        binding.searchName.addTextChangedListener(searchNameWatcher)
    }


    private fun setupAdapter(binding: FragmentSearchBinding) {
        adapter = GithubUserAdapter()
        binding.searchUserContainer.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { state ->
                val loadRefresh = state.refresh

                binding.errorMessage.isVisible = loadRefresh is LoadState.Error
                binding.searchUserContainer.isVisible = loadRefresh is LoadState.NotLoading
                binding.searchUserContainerShimmer.isVisible = loadRefresh is LoadState.Loading

                if (loadRefresh is LoadState.Error) {
                    binding.errorMessage.text = loadRefresh.error.message
                }
            }
        }
    }
}