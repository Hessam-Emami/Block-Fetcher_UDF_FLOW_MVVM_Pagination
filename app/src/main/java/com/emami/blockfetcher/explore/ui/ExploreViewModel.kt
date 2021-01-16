package com.emami.blockfetcher.explore.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.emami.blockfetcher.common.exception.UnknownLastLocationException
import com.emami.blockfetcher.explore.data.ExploreRepository
import com.emami.blockfetcher.explore.data.LocationManager
import com.emami.blockfetcher.explore.data.model.Venue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExploreViewModel @ViewModelInject constructor(
    private val locationManager: LocationManager,
    private val repository: ExploreRepository,
) :
    ViewModel() {

    private val _state = MutableStateFlow(ExploreViewState())
    val state: StateFlow<ExploreViewState>
        get() = _state

    private val _effect = MutableSharedFlow<ExploreViewEffect>(replay = 0)
    val effect: SharedFlow<ExploreViewEffect>
        get() = _effect

    fun init() {
        viewModelScope.launch {
            try {
                _state.emit(_state.value.copy(isLoading = true))
                val lastLocation = locationManager.awaitLastLocation()
                repository.loadData(lastLocation)
            } catch (e: UnknownLastLocationException) {
                _state.emit(_state.value.copy(isLoading = false))
                _effect.emit(ExploreViewEffect.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    data class ExploreViewState(
        val list: PagingData<Venue> = PagingData.empty(),
        val isLoading: Boolean = false,
    )

    sealed class ExploreViewEffect() {
        data class Error(val string: String) : ExploreViewEffect()
    }

}

