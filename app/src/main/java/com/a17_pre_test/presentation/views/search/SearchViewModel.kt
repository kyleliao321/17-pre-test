package com.a17_pre_test.presentation.views.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.a17_pre_test.domain.models.GithubUserDomainModel
import com.a17_pre_test.domain.usecases.GetUserByNameFlowUseCase
import com.a17_pre_test.utils.response.Response
import com.a17_pre_test.utils.viewmodel.BaseAction
import com.a17_pre_test.utils.viewmodel.BaseIntent
import com.a17_pre_test.utils.viewmodel.BaseState
import com.a17_pre_test.utils.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getUserByNameFlowUseCase: GetUserByNameFlowUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel<SearchViewModel.State, SearchViewModel.Action, SearchViewModel.Intent>(State()) {

    private fun loadUserFlow(name: String) = viewModelScope.launch(defaultDispatcher) {
        sendAction(Action.LoadUserFlow)

        val params = GetUserByNameFlowUseCase.Params(name)
        val res = getUserByNameFlowUseCase(params)

        if (res is Response.Success) {
            sendAction(Action.UserFlowLoaded(res.data.cachedIn(viewModelScope)))
        } else {
            sendAction(Action.UserFlowLoaded(null))
        }
    }

    override fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadUserFlow -> loadUserFlow(intent.name)
        }
    }

    override fun onReduceState(action: Action): State = when (action) {
        is Action.LoadUserFlow -> state.copy(
            isUserFlowLoading = true
        )

        is Action.UserFlowLoaded -> state.copy(
            isUserFlowLoading = false,
            userFlow = action.flow
        )
    }

    sealed class Intent : BaseIntent {
        class LoadUserFlow(val name: String) : Intent()
    }

    sealed class Action : BaseAction {
        object LoadUserFlow : Action()
        class UserFlowLoaded(val flow: Flow<PagingData<GithubUserDomainModel>>?) : Action()
    }

    data class State(
        val isUserFlowLoading: Boolean = false,
        val userFlow: Flow<PagingData<GithubUserDomainModel>>? = null
    ) : BaseState
}