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

        // دسترسی به SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // دریافت userId از Intent یا SharedPreferences
        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            userId = sharedPreferences.getInt("userId", -1)
        }

        if (userId == -1) {
            // اگر userId موجود نباشد، به صفحه ورود برگردید
            startActivity(Intent(this, LoginActivityTheme1::class.java))
            finish()
            return
        }

        // کلیک لیسنر برای شخصی‌سازی تم 9
        val splashScreenButton = findViewById<LinearLayout>(R.id.splash_screen)
        splashScreenButton.setOnClickListener {
            // تنظیم فلگ شخصی‌سازی تم 9 به true
            sharedPreferences.edit().apply {
                putBoolean("isTheme9Customized", true)
                putString("SELECTED_THEME", "theme10")
                apply()
            }

            // باز کردن صفحه ImagesActivity
            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // اعمال تم 10 (اگر قبلاً اعمال نشده باشد)
        if (sharedPreferences.getString("SELECTED_THEME", "") != "theme10") {
            sharedPreferences.edit().putString("SELECTED_THEME", "theme10").apply()
        }

        // هدایت کاربر به صفحه اصلی با تم جدید
        val intent = Intent(this, HomeActivityTheme10::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }
}