package com.karabo.vibe.network

import android.app.Application

class VibeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize RetrofitClient once for the entire application life cycle
        RetrofitClient.init(this)
    }
}
