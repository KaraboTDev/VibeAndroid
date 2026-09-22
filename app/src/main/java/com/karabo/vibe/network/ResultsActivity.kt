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

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNav, -1) // -1 because it's a dynamic result stack screen

        val moodTag = intent.getStringExtra(Constants.EXTRA_MOOD_TAG)
        val tvResultsHeading = findViewById<TextView>(R.id.tvResultsHeading)
        tvResultsHeading.text = if (!moodTag.isNullOrEmpty()) "$moodTag spots near you" else "Spots near you"

        val rvVenues = findViewById<RecyclerView>(R.id.rvVenues)
        rvVenues.layoutManager = LinearLayoutManager(this)

        val progressBar = findViewById<ProgressBar>(R.id.resultsProgressBar)
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyState)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val venues = RetrofitClient.api.getVenues(
                    mood = moodTag,
                    lat = Constants.DEFAULT_LATITUDE,
                    lng = Constants.DEFAULT_LONGITUDE
                )

                progressBar.visibility = View.GONE
                if (venues.isEmpty()) {
                    tvEmptyState.visibility = View.VISIBLE
                    rvVenues.visibility = View.GONE
                } else {
                    tvEmptyState.visibility = View.GONE
                    rvVenues.visibility = View.VISIBLE
                    rvVenues.adapter = VenueAdapter(venues) { selectedVenue ->
                        val intent = Intent(this@ResultsActivity, VenueDetailActivity::class.java).apply {
                            putExtra(Constants.EXTRA_VENUE_ID, selectedVenue.id)
                        }
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                tvEmptyState.visibility = View.VISIBLE
                tvEmptyState.text = "Error loading spots"
                Toast.makeText(this@ResultsActivity, "Network error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
