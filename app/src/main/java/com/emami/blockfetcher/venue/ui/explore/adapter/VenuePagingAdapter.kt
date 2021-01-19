package com.emami.blockfetcher.venue.ui.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.emami.blockfetcher.databinding.ExploreItemVenueBinding
import com.emami.blockfetcher.venue.data.model.Venue
import com.emami.blockfetcher.venue.ui.explore.viewholder.VenueViewHolder

class VenuePagingAdapter(private val onClick: (venue: Venue) -> Unit) :
    PagingDataAdapter<Venue, VenueViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VenueViewHolder {
        val binding = ExploreItemVenueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VenueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }

    object DiffCallback : DiffUtil.ItemCallback<Venue>() {
        override fun areItemsTheSame(
            oldItem: Venue,
            newItem: Venue,
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Venue,
            newItem: Venue,
        ): Boolean =
            oldItem.name == newItem.name
    }


}
