package com.karabo.vibe.network

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.karabotshivhase.vibe.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Setup bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUtils.setupBottomNavigation(this, bottomNav, R.id.navigation_home)

        val chipGroupMoods = findViewById<ChipGroup>(R.id.chipGroupMoods)
        val btnShowMe = findViewById<Button>(R.id.btnShowMe)

        btnShowMe.setOnClickListener {
            val checkedChipId = chipGroupMoods.checkedChipId
            if (checkedChipId == -1) {
                Toast.makeText(this, "Please select a mood filter chip first", Toast.LENGTH_SHORT).show()
            } else {
                val selectedChip = findViewById<Chip>(checkedChipId)
                val moodTag = selectedChip.text.toString()

                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra(Constants.EXTRA_MOOD_TAG, moodTag)
                startActivity(intent)
            }
        }
    }
}
