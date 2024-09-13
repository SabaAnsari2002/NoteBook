package com.saba.notebook

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class ThemeCustomizationActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_customization)

        // دسترسی به SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // کلیک لیسنر برای شخصی‌سازی تم 9
        val splashScreenButton = findViewById<LinearLayout>(R.id.splash_screen)
        splashScreenButton.setOnClickListener {
            // تنظیم فلگ شخصی‌سازی تم 9 به true
            sharedPreferences.edit().putBoolean("isTheme9Customized", true).apply()

            // باز کردن صفحه ImagesActivity
            val intent = Intent(this, ImagesActivity::class.java)
            startActivity(intent)
        }
    }
}
