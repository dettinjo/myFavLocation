package de.hdm_stuttgart.myfavlocation.frontend.fav_locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdm_stuttgart.myfavlocation.R
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.frontend.tiled_list.LocationAdapter
import de.hdm_stuttgart.myfavlocation.utilities.InjectorUtils

class FavLocationsFragment : Fragment() {

    private lateinit var favLocationsViewModel: FavLocationViewModel
    private lateinit var locationsRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val factory = context?.let { InjectorUtils.provideFavLocationsViewModelFactory(it) }
        favLocationsViewModel = ViewModelProviders.of(this, factory).get(FavLocationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_fav_locations, container, false)


        locationsRecyclerView = root.findViewById(R.id.favoriteLocations)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        locationsRecyclerView.layoutManager = layoutManager
        val adapter = LocationAdapter(
            requireActivity() as AppCompatActivity,
            mutableListOf()
        )
        locationsRecyclerView.adapter = adapter
        val observerOwn = Observer<List<Location>> {
                l -> adapter.values = l
            adapter.notifyDataSetChanged()
        }
        favLocationsViewModel.locations.observe(viewLifecycleOwner, observerOwn)

        favLocationsViewModel.init()
        return root
    }

    override fun onResume() {
        super.onResume()
        favLocationsViewModel.pull()
    }
}