package de.hdm_stuttgart.myfavlocation.frontend.tiled_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import de.hdm_stuttgart.myfavlocation.R

class FullscreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_fullscreen_image)
        setContentView(R.layout.activity_fullscreen_image)
        val imageView: ImageView = findViewById(R.id.imageFullscreen)
        val photo: String? = intent.extras?.getString("fullscreen")

        if(photo != "") {
            Glide.with(this).load(photo).into(imageView)
        }
        else {
            Glide.with(this).load(R.drawable.noimageavailable).into(imageView)
        }
        imageView.setOnClickListener {
            finish()
        }
    }
}