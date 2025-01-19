package de.hdm_stuttgart.myfavlocation.frontend.tiled_list

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdm_stuttgart.myfavlocation.R

class LocationPairViewHolder (itemView: LinearLayout) : RecyclerView.ViewHolder(itemView) {
    val imagePlaces : ImageView = itemView.findViewById(R.id.card_image)
    val name : TextView = itemView.findViewById(R.id.card_name)
    val proximity : TextView = itemView.findViewById(R.id.card_proximity)
    val ratingBar : TextView = itemView.findViewById(R.id.card_rating)
    val imageFavorite: ImageView = itemView.findViewById(R.id.card_favorite)
}