package com.example.beta

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        val imageView: ImageView = findViewById(R.id.imageView)

        // Use Glide to load the image from the Intent extra
        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .into(imageView)
    }
}



