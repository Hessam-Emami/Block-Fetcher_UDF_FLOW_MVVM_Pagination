package com.emami.blockfetcher.venue.ui.explore.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.emami.blockfetcher.venue.ui.explore.viewholder.VenueLoadStateViewHolder

// Adapter that displays a loading spinner when
// state = LoadState.Loading, and an error message and retry
// button when state is LoadState.Error.
class VenuePagingLoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<VenueLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ) = VenueLoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: VenueLoadStateViewHolder,
        loadState: LoadState,
    ) = holder.bind(loadState)
}