package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class MainActivityTheme9 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val selectedMainImage = sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)

        if (selectedMainImage != null) {
            // Decode and set the image in the ImageView
            val imageByteArray = android.util.Base64.decode(selectedMainImage, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            val imageView = findViewById<ImageView>(R.id.mainImageView)
            imageView.setImageBitmap(bitmap)
        }

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val userId = sharedPreferences.getInt("userId", -1)
            if (userId != -1) {
                val intent = Intent(this, HomeActivityTheme9::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } else {
                // Redirect to login page if userId is not found
                startActivity(Intent(this, LoginActivityTheme9::class.java))
                finish()
            }
            return
        }

        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivityTheme9::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivityTheme9::class.java))
        }
    }
}