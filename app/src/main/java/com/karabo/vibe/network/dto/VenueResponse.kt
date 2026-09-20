package com.karabo.vibe.network.dto

data class VenueResponse(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val openingHours: String?,
    val tags: List<String>
)
