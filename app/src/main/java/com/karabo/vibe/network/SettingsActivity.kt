package com.karabo.vibe.network

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.karabo.vibe.network.auth.TokenManager
import com.karabo.vibe.network.dto.UpdateSettingsRequest
import com.karabotshivhase.vibe.R
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private var currentNotificationsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tokenManager = TokenManager(this)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNav, R.id.navigation_settings)

        loadSettings()

        val cardNotifications = findViewById<MaterialCardView>(R.id.cardNotifications)
        cardNotifications.setOnClickListener {
            toggleNotifications()
        }

        // Tapping other cards simulates setting changes
        findViewById<MaterialCardView>(R.id.cardLanguage).setOnClickListener {
            Toast.makeText(this, "Language selection is handled by system locale", Toast.LENGTH_SHORT).show()
        }
        findViewById<MaterialCardView>(R.id.cardDefaultArea).setOnClickListener {
            Toast.makeText(this, "Default search area is locked to Pretoria", Toast.LENGTH_SHORT).show()
        }
        findViewById<MaterialCardView>(R.id.cardAccount).setOnClickListener {
            Toast.makeText(this, "Account profiling managed securely", Toast.LENGTH_SHORT).show()
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            performLogout()
        }
    }

    private fun loadSettings() {
        val progressBar = findViewById<ProgressBar>(R.id.settingsProgressBar)
        val tvLanguage = findViewById<TextView>(R.id.tvSettingLanguage)
        val tvArea = findViewById<TextView>(R.id.tvSettingArea)
        val tvNotifications = findViewById<TextView>(R.id.tvSettingNotifications)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val settings = RetrofitClient.api.getSettings()
                progressBar.visibility = View.GONE

                tvLanguage.text = settings.preferredLanguage
                tvArea.text = settings.defaultArea
                currentNotificationsEnabled = settings.notificationsEnabled
                tvNotifications.text = if (currentNotificationsEnabled) "Enabled" else "Disabled"
                tvNotifications.setTextColor(
                    if (currentNotificationsEnabled) getColor(R.color.vibe_orange) else getColor(R.color.vibe_text_muted)
                )
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@SettingsActivity, "Failed to load settings: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun toggleNotifications() {
        val nextState = !currentNotificationsEnabled
        val tvNotifications = findViewById<TextView>(R.id.tvSettingNotifications)
        val progressBar = findViewById<ProgressBar>(R.id.settingsProgressBar)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val request = UpdateSettingsRequest(notificationsEnabled = nextState)
                val updated = RetrofitClient.api.updateSettings(request)
                progressBar.visibility = View.GONE

                currentNotificationsEnabled = updated.notificationsEnabled
                tvNotifications.text = if (currentNotificationsEnabled) "Enabled" else "Disabled"
                tvNotifications.setTextColor(
                    if (currentNotificationsEnabled) getColor(R.color.vibe_orange) else getColor(R.color.vibe_text_muted)
                )
                Toast.makeText(this@SettingsActivity, "Settings updated", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@SettingsActivity, "Failed to update settings: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun performLogout() {
        tokenManager.clearToken()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
