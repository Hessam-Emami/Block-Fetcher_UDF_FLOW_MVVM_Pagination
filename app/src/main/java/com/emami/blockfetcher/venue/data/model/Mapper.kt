package com.emami.blockfetcher.venue.data.model


fun Dto.VenueDTO.toVenueEntity() = VenueEntity(id = id,
    name = name,
    primaryCategory = CategoryEntity(primaryCategory.id,
        primaryCategory.name,
        primaryCategory.icon.prefix,
        primaryCategory.icon.suffix),
    location = LocationEntity(location.lat,
        location.lng,
        location.address ?: "No Address Associated.",
        location.distance))

fun VenueEntity.toDomain() =
    Venue(id = id,
        location = Location(LatitudeLongitude(location.lat, location.lng),
            location.address,
            location.distance),
        primaryCategory = Category(primaryCategory.name,
            primaryCategory.iconPrefix,
            primaryCategory.iconPostFix),
        name = name)

fun List<Dto.Group>.toVenueDTOList(): List<Dto.VenueDTO> =
    this.asSequence()
        .flatMap { it.items }
        .map { it.venue }
        .toList()

fun Dto.VenueDetail.toVenueDetailEntity(): VenueDetailEntity = VenueDetailEntity(
    id = this.id,
    name = this.name,
    description = this.description ?: "",
    url = this.url ?: "",
    samplePhrase = this.phrases?.random()?.phrase ?: "",
    venueMainIcon = if (this.bestPhoto != null) IconEntity(this.bestPhoto.prefix,
        this.bestPhoto.suffix,
        this.bestPhoto.width,
        this.bestPhoto.height) else null,
    rating = RatingEntity(this.rating, this.ratingColor, this.ratingSignals),
    primaryCategory = CategoryEntity(primaryCategory.id,
        primaryCategory.name,
        primaryCategory.icon.prefix,
        primaryCategory.icon.suffix),
    location = LocationEntity(location.lat,
        location.lng,
        location.address ?: "No Address Associated.",
        location.distance),
    status = OpenStatusEntity(this.hours?.status, this.hours?.isOpen),
    numberOfVenueDetailUpdates = this.pageUpdates?.count
)

fun VenueDetailEntity.toVenueDetail() = VenueDetail(this.name)