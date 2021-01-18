package com.emami.blockfetcher.venue.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.emami.blockfetcher.venue.data.local.db.VenueRoomDatabase
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
import com.emami.blockfetcher.venue.data.model.RemoteKeysEntity
import com.emami.blockfetcher.venue.data.model.VenueEntity
import java.time.Instant
import javax.inject.Inject


class VenueLocalDataSource @Inject constructor(
    private val db: VenueRoomDatabase,
    private val sharedPref: SharedPreferences,
) {
    private val venueDao = db.venueDao()
    private val remoteKeysDao = db.remoteKeysDao()

    private suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeysEntity>) {
        remoteKeysDao.insertAll(remoteKeys)
    }

    private suspend fun insertVenues(venues: List<VenueEntity>) {
        venueDao.insertAll(venues)
    }

    fun getLastKnownLocation(): LatitudeLongitude? {
        val lat = sharedPref.getString(PREF_LAT_KEY, null)
        val lng = sharedPref.getString(PREF_LNG_KEY, null)
        return if (lat != null && lng != null) LatitudeLongitude(lat.toDouble(), lng.toDouble())
        else null
    }

    fun saveLastKnownLocation(location: LatitudeLongitude) {
        sharedPref.edit {
            putString(PREF_LAT_KEY, location.lat.toString())
            putString(PREF_LNG_KEY, location.lng.toString())
        }
    }

    suspend fun getRemoteKeyById(venueId: String): RemoteKeysEntity? {
        return db.withTransaction { remoteKeysDao.getRemoteKeyById(venueId) }
    }

    fun getAllVenuesPaged(): PagingSource<Int, VenueEntity> {
        return venueDao.getAllVenuesPaged()
    }

    private suspend fun getAllVenues(): List<VenueEntity> {
        return db.withTransaction { venueDao.getAllVenues() }
    }

    suspend fun getOldestVenueRecordCreationTime(): Instant? {
        return db.withTransaction { venueDao.getOldestRecord() }
    }

    suspend fun getCachedVenueCount(): Int = getAllVenues().size

    suspend fun invalidateData() {
        db.withTransaction {
            venueDao.clearTable()
            remoteKeysDao.clearTable()
        }
    }

    suspend fun insertVenueAndCorrespondingKeys(
        list: List<VenueEntity>,
        keys: List<RemoteKeysEntity>,
    ) {
        db.withTransaction {
            insertVenues(list)
            insertRemoteKeys(keys)
        }
    }

    private companion object {
        const val PREF_LAT_KEY = "pref-lat-key"
        const val PREF_LNG_KEY = "pref-lng-key"
    }
}

