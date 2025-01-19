package de.hdm_stuttgart.myfavlocation.frontend.detail_views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import de.hdm_stuttgart.myfavlocation.R
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.enumerations.Type
import de.hdm_stuttgart.myfavlocation.frontend.tiled_list.FullscreenImageActivity
import de.hdm_stuttgart.myfavlocation.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_location_detail.*


class LocationDetailActivity: AppCompatActivity() {
    private lateinit var locationDetailViewModel: LocationDetailViewModel
    private var editing = false
    private lateinit var imageDetail: ImageView
    private lateinit var nameDetail: TextView
    private lateinit var ratingDetail: TextView
    private lateinit var ratingBarDetail: RatingBar
    private lateinit var categoryDetail: TextView
    private lateinit var proximityDetail:TextView
    private lateinit var descriptionDetail: TextView
    private lateinit var imageMap: ImageView
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var editButton: FloatingActionButton
    private lateinit var nameSwitcher: ViewSwitcher
    private lateinit var descriptionSwitcher: ViewSwitcher
    private lateinit var categoryMenu: AutoCompleteTextView
    private var isFavorite = false
    private lateinit var item: Location



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)
        editing = false
        val factory = InjectorUtils.provideLocationsDetailViewModelFactory(this)
        locationDetailViewModel = ViewModelProviders.of(this, factory).get(LocationDetailViewModel::class.java)

        imageDetail = findViewById(R.id.imagePlace_detail)
        nameDetail = findViewById(R.id.name_detail)
        ratingDetail = findViewById(R.id.rating_detail)
        ratingBarDetail = findViewById(R.id.ratingBar_detail)
        categoryDetail = findViewById(R.id.category_detail)
        proximityDetail = findViewById(R.id.proximity_detail)
        descriptionDetail = findViewById(R.id.description_detail)
        imageMap = findViewById(R.id.staticMap_detail)
        nameInput = findViewById(R.id.detailName_input)
        descriptionInput = findViewById(R.id.detailDescription_field_input)
        editButton = findViewById(R.id.fab_detail)
        nameSwitcher = findViewById(R.id.switcher_name)
        descriptionSwitcher = findViewById(R.id.switcher_description)
        categoryMenu = findViewById(R.id.filled_exposed_dropdown_detail)

        setDetails()

        if(item.type== Type.GOOGLE){
            editButton.hide()
        }

        editButton.setOnClickListener {
            editing= !editing
            if(editing) {
                editDetails()
            }
            else{
                saveEdit()
            }
        }
        imageDetail.setOnClickListener {
            val intent = Intent(it.context, FullscreenImageActivity::class.java)
            val photo: String = item.photoUrls!!
            intent.putExtra("fullscreen", photo)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        super.onCreateOptionsMenu(menu)
        val favButton: MenuItem? = menu?.findItem(R.id.favoriteButton)

        checkIfFavorite(favButton)

        return true
    }

    /*
    provides the data from the selected location object
     */
    @SuppressLint("SetTextI18n")
    private fun setDetails(){
        item = intent.getParcelableExtra("location")!!
        val position = item.position

        if (item.photoUrls != "") {
            Glide.with(this).load(item.photoUrls).centerCrop().into(imageDetail)
        }
        else {
            Glide.with(this).load(R.drawable.noimageavailable).centerCrop().into(imageDetail)
        }
        nameDetail.text = item.name
        ratingDetail.text = item.rating.toString()
        ratingBarDetail.rating = item.rating
        categoryDetail.text = item.category
        proximityDetail.text = "${item.localProximity}m"
        descriptionDetail.text = item.description

        val url = position?.let { locationDetailViewModel.staticMap(it) }
        Glide.with(this).load(url).into(imageMap)
    }

    /*
    switches views for editing of the values
     */
    private fun editDetails() {
        nameSwitcher.showNext()
        descriptionSwitcher.showNext()
        switcher_category.showNext()

            nameInput.setText(nameDetail.text)
            nameDetail.text = nameInput.text.toString()
            descriptionInput.setText(descriptionDetail.text)
            descriptionDetail.text = descriptionInput.text

            ratingBarDetail.setIsIndicator(false)
            ratingBarDetail.setOnRatingBarChangeListener { _, rating, _ ->
                ratingDetail.text = rating.toString()
        }

        val adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, R.layout.list_item)
        categoryMenu.setAdapter(adapter)

    }

    /*
    safes the new values to the location object
     */
    private fun saveEdit(){
        nameSwitcher.showPrevious()
        descriptionSwitcher.showPrevious()
        switcher_category.showPrevious()

        if(categoryMenu.text.toString()!= "Choose Category") {
            categoryDetail.text = categoryMenu.text.toString()
        }

        ratingBarDetail.setIsIndicator(true)
        nameDetail.text = nameInput.text
        descriptionDetail.text = descriptionInput.text

        item.name = nameDetail.text.toString()
        item.description = descriptionDetail.text.toString()
        item.rating = ratingBarDetail.rating
        item.category = categoryDetail.text.toString()

        locationDetailViewModel.saveChanges(item)
    }

    /*
    checks if the location is already in the favorite database
     */
    private fun checkIfFavorite(menuItem: MenuItem?){
        if(locationDetailViewModel.checkIfFavorite(item)) {
            if (menuItem != null) {
                menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24)
                isFavorite=true
            }
        }
        else {
            if (menuItem != null) {
                menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
                isFavorite=false
            }
        }
    }

    /*
    delete location from favorite database
     */
    private fun deleteLocation(){
        locationDetailViewModel.deleteFromFavorites(item)
    }

    /*
    add location to favorite database
     */
    private fun addToFavorite(){
        locationDetailViewModel.addToFavorite(item)
    }

    fun onButtonClick(item: MenuItem) {
        if (isFavorite) {
            deleteLocation()
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
            isFavorite = false
        } else {
            addToFavorite()
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24)
            isFavorite = true
        }
    }
}

