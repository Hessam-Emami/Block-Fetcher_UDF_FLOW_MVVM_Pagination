package com.emami.blockfetcher.venue.ui.explore

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.emami.blockfetcher.common.exception.UnknownLastLocationException
import com.emami.blockfetcher.venue.data.ExploreRepository
import com.emami.blockfetcher.venue.data.LocationManager
import com.emami.blockfetcher.venue.data.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class ExploreViewModel @ViewModelInject constructor(
    private val locationManager: LocationManager,
    private val repository: ExploreRepository,
) :
    ViewModel() {

    private val _state = MutableStateFlow(ExploreViewState())
    val state: StateFlow<ExploreViewState>
        get() = _state

    //SharedFlow with 0 replay acts like SingleLiveEvent. Useful for emitting ViewEffects (one-shot state calls)
    private val _effect = MutableSharedFlow<ExploreViewEffect>(replay = 0)
    val effect: SharedFlow<ExploreViewEffect>
        get() = _effect

    @ExperimentalPagingApi
    fun startVenueDiscovery() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.emit(_state.value.copy(isLoading = true))
                val lastLocation = locationManager.awaitLastLocation()
                repository.fetchVenues(lastLocation).cachedIn(viewModelScope)
                    .catch { cause ->
                        Timber.e(cause)
                        _effect.emit(ExploreViewEffect.Error(cause.message ?: "Unknown Error"))
                    }
                    .collect {
                        _state.emit(_state.value.copy(list = it, isLoading = false))
                    }
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

