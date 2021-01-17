package com.emami.blockfetcher.venue.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant


@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    val venueId: String,
    val nextKey: Int?,
)

@Entity(tableName = "venue")
data class VenueEntity(
    @PrimaryKey @ColumnInfo(name = "venue_id")
    val id: String,
    val name: String,
    @Embedded
    val primaryCategory: CategoryEntity,
    @Embedded
    val location: LocationEntity,
    val created_at: Instant = Instant.now(),
)

data class CategoryEntity(
    @ColumnInfo(name = "category_id")
    val id: String,
    @ColumnInfo(name = "category_name")
    val name: String,
    val iconPrefix: String,
    val iconPostFix: String,
)

data class LocationEntity(
    val lat: Double,
    val lng: Double,
    val address: String,
    val distance: Int,
)


