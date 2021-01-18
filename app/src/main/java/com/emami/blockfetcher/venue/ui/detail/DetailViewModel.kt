package com.emami.blockfetcher.venue.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.emami.blockfetcher.common.base.BaseViewModel
import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.venue.data.VenueRepository
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
import com.emami.blockfetcher.venue.data.model.VenueDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber


class DetailViewModel @ViewModelInject constructor(private val repository: VenueRepository) :
    BaseViewModel<DetailViewModel.DetailViewState, DetailViewModel.DetailViewEffect>(DetailViewState()) {

    fun getVenueDetails(venueId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getVenueDetailById(venueId)
                .onStart { _state.emit(_state.value.copy(isLoading = true, needsRetry = false)) }
                .catch {
                    Timber.e(it)
                    _effect.emit(DetailViewEffect.Error(it.message ?: "Unknown Error"))
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> _state.emit(_state.value.copy(venue = result.body,
                            isLoading = false, needsRetry = false))
                        is Result.Error -> {
                            _state.emit(_state.value.copy(
                                isLoading = false, needsRetry = true))
                            _effect.emit(DetailViewEffect.Error(result.errorMessage))
                        }
                    }
                }
        }
    }

    fun launchOpenWebEffect(url: String?) = viewModelScope.launch {
        if (url.isNullOrEmpty()) _effect.emit(DetailViewEffect.Error("No website is associated by owner"))
        else _effect.emit(DetailViewEffect.OpenWebsite(url))
    }

    fun launchCallPhoneEffect(phoneNumber: String?) = viewModelScope.launch {
        if (phoneNumber.isNullOrEmpty()) _effect.emit(DetailViewEffect.Error("No phone number is associated by owner"))
        else _effect.emit(DetailViewEffect.OpenPhoneCall(phoneNumber))
    }

    fun launchLocationEffect(coordinate: LatitudeLongitude) = viewModelScope.launch {
        _effect.emit(DetailViewEffect.OpenLocationOnMap("${coordinate.lat},${coordinate.lng}"))
    }


    data class DetailViewState(
        val venue: VenueDetail? = null,
        val isLoading: Boolean = false,
        val needsRetry: Boolean = false,
    ) : BaseViewState

    sealed class DetailViewEffect() : BaseViewEffect {
        data class Error(val string: String) : DetailViewEffect()
        data class OpenWebsite(val url: String) : DetailViewEffect()
        data class OpenPhoneCall(val phoneNumber: String) : DetailViewEffect()
        data class OpenLocationOnMap(val locationQuery: String) : DetailViewEffect()
    }
}