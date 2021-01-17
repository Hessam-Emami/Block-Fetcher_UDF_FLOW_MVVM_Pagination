package com.emami.blockfetcher.venue.data.model

import androidx.annotation.Px

data class Venue(
    val id: String,
    val location: Location,
    val primaryCategory: Category,
    val name: String,
) {
    val labeledDistance: String
        get() = "${location.distance} m"
}

data class Category(
    val name: String,
    private val iconPrefix: String,
    private val iconPostFix: String,
) {
    fun icon(@Px widthHeight: Int) = buildString {
        append(iconPostFix)
        append(widthHeight)
        append(iconPostFix)
    }
}

data class Location(
    val coordinate: LatitudeLongitude,
    val address: String, val distance: Int,
)

data class LatitudeLongitude(val lat: Double, val lng: Double) {
    val coordinate: String
        get() = buildString {
            append(lat)
            append(',')
            append(lng)
        }
}
