package com.karabo.vibe.network

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.karabo.vibe.network.dto.AddFavoriteRequest
import com.karabotshivhase.vibe.R
import kotlinx.coroutines.launch

class VenueDetailActivity : AppCompatActivity() {

    private var venueId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)

        venueId = intent.getIntExtra(Constants.EXTRA_VENUE_ID, -1)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (venueId == -1) {
            Toast.makeText(this, "Invalid Venue ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadVenueDetails()

        val btnSaveFavorite = findViewById<Button>(R.id.btnSaveFavorite)
        btnSaveFavorite.setOnClickListener {
            saveToFavorites()
        }
    }

    private fun loadVenueDetails() {
        val progressBar = findViewById<ProgressBar>(R.id.detailProgressBar)
        val tvName = findViewById<TextView>(R.id.tvDetailVenueName)
        val tvAddress = findViewById<TextView>(R.id.tvDetailAddress)
        val tvHours = findViewById<TextView>(R.id.tvDetailHours)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupDetailTags)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val venue = RetrofitClient.api.getVenueById(venueId)
                progressBar.visibility = View.GONE

                tvName.text = venue.name
                tvAddress.text = venue.address ?: "Pretoria, SA"
                tvHours.text = venue.openingHours ?: "Hours not specified"

                chipGroup.removeAllViews()
                venue.tags.forEach { tag ->
                    val chip = Chip(this@VenueDetailActivity).apply {
                        text = tag
                        isClickable = false
                        isCheckable = false
                    }
                    chipGroup.addView(chip)
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@VenueDetailActivity, "Failed to load details: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveToFavorites() {
        val btnSaveFavorite = findViewById<Button>(R.id.btnSaveFavorite)
        btnSaveFavorite.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.addFavorite(AddFavoriteRequest(venueId = venueId))
                if (response.isSuccessful) {
                    Toast.makeText(this@VenueDetailActivity, "Saved to favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    btnSaveFavorite.isEnabled = true
                    Toast.makeText(this@VenueDetailActivity, "Failed to save favorite", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                btnSaveFavorite.isEnabled = true
                Toast.makeText(this@VenueDetailActivity, "Network error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
