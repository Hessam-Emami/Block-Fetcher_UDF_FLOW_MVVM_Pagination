package com.emami.blockfetcher.venue.data.local

import android.location.Location
import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class CacheIntegrityChecker @Inject constructor(private val localDataSource: VenueLocalDataSource) {

    suspend fun isDataValidForGivenQuery(location: LatitudeLongitude): Boolean {
        val a = getDataIntegrityByTime()
        val b = getIntegrityByCacheSize()
        val c = getIntegrityByLastLocationDistance(location)
        Timber.d("Cache Integrity Result Time: $a Item Count: $b Last Location Distance $c")
        return a && b && c
    }

    suspend fun isCurrentVenueDetailValidByTime(id: String): Boolean {
        val creationTime = localDataSource.getVenueDetailCreationTime(id) ?: return true
        return isDurationLessThanAllowedDays(creationTime)

    }


    private suspend fun getDataIntegrityByTime(): Boolean {
        val oldestCachedRecordTime =
            localDataSource.getOldestVenueRecordCreationTime() ?: return true
        return isDurationLessThanAllowedDays(oldestCachedRecordTime)
    }

    private fun isDurationLessThanAllowedDays(oldestCachedRecordTime: Instant) =
        Duration.between(oldestCachedRecordTime, Instant.now())
            .toDays() < MAX_CACHE_ALLOWED_DAYS

    private suspend fun getIntegrityByCacheSize(): Boolean {
        return localDataSource.getCachedVenueCount() >= Constants.DEFAULT_PAGE_SIZE
    }

    private fun getIntegrityByLastLocationDistance(currentLocation: LatitudeLongitude): Boolean {
        val locationA = Location("CurrentLocation");

        locationA.latitude = currentLocation.lat
        locationA.longitude = currentLocation.lng


        val locationB = Location("LastKnownLocation");
        val lastKnownLocation = localDataSource.getLastKnownLocation() ?: return true
        locationB.latitude = lastKnownLocation.lat
        locationB.longitude = lastKnownLocation.lng

        val distance = locationA.distanceTo(locationB)
        return distance < MAX_ALLOWED_DISTANCE_FROM_LAST_LOCATION
    }

    private companion object {
        const val MAX_CACHE_ALLOWED_DAYS = 7
        const val MAX_ALLOWED_DISTANCE_FROM_LAST_LOCATION = 100f
    }
}