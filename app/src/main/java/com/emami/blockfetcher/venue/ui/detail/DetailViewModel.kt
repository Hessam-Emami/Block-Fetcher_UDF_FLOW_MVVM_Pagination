package com.emami.blockfetcher.venue.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.venue.data.VenueRepository
import com.emami.blockfetcher.venue.data.model.VenueDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalPagingApi
class DetailViewModel @ViewModelInject constructor(private val repository: VenueRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(DetailViewState())
    val state: StateFlow<DetailViewState>
        get() = _state

    //SharedFlow with 0 replay acts like SingleLiveEvent. Useful for emitting ViewEffects (one-shot state calls)
    private val _effect = MutableSharedFlow<DetailViewEffect>(replay = 0)
    val effect: SharedFlow<DetailViewEffect>
        get() = _effect


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
    )

    sealed class DetailViewEffect() {
        data class Error(val string: String) : DetailViewEffect()
    }
}