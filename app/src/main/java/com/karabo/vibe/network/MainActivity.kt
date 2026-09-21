package com.karabo.vibe.network

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.karabo.vibe.network.dto.RegisterRequest
import com.karabotshivhase.vibe.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize networking
        RetrofitClient.init(this)

        // Temporary test call
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.register(
                    RegisterRequest(email = "androidtest@vibe.com", password = "TestPassword123!")
                )
                Log.d("VibeTest", "Success: ${response.token}")
            } catch (e: Exception) {
                Log.e("VibeTest", "Failed: ${e.message}")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}