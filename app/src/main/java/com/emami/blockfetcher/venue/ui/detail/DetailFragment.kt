package com.emami.blockfetcher.venue.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.emami.blockfetcher.databinding.DetailFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
@ExperimentalPagingApi
class DetailFragment : Fragment() {


    private val viewModel: DetailViewModel by viewModels()


    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val venueId = DetailFragmentArgs.fromBundle(requireArguments()).venueId
        viewModel.getVenueDetails(venueId)
        viewModel.effect.onEach { renderEffect(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.state.onEach { renderState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun renderState(state: DetailViewModel.DetailViewState) {
        Snackbar.make(requireView(),
            "STATE CHANGED $state",
            Snackbar.LENGTH_LONG).show()
        binding.title.text = state.venue?.toString()
    }

    private fun renderEffect(effect: DetailViewModel.DetailViewEffect) {
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