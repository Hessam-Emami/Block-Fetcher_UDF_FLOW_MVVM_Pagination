package com.emami.blockfetcher.explore.data.model

import com.google.gson.annotations.SerializedName

/*
 *  1. We strongly use @SerializedName on every property because as proguard
 * obfuscates these classes and property names, we want Gson to serialize/deserialize properly!
 *
 *  2. Data classes are kept as ExploreResponse's subclass in order to prevent Redeclaration error by the compiler
 *  between Dto, Entity, Domain objects that have the same name.
 *
 *  3. Mapping is auto generated and is not written by hand!
 */

data class ExploreResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("response") val response: Response,
) {


    data class Meta(
        @SerializedName("code") val code: Int,
        @SerializedName("requestId") val requestId: String,
    )

    data class Response(
        @SerializedName("warning") val warning: Warning?,
        @SerializedName("suggestedRadius") val suggestedRadius: Int?,
        @SerializedName("headerLocation") val headerLocation: String?,
        @SerializedName("headerFullLocation") val headerFullLocation: String?,
        @SerializedName("headerLocationGranularity") val headerLocationGranularity: String?,
        @SerializedName("totalResults") val totalResults: Int,
        @SerializedName("suggestedBounds") val suggestedBounds: SuggestedBounds?,
        @SerializedName("groups") val groups: List<Group>,
    )

    data class SuggestedBounds(
        @SerializedName("ne") val ne: Ne,
        @SerializedName("sw") val sw: Sw,
    )

    data class Group(
        @SerializedName("type") val type: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("items") val items: List<Item>,
    )

    data class Item(
        @SerializedName("reasons") val reasons: Reason?,
        @SerializedName("venue") val venue: Venue,
    )


    data class Venue(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("location") val location: Location,
        @SerializedName("categories") val categories: List<Category>,
        @SerializedName("popularityByGeo") val popularityByGeo: Double,
        @SerializedName("venuePage") val venuePage: VenuePage,
    )

    data class Category(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("pluralName") val pluralName: String?,
        @SerializedName("shortName") val shortName: String?,
        @SerializedName("icon") val icon: Icon,
        @SerializedName("primary") val primary: Boolean,
    )

    data class Icon(
        @SerializedName("prefix") val prefix: String,
        @SerializedName("suffix") val suffix: String,
    )


    data class LabeledLatLng(
        @SerializedName("label") val label: String,
        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double,
    )

    data class Location(
        @SerializedName("address") val address: String,
        @SerializedName("crossStreet") val crossStreet: String?,
        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double,
        @SerializedName("labeledLatLngs") val labeledLatLngs: List<LabeledLatLng>?,
        @SerializedName("distance") val distance: Int,
        @SerializedName("postalCode") val postalCode: Int?,
        @SerializedName("cc") val cc: String?,
        @SerializedName("city") val city: String?,
        @SerializedName("state") val state: String?,
        @SerializedName("country") val country: String?,
        @SerializedName("formattedAddress") val formattedAddress: List<String>?,
    )


    data class Ne(

        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double,
    )

    data class Reason(

        @SerializedName("count") val count: Int,
        @SerializedName("items") val items: List<Item>,
    )


    data class Sw(

        @SerializedName("lat") val lat: Double,
        @SerializedName("lng") val lng: Double,
    )


    data class VenuePage(
        @SerializedName("id") val id: Int,
    )

    data class Warning(

        @SerializedName("text") val text: String,
    )
}
