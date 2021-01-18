package com.emami.blockfetcher.venue.ui.explore.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.emami.blockfetcher.databinding.ExploreItemVenueBinding
import com.emami.blockfetcher.venue.data.model.Venue
import timber.log.Timber

class VenueViewHolder(private val binding: ExploreItemVenueBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(venue: Venue?) {
        if (venue == null) Timber.w("We're having NULL venue despite turning off the placeholders!")
        venue?.let {
            binding.title.text = venue.name
        }
    }
}