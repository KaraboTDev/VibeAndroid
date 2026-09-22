package com.karabo.vibe.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.karabo.vibe.network.dto.FavoriteResponse
import com.karabotshivhase.vibe.R

class FavoriteAdapter(
    private val items: List<FavoriteResponse>,
    private val onItemClick: (FavoriteResponse) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvVenueName: TextView = view.findViewById(R.id.tvVenueName)
        val tvVenueAddress: TextView = view.findViewById(R.id.tvVenueAddress)
        val tvVenueHours: TextView = view.findViewById(R.id.tvVenueHours)
        val tvSyncStatus: TextView = view.findViewById(R.id.tvSyncStatus)
        val chipGroupItemTags: ChipGroup = view.findViewById(R.id.chipGroupItemTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = items[position]
        holder.tvVenueName.text = favorite.name
        holder.tvVenueAddress.text = favorite.address ?: "Pretoria, SA"
        
        // Hide standard hours since FavoriteResponse doesn't supply it directly
        holder.tvVenueHours.visibility = View.GONE
        
        // Make syncStatus container visible and render its value
        holder.tvSyncStatus.visibility = View.VISIBLE
        holder.tvSyncStatus.text = favorite.syncStatus
        
        // Clear any placeholder chips since tags aren't present in FavoriteResponse DTO
        holder.chipGroupItemTags.removeAllViews()

        holder.itemView.setOnClickListener {
            onItemClick(favorite)
        }
    }

    override fun getItemCount(): Int = items.size
}
