package de.hdm_stuttgart.myfavlocation.frontend.home

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

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var placesRecyclerView: RecyclerView
    companion object {lateinit var adapter: LocationAdapter}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val factory = context?.let {
            InjectorUtils.provideHomeViewModelFactory(it) }
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        placesRecyclerView = root.findViewById(R.id.places)
        adapter = LocationAdapter(
            requireActivity() as AppCompatActivity,
            mutableListOf()
        )
        placesRecyclerView.adapter = adapter
        val observer = Observer<List<Location>> {
                l -> adapter.values = l
            adapter.notifyDataSetChanged()
        }
        homeViewModel.locations.observe(viewLifecycleOwner, observer)
        return root
    }

    override fun onResume() {
        super.onResume()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        placesRecyclerView.layoutManager = layoutManager
        val adapter = LocationAdapter(
            requireActivity() as AppCompatActivity,
            mutableListOf()
        )
        placesRecyclerView.adapter = adapter
        val observer = Observer<List<Location>> {
                l -> adapter.values = l
            adapter.notifyDataSetChanged()
        }
        homeViewModel.locations.observe(viewLifecycleOwner, observer)
        homeViewModel.get()
    }
}
