package de.hdm_stuttgart.myfavlocation.frontend

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdm_stuttgart.myfavlocation.R
import de.hdm_stuttgart.myfavlocation.backend.ServiceManager
import de.hdm_stuttgart.myfavlocation.backend.data.ACCESS_FINE_LOCATION_CODE
import de.hdm_stuttgart.myfavlocation.backend.data.REQUEST_IMAGE_CAPTURE
import de.hdm_stuttgart.myfavlocation.frontend.add_location.AddLocation
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var currentPhotoPath: String
    private lateinit var photoPath: String
    private var cameraOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_fav_locations
            )
        )

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        //check necessary permissions
        if (checkSelfPermission(permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        } else {
            ServiceManager.getInstance().notifyAllServicePermissionGranted()
        }


        val addLocFab = findViewById<FloatingActionButton>(R.id.main_addLoc_fab)

        //Fab for Camera
        addLocFab.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        if (cameraOpened) {
            cameraOpened = false

            if (photoPath != "") {
                val intent = Intent(this, AddLocation::class.java)
                intent.putExtra("photoPath", photoPath)
                startActivity(intent)
            }
        }
    }

    /**
     *  dispatch a camera intent and store the taken image to a file (createImageFile())
     */
    private fun dispatchTakePictureIntent() {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "Camera-Permission is required in order to add new locations",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )

                    takePictureIntent.putExtra(
                        MediaStore.EXTRA_SCREEN_ORIENTATION,
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        REQUEST_IMAGE_CAPTURE
                    )
                    cameraOpened = true
                    photoPath = photoFile.absolutePath
                }
            }
        }
    }

    /**
     * if no image was taken in the camera intent, set 'photopath' to an empty string
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_CANCELED) {
            photoPath = ""
        }

    }

    /**
     * create a file to which an image can be saved later
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", // prefix
            ".jpg", // suffix
            storageDir // directory
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    /**
     * request different permissions
     * currently:
     *  - ACCESS_FINE_LOCATION
     *  - CAMERA
     */
    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission.ACCESS_FINE_LOCATION
            ) ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission.CAMERA
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permissions needed")
                .setMessage("MyFavLocation uses GPS and Camera for the majority of its features and cannot be used without them")
                .setPositiveButton("ok") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(permission.ACCESS_FINE_LOCATION, permission.CAMERA),
                        ACCESS_FINE_LOCATION_CODE
                    )
                }
                .setNegativeButton("close app") { _, _ -> exitProcess(0) }
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.ACCESS_FINE_LOCATION, permission.CAMERA),
                ACCESS_FINE_LOCATION_CODE
            )
        }
    }

    /**
     * notify the ServiceManager when any requests change
     * (currently the request-code gets ignored, this could be changed to a more sophisticated implementation if needed)
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       ServiceManager.getInstance().notifyAllServicePermissionGranted()
    }
}
