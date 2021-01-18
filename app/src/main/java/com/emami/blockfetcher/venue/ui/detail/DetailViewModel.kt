package com.emami.blockfetcher.venue.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.emami.blockfetcher.common.base.BaseViewModel
import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.venue.data.VenueRepository
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
                .onStart { _state.emit(_state.value.copy(isLoading = true)) }
                .catch {
                    Timber.e(it)
                    _effect.emit(DetailViewEffect.Error(it.message ?: "Unkiwn"))
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> _state.emit(_state.value.copy(venue = result.body,
                            isLoading = false))
                        is Result.Error -> {
                            _state.emit(_state.value.copy(
                                isLoading = false))
                            _effect.emit(DetailViewEffect.Error(result.errorMessage))
                        }
                    }
                }
        }
    }


    data class DetailViewState(
        val venue: VenueDetail? = null,
        val isLoading: Boolean = false,
    ) : BaseViewState

    sealed class DetailViewEffect() : BaseViewEffect {
        data class Error(val string: String) : DetailViewEffect()
    }
}