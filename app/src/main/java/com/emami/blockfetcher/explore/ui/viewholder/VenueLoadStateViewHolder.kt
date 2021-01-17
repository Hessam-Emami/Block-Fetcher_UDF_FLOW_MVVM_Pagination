package com.emami.blockfetcher.explore.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.emami.blockfetcher.R
import com.emami.blockfetcher.databinding.ExploreItemAdapterLoadStateBinding

class VenueLoadStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.explore_item_adapter_load_state, parent, false)
) {
    private val binding = ExploreItemAdapterLoadStateBinding.bind(itemView)
    private val progressBar: ProgressBar = binding.progressBar
    private val errorMsg: TextView = binding.errorMsg
    private val retry: Button = binding.retryButton
        .also {
            it.setOnClickListener { retry() }
        }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = loadState is LoadState.Error
    }
}