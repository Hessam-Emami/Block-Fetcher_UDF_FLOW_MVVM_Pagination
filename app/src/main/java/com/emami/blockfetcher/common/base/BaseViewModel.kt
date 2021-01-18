package com.emami.blockfetcher.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel<ViewState : BaseViewModel.BaseViewState, ViewEffect : BaseViewModel.BaseViewEffect>(
    initialViewState: ViewState,
) : ViewModel() {
    protected val _state = MutableStateFlow(initialViewState)
    val state: StateFlow<ViewState>
        get() = _state

    //SharedFlow with 0 replay acts like SingleLiveEvent. Useful for emitting ViewEffects (one-shot state calls)
    protected val _effect = MutableSharedFlow<ViewEffect>(replay = 0)
    val effect: SharedFlow<ViewEffect>
        get() = _effect

    interface BaseViewEffect
    interface BaseViewState
}