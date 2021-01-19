package com.emami.blockfetcher.venue.ui.explore

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.emami.blockfetcher.common.base.BaseViewModel
import com.emami.blockfetcher.common.exception.UnknownLastLocationException
import com.emami.blockfetcher.venue.data.LocationManager
import com.emami.blockfetcher.venue.data.VenueRepository
import com.emami.blockfetcher.venue.data.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class ExploreViewModel @ViewModelInject constructor(
    private val locationManager: LocationManager,
    private val repository: VenueRepository,
) :
    BaseViewModel<ExploreViewModel.ExploreViewState, ExploreViewModel.ExploreViewEffect>(
        ExploreViewState()) {

    fun startVenueDiscovery() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.emit(_state.value.copy(isLoading = true))
                //Await for the current location
                val lastLocation = locationManager.awaitLastLocation()
                //Notify model with currentLocation and wait for processing
                repository.fetchVenues(lastLocation).cachedIn(viewModelScope)
                    .onStart { _state.emit(_state.value.copy(isLoading = true)) }
                    .catch { cause ->
                        Timber.e(cause)
                    }
                    .collect {
                        _state.emit(_state.value.copy(list = it, isLoading = false))
                    }
            }
            //This is for catching only LocationManager's awaitLastLocation which might throw exception
            catch (e: UnknownLastLocationException) {
                _state.emit(_state.value.copy(isLoading = false))
                _effect.emit(ExploreViewEffect.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun onVenueSelected(venue: Venue) {
        viewModelScope.launch {
            _effect.emit(ExploreViewEffect.NavigateToDetails(venue.id))
        }
    }

    data class ExploreViewState(
        val list: PagingData<Venue> = PagingData.empty(),
        val isLoading: Boolean = false,
    ) : BaseViewState

    sealed class ExploreViewEffect() : BaseViewEffect {
        data class Error(val string: String) : ExploreViewEffect()
        data class NavigateToDetails(val venueId: String) : ExploreViewEffect()
    }

}

