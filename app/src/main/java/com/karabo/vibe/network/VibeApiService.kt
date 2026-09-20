package com.karabo.vibe.network

import com.karabo.vibe.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface VibeApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("settings")
    suspend fun getSettings(): SettingsResponse

    @PUT("settings")
    suspend fun updateSettings(@Body request: UpdateSettingsRequest): SettingsResponse

    @GET("venues")
    suspend fun getVenues(
        @Query("mood") mood: String?,
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?
    ): List<VenueResponse>

    @GET("venues/{id}")
    suspend fun getVenueById(@Path("id") id: Int): VenueResponse

    @GET("favorites")
    suspend fun getFavorites(): List<FavoriteResponse>

    @POST("favorites")
    suspend fun addFavorite(@Body request: AddFavoriteRequest): Response<Unit>

    @DELETE("favorites/{id}")
    suspend fun deleteFavorite(@Path("id") id: Int): Response<Unit>
}
