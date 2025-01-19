package de.hdm_stuttgart.myfavlocation.frontend.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
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

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val factory = context?.let {  InjectorUtils.provideSearchViewModelFactory(it) }
        searchViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val searchInput = root.findViewById<SearchView>(R.id.search_input)
        val btnSearch = root.findViewById<Button>(R.id.btn_search)
        searchInput.setOnClickListener{
            searchInput.isIconified=false
        }

        btnSearch.setOnClickListener {
            searchViewModel.search(searchInput.query.toString())
        }

        val placesRecyclerView = root.findViewById<RecyclerView>(R.id.search_results)
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
        searchViewModel.locations.observe(viewLifecycleOwner, observer)

        val errorMsg = Observer<String> {
            s -> Toast.makeText(context, s, Toast.LENGTH_LONG).show()
        }
        searchViewModel.error.observe(viewLifecycleOwner, errorMsg)

        return root


    }
}
