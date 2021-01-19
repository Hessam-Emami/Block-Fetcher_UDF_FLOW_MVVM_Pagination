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
        primaryCategory = Category(
            primaryCategory.name,
            Icon(primaryCategory.iconPrefix, primaryCategory.iconPostFix, null, null),
        ),
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
    rating = RatingEntity(if (this.rating != null) this.rating.toString() else "?",
        this.ratingColor ?: "FFC800",
        this.ratingSignals ?: 0),
    likesCount = this.likes.count,
    primaryCategory = CategoryEntity(primaryCategory.id,
        primaryCategory.name,
        primaryCategory.icon.prefix,
        primaryCategory.icon.suffix),
    location = LocationEntity(location.lat,
        location.lng,
        location.address ?: "No Address Associated.",
        location.distance),
    status = OpenStatusEntity(this.hours?.status, this.hours?.isOpen),
    numberOfVenueDetailUpdates = this.pageUpdates?.count,
    phoneNumber = this.contact?.phone
)

fun VenueDetailEntity.toVenueDetail() = VenueDetail(
    id = this.id,
    name = this.name,
    description = description,
    url = url,
    likesCount = likesCount,
    venueMainIcon = Icon(this.venueMainIcon?.prefix,
        this.venueMainIcon?.suffix,
        this.venueMainIcon?.height,
        this.venueMainIcon?.width),
    rating = Rating(rating.rating, rating.ratingColor, rating.ratingCount),
    primaryCategory = Category(this.primaryCategory.name,
        Icon(primaryCategory.iconPrefix, primaryCategory.iconPostFix, null, null)),
    location = Location(
        LatitudeLongitude(location.lat, location.lng), location.address, location.distance),
    status = status?.status,
    phoneNumber = this.phoneNumber
)