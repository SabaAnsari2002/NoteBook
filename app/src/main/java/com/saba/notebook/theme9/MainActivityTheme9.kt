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

        // دسترسی به SharedPreferences برای بررسی تم و تصویر انتخاب شده
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // بررسی اینکه آیا تصویر برای تم شخصی‌سازی شده انتخاب شده است
        val isThemeCustomized = sharedPreferences.getBoolean("isTheme9Customized", false)
        val selectedMainImage = sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)
        val selectedRegisterImage = sharedPreferences.getString("SELECTED_REGISTER_IMAGE", null)
        val selectedLoginImage = sharedPreferences.getString("SELECTED_LOGIN_IMAGE", null)


        if (isThemeCustomized && selectedMainImage != null) {
            // اگر تم شخصی‌سازی شده و تصویر انتخاب شده باشد، تصویر را نمایش دهید
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
                // اگر کاربر لاگین نکرده است، به صفحه لاگین بروید
                startActivity(Intent(this, LoginActivityTheme9::class.java))
                finish()
            }
            return
        }

        // تنظیم دکمه‌های ثبت‌نام و ورود
        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            // بررسی انتخاب تصویر برای رجیستر
            if (selectedRegisterImage != null) {
                // اگر تصویری برای رجیستر انتخاب شده، به RegisterActivityTheme9 بروید
                startActivity(Intent(this, RegisterActivityTheme9::class.java))
            } else {
                // در غیر این صورت، به RegisterActivityTheme10 بروید
                startActivity(Intent(this, RegisterActivityTheme10::class.java))
            }
        }

        btnLogin.setOnClickListener {
            if (selectedLoginImage != null) {
                startActivity(Intent(this, LoginActivityTheme9::class.java))
            } else {
                startActivity(Intent(this, LoginActivityTheme10::class.java))
            }
        }
    }
}
