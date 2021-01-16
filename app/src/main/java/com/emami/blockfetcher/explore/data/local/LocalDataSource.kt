package com.emami.blockfetcher.explore.data.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.emami.blockfetcher.explore.data.model.RemoteKeysEntity
import com.emami.blockfetcher.explore.data.model.VenueEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val db: VenueRoomDatabase) {
    private val venueDao = db.venueDao()
    private val remoteKeysDao = db.remoteKeysDao()

    private suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeysEntity>) {
        remoteKeysDao.insertAll(remoteKeys)
    }

    private suspend fun insertVenues(venues: List<VenueEntity>) {
        venueDao.insertAll(venues)
    }

    suspend fun getRemoteKeyById(venueId: String): RemoteKeysEntity? {
        return db.withTransaction { remoteKeysDao.getRemoteKeyById(venueId) }
    }

    fun getAllVenues(): PagingSource<Int, VenueEntity> {
        return venueDao.getAllVenues()
    }

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
}

