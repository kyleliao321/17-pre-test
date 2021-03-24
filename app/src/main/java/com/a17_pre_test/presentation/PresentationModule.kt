package com.a17_pre_test.presentation

import com.a17_pre_test.presentation.views.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel { SearchViewModel(get()) }
}