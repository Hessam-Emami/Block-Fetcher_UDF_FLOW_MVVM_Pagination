package com.emami.blockfetcher.venue.ui.explore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emami.blockfetcher.common.base.BaseFragment
import com.emami.blockfetcher.common.base.BaseViewModel
import com.emami.blockfetcher.databinding.ExploreFragmentBinding
import com.emami.blockfetcher.venue.ui.explore.adapter.VenuePagingAdapter
import com.emami.blockfetcher.venue.ui.explore.adapter.VenuePagingLoadStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment :
    BaseFragment<ExploreViewModel.ExploreViewState, ExploreViewModel.ExploreViewEffect>() {

    private val viewModel: ExploreViewModel by viewModels()

    override val _viewModel: BaseViewModel<ExploreViewModel.ExploreViewState, ExploreViewModel.ExploreViewEffect>
        get() = viewModel

    //According to the docs, this doesn't need to detach
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    private var _binding: ExploreFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onStart() {
        super.onStart()
        //User might revoke the permission while the app is open, So we check this regularly
        checkAndRequestLocationPermission()
    }

    lateinit var venuePagingAdapter: VenuePagingAdapter

    override fun renderEffect(effect: ExploreViewModel.ExploreViewEffect) {
        when (effect) {
            is ExploreViewModel.ExploreViewEffect.Error -> Snackbar.make(requireView(),
                effect.string,
                Snackbar.LENGTH_SHORT).show()
            is ExploreViewModel.ExploreViewEffect.NavigateToDetails -> openVenueDetailScreen(
                effect.venueId)
        }
    }

    private fun openVenueDetailScreen(venueId: String) {
        findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToDetailFragment(
            venueId))
    }

    override fun renderState(state: ExploreViewModel.ExploreViewState) {
        venuePagingAdapter.submitData(viewLifecycleOwner.lifecycle, state.list)
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
    }

    private fun onLocationPermissionAvailable() {
        viewModel.startVenueDiscovery()
    }

    /*
     * We notify the model for state processing here, However we will only start/stop collecting  once
     * the view is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            onLocationPermissionAvailable()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionLauncher =
            requireActivity().registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    onLocationPermissionAvailable()
                } else {
                    onLocationPermissionDenied()
                }
            }
    }

    private fun onLocationPermissionDenied() {
        MaterialAlertDialogBuilder(requireContext()).setCancelable(false)
            .setTitle("Location Not Available")
            .setMessage("We can't recommend any places without having you're location. You can always grant permission from the settings menu! ")
            .setNegativeButton(
                "Exit"
            ) { _, _ -> requireActivity().finish() }
            .show()
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /*
     * According to the existing feature, We only need location permission while the user is on this screen
     * So we avoid getting the permission in higher level components(Activity) for now.
     */
    private fun checkAndRequestLocationPermission() {
        val permission =
            Manifest.permission.ACCESS_FINE_LOCATION
        val isGranted: Boolean = isPermissionGranted(permission)
        when {
            !isGranted -> {
                requestPermissionLauncher.launch(permission)
            }
            shouldShowRequestPermissionRationale(permission) -> {
                //Show an explanation before requesting!
                onShowingPermissionRational(permission)
            }
        }
    }


    private fun onShowingPermissionRational(permission: String) {
        MaterialAlertDialogBuilder(requireContext()).setCancelable(false)
            .setTitle("Usage of your location")
            .setMessage("Block Fetcher needs to access you're location in order to find the nearest venues for you")
            .setPositiveButton(
                "Okay"
            ) { _, _ -> requestPermissionLauncher.launch(permission) }
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ExploreFragmentBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        venuePagingAdapter = VenuePagingAdapter(viewModel::onVenueSelected)
        binding.pagingRecyclerView.adapter =
            venuePagingAdapter.withLoadStateFooter(VenuePagingLoadStateAdapter(venuePagingAdapter::retry))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

