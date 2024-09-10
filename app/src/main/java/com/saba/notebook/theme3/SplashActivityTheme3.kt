package com.saba.notebook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

class SplashActivityTheme3 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash3)

        // تأخیر دو ثانیه‌ای قبل از شروع MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivityTheme3::class.java))
            finish()
        }, 2000) // 2000 میلی‌ثانیه برابر با 2 ثانیه است
    }
}