package com.a17_pre_test.utils.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

abstract class BaseViewModel<State : BaseState, Action : BaseAction, Intent : BaseIntent>(
    initialState: State
) :
    ViewModel() {

    private val _state = MutableLiveData<State>()
    val stateLiveData: LiveData<State>
        get() = _state

    protected var state by Delegates.observable(initialState) { _, old, new ->
        _state.value = new
    }

    abstract fun onIntent(intent: Intent)

    protected fun sendAction(action: Action) {
        state = onReduceState(action)
    }

    protected abstract fun onReduceState(action: Action): State

}