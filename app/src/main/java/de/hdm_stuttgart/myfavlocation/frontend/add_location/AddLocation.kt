package de.hdm_stuttgart.myfavlocation.frontend.add_location


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import de.hdm_stuttgart.myfavlocation.R
import de.hdm_stuttgart.myfavlocation.frontend.tiled_list.FullscreenImageActivity
import de.hdm_stuttgart.myfavlocation.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_addlocation.*



class AddLocation : AppCompatActivity() {

    private lateinit var addLocationViewModel: AddLocationViewModel
    private lateinit var photoPath: String
    private lateinit var imageView: ImageView

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addlocation)

        val factory = InjectorUtils.provideAddLocationViewModelFactory(this)
        addLocationViewModel = ViewModelProviders.of(this, factory).get(AddLocationViewModel::class.java)
        imageView = findViewById<ImageView>(R.id.imagePlace_addLocation)

        provideCategories()
        provideStaticMap()
        setImage()

        val fabCheck = findViewById<FloatingActionButton>(R.id.fab_add_location)
        fabCheck.setOnClickListener{

            if(saveLocation()) {
              finish()
            }
            else{
                Toast.makeText(this,"Error: No input", Toast.LENGTH_LONG)
            }

        }
        imageView.setOnClickListener {
            val intent = Intent(it.context, FullscreenImageActivity::class.java)
            intent.putExtra("fullscreen", photoPath)
            startActivity(intent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
    /*
    provides categories for dropdown menu
     */
    private fun provideCategories(){
        val adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, R.layout.list_item)
        val editTextFilledExposedDropdown: AutoCompleteTextView = findViewById(R.id.filled_exposed_dropdown)
        editTextFilledExposedDropdown.setAdapter(adapter)
    }

    /*
    provides static map with created link
     */
    private fun provideStaticMap(){
        Glide.with(this).load(addLocationViewModel.getURL()).into(staticMap)
    }

    /*
    safes location with inserted values to location object
     */
    private fun saveLocation(): Boolean{
        val nameTextView = findViewById<TextInputEditText>(R.id.outlinedTextFieldName_input)
        val descriptionTextView = findViewById<TextInputEditText>(R.id.outlinedTextFieldDescriptionInput)
        val categoryMenu = findViewById<AutoCompleteTextView>(R.id.filled_exposed_dropdown)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val image: String = intent.extras?.get("photoPath").toString()
        lateinit var name: String
        lateinit var description: String
        lateinit var category: String


        if(nameTextView.text.toString().isEmpty()){
        nameTextView.error = getString(R.string.errorNoInput)
            name = ""
            }
        else{
            name= nameTextView.text.toString()
        }
        if(descriptionTextView.text.toString().isEmpty()){
            descriptionTextView.error = getString(R.string.errorNoInput)
            description = ""
        }
        else{
            description = descriptionTextView.text.toString()
        }
        if(categoryMenu.text.toString() == "Choose Category"){
            categoryMenu.error = getString(R.string.errorNoInput)
            category = ""
        }
        else{
            category = categoryMenu.text.toString()
        }

        val rating: Float = ratingBar.rating

         if((name.length > 1) && (description.length > 1) && (category.length > 1)){
            addLocationViewModel.saveLocation(image,name,description,category, rating,this)
             return true
        } else{
            return false
        }
    }

    /*
    provides photographed image
     */
    private fun setImage(){
        val path = intent.extras?.get("photoPath").toString()
        photoPath = path
        Glide.with(this).load(path).centerCrop().into(imageView)

    }
}