package com.saba.notebook

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImagesActivity : AppCompatActivity() {

    private lateinit var dbHelper: ImageDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnSelectFromGallery: Button
    private lateinit var progressDialog: ProgressDialog

    companion object {
        private const val PREF_IMAGES_LOADED = "images_loaded"
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // دریافت حالت دارک مود یا لایت مود
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        applyMode(isDarkMode)  // فراخوانی تابع برای اعمال حالت مود

        dbHelper = ImageDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery)

        // Initialize and show ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("لطفا منتظر بمانید...")
            setCancelable(false)
            show()
        }

        val imageType = intent.getStringExtra("IMAGE_TYPE") ?: "SPLASH"

        // Check if images have been loaded before
        val imagesLoaded = sharedPreferences.getBoolean(PREF_IMAGES_LOADED, false)

        // Load images from database asynchronously
        lifecycleScope.launch {
            if (!imagesLoaded) {
                addDrawableImagesToDatabase()
                sharedPreferences.edit().putBoolean(PREF_IMAGES_LOADED, true).apply()
            }

            val imageList = loadImagesFromDatabase()
            setupRecyclerView(imageList)

            // Hide ProgressDialog after images are loaded
            progressDialog.dismiss()
        }

        btnSelectFromGallery.setOnClickListener {
            openGallery()
        }
    }
    private fun applyMode(isDarkMode: Boolean) {
        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val btnSelectFromGallery = findViewById<Button>(R.id.btnSelectFromGallery)

        if (isDarkMode) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
            btnSelectFromGallery.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        } else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            btnSelectFromGallery.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }
    }


    private suspend fun loadImagesFromDatabase(): MutableList<ByteArray> {
        return withContext(Dispatchers.IO) {
            dbHelper.getAllImages().toMutableList()
        }
    }

    private fun setupRecyclerView(imageList: MutableList<ByteArray>) {
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = ImageAdapter(imageList) { selectedImage ->
            val imageType = intent.getStringExtra("IMAGE_TYPE") ?: "SPLASH"
            saveSelectedImageToPreferences(selectedImage, imageType)
            showSuccessMessage(imageType)

            Handler().postDelayed({ finish() }, 500) // 0.5 second delay
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
                lifecycleScope.launch {
                    val imageByteArray = contentResolver.openInputStream(uri)?.readBytes()
                    imageByteArray?.let { byteArray ->
                        withContext(Dispatchers.IO) {
                            dbHelper.insertImage(byteArray)
                        }

                        (recyclerView.adapter as ImageAdapter).addImage(byteArray)

                        val imageType = intent.getStringExtra("IMAGE_TYPE") ?: "SPLASH"
                        saveSelectedImageToPreferences(byteArray, imageType)
                        showSuccessMessage(imageType)

                        Handler().postDelayed({ finish() }, 500)
                    }
                }
            }
        }
    }
    override fun onStop() {
        super.onStop()

        val imageType = intent.getStringExtra("IMAGE_TYPE") ?: "SPLASH"

        // بررسی انتخاب تصویر برای صفحه فعلی
        val selectedImage = when (imageType) {
            "REGISTER" -> sharedPreferences.getString("SELECTED_REGISTER_IMAGE", null)
            "LOGIN" -> sharedPreferences.getString("SELECTED_LOGIN_IMAGE", null)
            "MAIN" -> sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)
            "HOME" -> sharedPreferences.getString("SELECTED_HOME_IMAGE", null)
            "ADD_NOTE" -> sharedPreferences.getString("SELECTED_ADD_NOTE_IMAGE", null)
            else -> sharedPreferences.getString("SELECTED_SPLASH_IMAGE", null)
        }

        // اگر تصویری انتخاب نشده باشد، تصویر پیش‌فرض را ذخیره می‌کنیم
        if (selectedImage == null) {
            val defaultImageByteArray = getDefaultImage()
            saveSelectedImageToPreferences(defaultImageByteArray, imageType)
            Toast.makeText(this, "تصویر پیش‌فرض برای $imageType انتخاب شد", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDefaultImage(): ByteArray {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_image) // تصویر پیش‌فرض
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private suspend fun addDrawableImagesToDatabase() {
        withContext(Dispatchers.IO) {
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
                R.drawable.theme_fifteen9, R.drawable.theme_sixteen1,
                R.drawable.theme_sixteen2,R.drawable.theme_sixteen3,
                R.drawable.theme_sixteen4,R.drawable.theme_sixteen5,
                R.drawable.theme_sixteen6,R.drawable.theme_sixteen7,
                R.drawable.theme_sixteen8,R.drawable.theme_sixteen9,

                )

            for (drawableId in drawableIds) {
                val bitmap = BitmapFactory.decodeResource(resources, drawableId)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val imageByteArray = stream.toByteArray()
                dbHelper.insertImage(imageByteArray)
            }
        }
    }

    private fun saveSelectedImageToPreferences(imageByteArray: ByteArray, imageType: String) {
        val prefsEditor = sharedPreferences.edit()
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
