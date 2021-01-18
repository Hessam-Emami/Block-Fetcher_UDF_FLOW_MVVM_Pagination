package com.emami.blockfetcher.venue.ui.explore.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.emami.blockfetcher.databinding.ExploreItemVenueBinding
import com.emami.blockfetcher.venue.data.model.Venue
import timber.log.Timber

class VenueViewHolder(private val binding: ExploreItemVenueBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(venue: Venue?) {
        if (venue == null) Timber.w("Despite turning off the placeholders, VenueViewHolder received null venue! ")
        venue?.let {
            with(binding) {
                title.text = venue.name
                categoryName.text = venue.tag
                distance.text = venue.labeledDistanceInKilometers
                address.text = venue.address
            }
        }
    }
}