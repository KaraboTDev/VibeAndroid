package com.karabo.vibe.network

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karabotshivhase.vibe.R
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNav, R.id.navigation_favorites)

        val rvFavorites = findViewById<RecyclerView>(R.id.rvFavorites)
        rvFavorites.layoutManager = LinearLayoutManager(this)

        val progressBar = findViewById<ProgressBar>(R.id.favoritesProgressBar)
        val tvEmptyState = findViewById<TextView>(R.id.tvFavoritesEmptyState)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val favorites = RetrofitClient.api.getFavorites()
                progressBar.visibility = View.GONE

                if (favorites.isEmpty()) {
                    tvEmptyState.visibility = View.VISIBLE
                    rvFavorites.visibility = View.GONE
                } else {
                    tvEmptyState.visibility = View.GONE
                    rvFavorites.visibility = View.VISIBLE
                    rvFavorites.adapter = FavoriteAdapter(favorites) { selectedFav ->
                        val intent = Intent(this@FavoritesActivity, VenueDetailActivity::class.java).apply {
                            putExtra(Constants.EXTRA_VENUE_ID, selectedFav.id)
                        }
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                tvEmptyState.visibility = View.VISIBLE
                tvEmptyState.text = "Error loading favorites"
                Toast.makeText(this@FavoritesActivity, "Network error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
