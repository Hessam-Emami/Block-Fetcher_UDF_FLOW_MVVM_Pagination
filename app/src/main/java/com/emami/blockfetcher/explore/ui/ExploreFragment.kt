package com.emami.blockfetcher.explore.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emami.blockfetcher.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExploreFragment : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        //User might revoke the permission while the app is open, So we check this regularly
        checkAndRequestLocationPermission()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            onLocationPermissionAvailable()
        }
    }

    private fun onLocationPermissionAvailable() {

        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()

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

    override fun onDetach() {
        super.onDetach()
        requestPermissionLauncher = null
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
                requestPermissionLauncher?.launch(permission)
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
            ) { _, _ -> requestPermissionLauncher?.launch(permission) }
            .show()
    }
}
