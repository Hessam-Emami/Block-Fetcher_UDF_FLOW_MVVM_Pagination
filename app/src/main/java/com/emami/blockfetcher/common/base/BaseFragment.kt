package com.emami.blockfetcher.common.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<ViewState : BaseViewModel.BaseViewState, ViewEffect : BaseViewModel.BaseViewEffect> :
    Fragment() {
    protected abstract val _viewModel: BaseViewModel<ViewState, ViewEffect>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.effect.onEach { renderEffect(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        _viewModel.state.onEach { renderState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    abstract fun renderState(state: ViewState)
    abstract fun renderEffect(effect: ViewEffect)
}