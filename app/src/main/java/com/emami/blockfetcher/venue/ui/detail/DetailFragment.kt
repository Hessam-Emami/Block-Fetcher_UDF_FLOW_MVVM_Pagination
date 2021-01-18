package com.emami.blockfetcher.venue.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.emami.blockfetcher.common.base.BaseFragment
import com.emami.blockfetcher.common.base.BaseViewModel
import com.emami.blockfetcher.databinding.DetailFragmentBinding
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
    }

    override fun renderState(state: DetailViewModel.DetailViewState) {
        Snackbar.make(requireView(),
            "STATE CHANGED $state",
            Snackbar.LENGTH_LONG).show()
        binding.title.text = state.venue?.toString()
    }

    override fun renderEffect(effect: DetailViewModel.DetailViewEffect) {
        if (effect is DetailViewModel.DetailViewEffect.Error) Snackbar.make(requireView(),
            effect.string,
            Snackbar.LENGTH_SHORT).show()
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