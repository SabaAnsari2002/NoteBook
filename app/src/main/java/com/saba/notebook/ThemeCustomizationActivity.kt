package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ThemeCustomizationActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_customization)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            userId = sharedPreferences.getInt("userId", -1)
        }

        if (userId == -1) {
            startActivity(Intent(this, LoginActivityTheme1::class.java))
            finish()
            return
        }

        val splashScreenButton = findViewById<LinearLayout>(R.id.splash_screen)
        splashScreenButton.setOnClickListener {
            sharedPreferences.edit().apply {
                putBoolean("isTheme9Customized", true)
                putString("SELECTED_THEME", "theme10")
                apply()
            }

            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("IMAGE_TYPE", "SPLASH")
            startActivity(intent)
        }

        val mainButton = findViewById<LinearLayout>(R.id.main)
        mainButton.setOnClickListener {
            sharedPreferences.edit().apply {
                putBoolean("isTheme9Customized", true)
                putString("SELECTED_THEME", "theme10")
                putString("SELECTED_MAIN_IMAGE", "your_image_data_here") // Replace with actual image data
                apply()
            }

            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("IMAGE_TYPE", "MAIN")
            startActivity(intent)
        }

        val registerButton = findViewById<LinearLayout>(R.id.register)
        registerButton.setOnClickListener {
            sharedPreferences.edit().apply {
                putBoolean("isTheme9Customized", true)
                putString("SELECTED_REGISTER_IMAGE", "your_register_image_data_here") // ذخیره تصویر رجیستر
                apply()
            }

            // هدایت به ImagesActivity برای انتخاب عکس برای رجیستر
            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("IMAGE_TYPE", "REGISTER")
            startActivity(intent)
        }

        val loginButton = findViewById<LinearLayout>(R.id.login)
        loginButton.setOnClickListener {
            sharedPreferences.edit().apply {
                putBoolean("isLoginCustomized", true) // شخصی‌سازی صفحه لاگین
                putString("SELECTED_LOGIN_IMAGE", "your_login_image_data_here") // ذخیره تصویر لاگین
                apply()
            }

            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("IMAGE_TYPE", "LOGIN")
            startActivity(intent)
        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        sharedPreferences.edit().putString("SELECTED_THEME", "theme10").apply()

        val intent = Intent(this, HomeActivityTheme10::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }
}