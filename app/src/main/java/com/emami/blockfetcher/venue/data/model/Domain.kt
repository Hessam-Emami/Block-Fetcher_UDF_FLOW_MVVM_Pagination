package com.emami.blockfetcher.venue.data.model

import androidx.annotation.Px

data class Venue(
    val id: String,
    private val location: Location,
    private val primaryCategory: Category,
    val name: String,
) {
    val labeledDistanceInKilometers: String
        get() = buildString {
            val distanceInKilo = location.distance.toDouble() / 1000
            val formattedDistance = String.format("%.2f", distanceInKilo)
            append(formattedDistance)
            append("km")
        }

    val address: String
        get() = location.address
    val tag: String
        get() = primaryCategory.name

    fun iconPath(@Px size: Int) = primaryCategory.getIconPath(size)
}

data class Icon(
    private val iconPrefix: String?,
    private val iconPostFix: String?,
    val height: Int?,
    val width: Int?,
) {
    fun getIconPathBySize(@Px widthHeight: Int) = buildString {
        append(iconPrefix)
        append(widthHeight)
        append(iconPostFix)
    }

    fun getIconPathByWidthHeight(@Px width: Int, @Px height: Int) = buildString {
        append(iconPrefix)
        append(width)
        append("x")
        append(height)
        append(iconPostFix)
    }
}

data class Category(
    val name: String,
    private val icon: Icon,
) {
    fun getIconPath(@Px size: Int): String = icon.getIconPathBySize(size)
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


data class VenueDetail constructor(
    val id: String,
    val name: String,
    val description: String,
    val url: String?,
    val likesCount: Int?,
    val venueMainIcon: Icon?,
    val phoneNumber: String?,
    val rating: Rating,
    val primaryCategory: Category,
    val location: Location,
    val status: String?,
)

data class Rating(val rating: String, val ratingColor: String, val ratingCount: Int)
