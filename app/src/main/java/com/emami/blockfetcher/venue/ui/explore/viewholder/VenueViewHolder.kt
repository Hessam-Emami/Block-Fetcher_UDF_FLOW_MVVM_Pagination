package com.emami.blockfetcher.venue.ui.explore.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.emami.blockfetcher.common.extensions.src
import com.emami.blockfetcher.databinding.ExploreItemVenueBinding
import com.emami.blockfetcher.venue.data.model.Venue
import timber.log.Timber

class VenueViewHolder(private val binding: ExploreItemVenueBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(venue: Venue?, onClick: (venue: Venue) -> Unit) {
        if (venue == null) Timber.w("Despite turning off the placeholders, VenueViewHolder received null venue! ")
        venue?.let {
            binding.root.setOnClickListener { onClick(venue) }
            with(binding) {
                title.text = it.name
                categoryName.text = it.tag
                distance.text = it.labeledDistanceInKilometers
                address.text = it.address
                icon.src(it.iconPath(100))
            }
        }
    }
}