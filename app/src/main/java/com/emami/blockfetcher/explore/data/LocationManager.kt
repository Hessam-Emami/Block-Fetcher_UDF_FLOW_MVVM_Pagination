package com.emami.blockfetcher.explore.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.emami.blockfetcher.common.exception.UnknownLastLocationException
import com.emami.blockfetcher.explore.data.model.LatitudeLongitude
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManager @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context,
) {


    /**
     * A one-shot coroutine call that returns last location if permission is granted or throws
     * exception when permission is denied.
     */
    suspend fun awaitLastLocation(): LatitudeLongitude =

        // Create a new coroutine that can be cancelled
        suspendCancellableCoroutine { continuation ->
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) continuation.cancel(UnknownLastLocationException())

            // Add listeners that will resume the execution of this coroutine
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                // Resume coroutine and return location
                continuation.resume(LatitudeLongitude(location.latitude, location.longitude))
            }.addOnFailureListener { e ->
                // Resume the coroutine by throwing an exception
                //Log actual exception to Crashlytics but return custom exception instead!
                Timber.e(e)
                continuation.resumeWithException(UnknownLastLocationException())
            }
        }

}

