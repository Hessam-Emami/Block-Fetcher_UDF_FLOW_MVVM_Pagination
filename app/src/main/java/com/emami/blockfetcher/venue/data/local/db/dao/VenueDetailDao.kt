package com.emami.blockfetcher.venue.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emami.blockfetcher.venue.data.model.VenueDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VenueDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venue: VenueDetailEntity)

    @Query("SELECT * FROM venue_detail WHERE :id=venue_id")
    fun getVenueByIdFlow(id: String): Flow<VenueDetailEntity?>

    @Query("SELECT * FROM venue_detail WHERE :id=venue_id")
    fun getVenueById(id: String): VenueDetailEntity?

    @Query("DELETE FROM venue_detail")
    suspend fun clearTable()

}