package com.emami.blockfetcher.explore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emami.blockfetcher.explore.data.model.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE venueId = :venueId")
    suspend fun getRemoteKeyById(venueId: String): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearTable()

}