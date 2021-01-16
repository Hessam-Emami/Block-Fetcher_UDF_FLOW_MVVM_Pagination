package com.emami.blockfetcher.explore.data.model

data class Venue(
    val id: String,
    val location: Location,
    val categories: List<Category>,
    val name: String,
) {
    val primaryIcon: String?
        get() = categories.find { it.isPrimary }?.icon
    val labeledDistance: String
        get() = "${location.distance} m"
}

data class Category(val name: String, val icon: String, val isPrimary: Boolean)

data class Location(
    val coordinate: LatitudeLongitude,
    val address: String, val distance: Int,
)

data class LatitudeLongitude(val lat: Double, val lng: Double)
