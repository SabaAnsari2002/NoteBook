package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val selectedTheme = sharedPreferences.getString("SELECTED_THEME", "theme1")
        val selectedSplashImage = sharedPreferences.getString("SELECTED_SPLASH_IMAGE", null)

        if (selectedTheme == "theme9" && selectedSplashImage != null) {
            // Display the selected image for theme 9
            setContentView(R.layout.activity_splash9)
            val imageView = findViewById<ImageView>(R.id.splashImageView)
            val imageByteArray = android.util.Base64.decode(selectedSplashImage, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            imageView.setImageBitmap(bitmap)
        } else {
            // Handle other themes
            when (selectedTheme) {
                "theme2" -> setContentView(R.layout.activity_splash2)
                "theme3" -> setContentView(R.layout.activity_splash3)
                "theme4" -> setContentView(R.layout.activity_splash4)
                "theme5" -> setContentView(R.layout.activity_splash5)
                "theme6" -> setContentView(R.layout.activity_splash6)
                "theme10" -> setContentView(R.layout.activity_splash10)

                else -> setContentView(R.layout.activity_splash1)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when (selectedTheme) {
                "theme2" -> Intent(this, MainActivityTheme2::class.java)
                "theme3" -> Intent(this, MainActivityTheme3::class.java)
                "theme4" -> Intent(this, MainActivityTheme4::class.java)
                "theme5" -> Intent(this, MainActivityTheme5::class.java)
                "theme6" -> Intent(this, MainActivityTheme6::class.java)
                "theme9" -> Intent(this, MainActivityTheme9::class.java)
                "theme10" -> Intent(this, MainActivityTheme10::class.java)

                else -> Intent(this, MainActivityTheme1::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
