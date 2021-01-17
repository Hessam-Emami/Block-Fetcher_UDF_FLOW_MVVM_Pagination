package com.emami.blockfetcher.venue.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emami.blockfetcher.venue.data.model.VenueEntity

@Dao
interface VenueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(venues: List<VenueEntity>)

    @Query("SELECT * FROM venue")
    fun getAllVenuesPaged(): PagingSource<Int, VenueEntity>

    @Query("DELETE FROM venue")
    suspend fun clearTable()

    @Query("SELECT * FROM venue")
    fun getAllVenues(): List<VenueEntity>
}