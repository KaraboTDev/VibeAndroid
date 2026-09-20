package com.karabo.vibe.network.dto

data class FavoriteResponse(
    val id: Int,
    val name: String,
    val address: String?,
    val savedAt: String,
    val syncStatus: String
)
