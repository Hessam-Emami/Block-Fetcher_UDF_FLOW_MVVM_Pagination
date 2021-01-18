package com.emami.blockfetcher.venue.ui.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.emami.blockfetcher.common.base.BaseFragment
import com.emami.blockfetcher.common.base.BaseViewModel
import com.emami.blockfetcher.common.extensions.loadFromPath
import com.emami.blockfetcher.databinding.DetailFragmentBinding
import com.emami.blockfetcher.venue.data.model.VenueDetail
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment :
    BaseFragment<DetailViewModel.DetailViewState, DetailViewModel.DetailViewEffect>() {

    private val viewModel: DetailViewModel by viewModels()

    override val _viewModel: BaseViewModel<DetailViewModel.DetailViewState, DetailViewModel.DetailViewEffect>
        get() = viewModel


    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val venueId = DetailFragmentArgs.fromBundle(requireArguments()).venueId
        viewModel.getVenueDetails(venueId)
        binding.retryButton.setOnClickListener { viewModel.getVenueDetails(venueId) }
    }

    override fun renderState(state: DetailViewModel.DetailViewState) {
        binding.dataContainer.visibility = if (state.needsRetry) View.INVISIBLE else View.VISIBLE
        binding.errorContainer.visibility = if (state.needsRetry) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        if (state.venue != null) populateVenue(state.venue)
    }

    private fun populateVenue(venue: VenueDetail) {
        with(binding) {
            name.text = venue.name
            description.text = venue.description
            description.visibility =
                if (venue.description.isEmpty()) View.GONE else View.VISIBLE
            website.setOnClickListener { viewModel.launchOpenWebEffect(venue.url) }
            phone.setOnClickListener { viewModel.launchCallPhoneEffect(venue.phoneNumber) }
            map.setOnClickListener { viewModel.launchLocationEffect(venue.location.coordinate) }
            categoryTag.text = venue.primaryCategory.name
            categoryIcon.loadFromPath(venue.primaryCategory.getIconPath(100))
            rating.text = venue.rating.rating.toString()
            rating.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#${venue.rating.ratingColor}"))
            venueIcon.loadFromPath(venue.venueMainIcon?.getIconPathByWidthHeight(500, 300))
            address.text = venue.location.address
            likes.text = venue.likesCount?.toString() ?: "0"
        }
    }

    override fun renderEffect(effect: DetailViewModel.DetailViewEffect) {
        when (effect) {
            is DetailViewModel.DetailViewEffect.Error -> Snackbar.make(requireView(),
                effect.string,
                Snackbar.LENGTH_SHORT).show()
            is DetailViewModel.DetailViewEffect.OpenWebsite -> {
                val openBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                startActivity(openBrowserIntent)
            }
            is DetailViewModel.DetailViewEffect.OpenPhoneCall -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${effect.phoneNumber}")
                startActivity(callIntent)
            }
            is DetailViewModel.DetailViewEffect.OpenLocationOnMap -> {
                val gmmIntentUri = Uri.parse("geo:${effect.locationQuery}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(mapIntent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}