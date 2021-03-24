package com.a17_pre_test.presentation.views.search

import android.os.Bundle
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
import com.a17_pre_test.utils.extension.textChanges
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    @ExperimentalCoroutinesApi
    private fun setupSearchName(binding: FragmentSearchBinding) {
        binding.searchName.textChanges()
            .debounce(300)
            .filterNot { it.isNullOrBlank() }
            .onEach { keyword ->
                keyword?.let {
                    viewModel.onIntent(SearchViewModel.Intent.LoadUserFlow(it.toString()))
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
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