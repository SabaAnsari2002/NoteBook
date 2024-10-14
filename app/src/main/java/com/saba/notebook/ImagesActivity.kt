package com.saba.notebook

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream

class ImagesActivity : AppCompatActivity() {

    private lateinit var dbHelper: ImageDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnSelectFromGallery: Button

    companion object {
        private const val PREF_IMAGES_LOADED = "images_loaded"
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        dbHelper = ImageDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery)

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
        val imageList = dbHelper.getAllImages().toMutableList()
        Log.d("ImagesActivity", "Number of images loaded: ${imageList.size}")

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = ImageAdapter(imageList) { selectedImage ->
            // Handle image selection
            saveSelectedImageToPreferences(selectedImage, imageType)
            showSuccessMessage(imageType)

            // Delay before closing the activity
            Handler().postDelayed({
                finish()
            }, 500) // 0.5 seconds delay
        }

        btnSelectFromGallery.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let { uri ->
                val imageByteArray = contentResolver.openInputStream(uri)?.readBytes()
                imageByteArray?.let { byteArray ->
                    // Save the selected image to the database
                    dbHelper.insertImage(byteArray)

                    // Update the RecyclerView
                    (recyclerView.adapter as ImageAdapter).addImage(byteArray)

                    // Handle image selection
                    val imageType = intent.getStringExtra("IMAGE_TYPE") ?: "SPLASH"
                    saveSelectedImageToPreferences(byteArray, imageType)
                    showSuccessMessage(imageType)

                    // Delay before closing the activity
                    Handler().postDelayed({
                        finish()
                    }, 500)
                }
            }
        }
    }

    private fun addDrawableImagesToDatabase() {
        val drawableIds = listOf(
            R.drawable.theme_eleven1, R.drawable.theme_eleven2,
            R.drawable.theme_eleven3, R.drawable.theme_eleven4,
            R.drawable.theme_eleven5, R.drawable.theme_eleven6,
            R.drawable.theme_eleven7, R.drawable.theme_eleven8,
            R.drawable.theme_eleven9, R.drawable.theme_twelve1,
            R.drawable.theme_twelve2, R.drawable.theme_twelve3,
            R.drawable.theme_twelve4, R.drawable.theme_twelve5,
            R.drawable.theme_twelve6, R.drawable.theme_twelve7,
            R.drawable.theme_twelve8, R.drawable.theme_twelve9,
            R.drawable.theme_thirteen1, R.drawable.theme_thirteen2,
            R.drawable.theme_thirteen3, R.drawable.theme_thirteen4,
            R.drawable.theme_thirteen5, R.drawable.theme_thirteen6,
            R.drawable.theme_thirteen7, R.drawable.theme_thirteen8,
            R.drawable.theme_thirteen9, R.drawable.theme_fourteen1,
            R.drawable.theme_fourteen2, R.drawable.theme_fourteen3,
            R.drawable.theme_fourteen4, R.drawable.theme_fourteen5,
            R.drawable.theme_fourteen6, R.drawable.theme_fourteen7,
            R.drawable.theme_fourteen8, R.drawable.theme_fourteen9,
            R.drawable.theme_fifteen1, R.drawable.theme_fifteen2,
            R.drawable.theme_fifteen3, R.drawable.theme_fifteen4,
            R.drawable.theme_fifteen5, R.drawable.theme_fifteen6,
            R.drawable.theme_fifteen7, R.drawable.theme_fifteen8,
            R.drawable.theme_fifteen9,
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
            "ADD_NOTE" -> prefsEditor.putString("SELECTED_ADD_NOTE_IMAGE", base64Image)
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
            "ADD_NOTE" -> "Image successfully selected for add note screen"
            else -> "Image successfully selected"
        }
        Toast.makeText(this, messageText, Toast.LENGTH_SHORT).show()
    }
}