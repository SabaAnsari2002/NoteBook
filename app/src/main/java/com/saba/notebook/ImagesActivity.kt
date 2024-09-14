package com.saba.notebook

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream

class ImagesActivity : AppCompatActivity() {

    private lateinit var dbHelper: ImageDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        dbHelper = ImageDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)

        // Add images from drawable to database (one-time setup)
        addDrawableImagesToDatabase()

        val imageList = dbHelper.getAllImages()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ImageAdapter(imageList) { selectedImage ->
            // Handle image selection
            saveSelectedImageToPreferences(selectedImage)
            finish() // Close activity after selecting an image
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

    private fun saveSelectedImageToPreferences(imageByteArray: ByteArray) {
        val prefsEditor = sharedPreferences.edit()
        // Convert ByteArray to Base64 String for storage
        val base64Image = android.util.Base64.encodeToString(imageByteArray, android.util.Base64.DEFAULT)
        prefsEditor.putString("SELECTED_SPLASH_IMAGE", base64Image)
        prefsEditor.apply()
    }
}
