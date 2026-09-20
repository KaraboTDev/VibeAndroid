package com.karabo.vibe.network.dto

data class AuthResponse(
    val token: String,
    val userId: Int,
    val email: String,
    val expiresAt: String
)
