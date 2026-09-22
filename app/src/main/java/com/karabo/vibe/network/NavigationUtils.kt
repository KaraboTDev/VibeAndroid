package com.karabo.vibe.network

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karabotshivhase.vibe.R

object NavigationUtils {
    fun setupBottomNavigation(activity: Activity, bottomNav: BottomNavigationView, currentItemId: Int) {
        bottomNav.selectedItemId = currentItemId
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == currentItemId) return@setOnItemSelectedListener true

            when (item.itemId) {
                R.id.navigation_home -> {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                    activity.finish()
                    true
                }
                R.id.navigation_favorites -> {
                    activity.startActivity(Intent(activity, FavoritesActivity::class.java))
                    activity.finish()
                    true
                }
                R.id.navigation_settings -> {
                    activity.startActivity(Intent(activity, SettingsActivity::class.java))
                    activity.finish()
                    true
                }
                else -> false
            }
        }
    }
}
