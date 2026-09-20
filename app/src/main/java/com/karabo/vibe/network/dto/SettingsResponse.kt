package com.karabo.vibe.network.dto

data class SettingsResponse(
    val preferredLanguage: String,
    val defaultArea: String,
    val notificationsEnabled: Boolean
)
