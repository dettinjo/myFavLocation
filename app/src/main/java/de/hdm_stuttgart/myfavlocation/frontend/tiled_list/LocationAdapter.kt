package de.hdm_stuttgart.myfavlocation.frontend.tiled_list

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdm_stuttgart.myfavlocation.R
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.fav_location_db.LocationDB
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository
import de.hdm_stuttgart.myfavlocation.frontend.detail_views.LocationDetailActivity
import kotlin.random.Random.Default.nextInt

class LocationAdapter (private var activity: AppCompatActivity, var values : List<Location>) : RecyclerView.Adapter<LocationPairViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationPairViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_item, parent, false)
        return LocationPairViewHolder(v as LinearLayout)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LocationPairViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, LocationDetailActivity::class.java)
            intent.putExtra("location",values[position])
            activity.startActivity(intent)
        }

        holder.name.text = values[position].name
        holder.proximity.text = values[position].localProximity.toString() + "m"
        holder.ratingBar.text = values[position].rating.toString()

        if (values[position].photoUrls != "") {
            Glide.with(holder.imagePlaces).load(values[position].photoUrls).centerCrop()
                .into(holder.imagePlaces)
        }
        else {
            Glide.with(holder.imagePlaces).load(R.drawable.noimageavailable).centerCrop()
                .into(holder.imagePlaces)
        }

    }

}