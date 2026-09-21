package com.karabo.vibe.network

import android.content.Context
import com.karabo.vibe.network.auth.AuthInterceptor
import com.karabo.vibe.network.auth.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:49899/"
    private lateinit var tokenManager: TokenManager

    /**
     * Call this in your Application class or MainActivity before accessing the api.
     */
    fun init(context: Context) {
        tokenManager = TokenManager(context.applicationContext)
    }

    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val api: VibeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VibeApiService::class.java)
    }
}
