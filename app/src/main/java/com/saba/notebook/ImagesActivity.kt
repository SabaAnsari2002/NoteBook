package com.saba.notebook

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream

class ImagesActivity : AppCompatActivity() {

    private lateinit var dbHelper: ImageDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREF_IMAGES_LOADED = "images_loaded"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        dbHelper = ImageDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)

        val imageType = intent.getStringExtra("IMAGE_TYPE") ?: "SPLASH"

        // Check if images have been loaded before
        val imagesLoaded = sharedPreferences.getBoolean(PREF_IMAGES_LOADED, false)

        if (!imagesLoaded) {
            // Add images from drawable to database (one-time setup)
            addDrawableImagesToDatabase()

            // Mark images as loaded
            sharedPreferences.edit().putBoolean(PREF_IMAGES_LOADED, true).apply()
        }

        // Load images from database
        val imageList = dbHelper.getAllImages()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ImageAdapter(imageList) { selectedImage ->
            // Handle image selection
            saveSelectedImageToPreferences(selectedImage, imageType)
            showSuccessMessage(imageType)

            // Delay before closing the activity
            Handler().postDelayed({
                finish()
            }, 500) // 0.5 seconds delay
        }
    }

    private fun addDrawableImagesToDatabase() {
        val drawableIds = listOf(
            R.drawable.add_note1, R.drawable.add_note2,
            R.drawable.add_note3, R.drawable.add_note4, R.drawable.add_note5
        )
        for (drawableId in drawableIds) {
            val bitmap = BitmapFactory.decodeResource(resources, drawableId)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageByteArray = stream.toByteArray()
            dbHelper.insertImage(imageByteArray)
        }
    }

    private fun saveSelectedImageToPreferences(imageByteArray: ByteArray, imageType: String) {
        val prefsEditor = sharedPreferences.edit()
        // Convert ByteArray to Base64 String for storage
        val base64Image = android.util.Base64.encodeToString(imageByteArray, android.util.Base64.DEFAULT)

        when (imageType) {
            "MAIN" -> prefsEditor.putString("SELECTED_MAIN_IMAGE", base64Image)
            "REGISTER" -> prefsEditor.putString("SELECTED_REGISTER_IMAGE", base64Image)
            "LOGIN" -> prefsEditor.putString("SELECTED_LOGIN_IMAGE", base64Image)
            "HOME" -> prefsEditor.putString("SELECTED_HOME_IMAGE", base64Image)
            else -> prefsEditor.putString("SELECTED_SPLASH_IMAGE", base64Image)
        }

        prefsEditor.apply()
    }

    private fun showSuccessMessage(imageType: String) {
        val messageText = when (imageType) {
            "MAIN" -> "Image successfully selected for main screen"
            "SPLASH" -> "Image successfully selected for splash screen"
            "REGISTER" -> "Image successfully selected for register screen"
            "LOGIN" -> "Image successfully selected for login screen"
            "HOME" -> "Image successfully selected for home screen"
            else -> "Image successfully selected"
        }
        Toast.makeText(this, messageText, Toast.LENGTH_SHORT).show()
    }
}
