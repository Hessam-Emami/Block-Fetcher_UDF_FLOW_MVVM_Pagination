package com.emami.blockfetcher.venue.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.emami.blockfetcher.venue.data.VenueRepository
import com.emami.blockfetcher.venue.data.model.Venue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

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

    }


    data class DetailViewState(
        val venue: Venue? = null,
        val isLoading: Boolean = false,
    )

    sealed class DetailViewEffect() {
        data class Error(val string: String) : DetailViewEffect()
    }
}