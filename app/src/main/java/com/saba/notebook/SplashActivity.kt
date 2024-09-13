package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // دسترسی به SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // بررسی تم انتخاب شده از SharedPreferences
        val selectedTheme = sharedPreferences.getString("SELECTED_THEME", "theme1")

        // انتخاب صفحه اسپلش براساس تم انتخاب شده
        when (selectedTheme) {
            "theme2" -> setContentView(R.layout.activity_splash2)
            "theme3" -> setContentView(R.layout.activity_splash3)
            "theme4" -> setContentView(R.layout.activity_splash4)
            "theme5" -> setContentView(R.layout.activity_splash5)
            "theme6" -> setContentView(R.layout.activity_splash6)

            else -> setContentView(R.layout.activity_splash1)
        }

        // تأخیر دو ثانیه‌ای قبل از شروع MainActivity مربوط به تم
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when (selectedTheme) {
                "theme2" -> Intent(this, MainActivityTheme2::class.java)
                "theme3" -> Intent(this, MainActivityTheme3::class.java)
                "theme4" -> Intent(this, MainActivityTheme4::class.java)
                "theme5" -> Intent(this, MainActivityTheme5::class.java)
                "theme6" -> Intent(this, MainActivityTheme6::class.java)

                else -> Intent(this, MainActivityTheme1::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000) // 2000 میلی‌ثانیه معادل 2 ثانیه است
    }
}
