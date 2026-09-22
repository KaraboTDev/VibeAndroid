package com.karabo.vibe.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.karabo.vibe.network.dto.VenueResponse
import com.karabotshivhase.vibe.R

class VenueAdapter(
    private val items: List<VenueResponse>,
    private val onItemClick: (VenueResponse) -> Unit
) : RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {

    class VenueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvVenueName: TextView = view.findViewById(R.id.tvVenueName)
        val tvVenueAddress: TextView = view.findViewById(R.id.tvVenueAddress)
        val tvVenueHours: TextView = view.findViewById(R.id.tvVenueHours)
        val chipGroupItemTags: ChipGroup = view.findViewById(R.id.chipGroupItemTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venue = items[position]
        holder.tvVenueName.text = venue.name
        holder.tvVenueAddress.text = venue.address ?: "Pretoria, SA"
        holder.tvVenueHours.text = venue.openingHours ?: "Hours not specified"
        
        holder.chipGroupItemTags.removeAllViews()
        venue.tags.forEach { tag ->
            val chip = Chip(holder.itemView.context).apply {
                text = tag
                isClickable = false
                isCheckable = false
            }
            holder.chipGroupItemTags.addView(chip)
        }

        holder.itemView.setOnClickListener {
            onItemClick(venue)
        }
    }

    override fun getItemCount(): Int = items.size
}
