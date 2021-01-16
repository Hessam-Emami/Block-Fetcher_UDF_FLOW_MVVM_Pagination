package com.emami.blockfetcher.explore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    val venueId: String,
    val nextKey: Int?,
)

@Entity(tableName = "venue")
data class VenueEntity(
    @PrimaryKey
    val id: String,
    val name: String,
)

