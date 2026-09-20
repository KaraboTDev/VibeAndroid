package com.karabo.vibe.network.dto

data class UpdateSettingsRequest(
    val preferredLanguage: String? = null,
    val defaultArea: String? = null,
    val notificationsEnabled: Boolean? = null
)
