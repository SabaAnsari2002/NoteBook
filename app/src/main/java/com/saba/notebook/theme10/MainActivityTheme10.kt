package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivityTheme10 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main10)

        // دسترسی به SharedPreferences برای بررسی وضعیت تم و تصویر انتخاب شده
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // بررسی اینکه آیا تم شخصی‌سازی شده است و آیا تصویری برای صفحه رجیستر انتخاب شده است
        val isThemeCustomized = sharedPreferences.getBoolean("isTheme9Customized", false)
        val selectedRegisterImage = sharedPreferences.getString("SELECTED_REGISTER_IMAGE", null)
        val selectedLoginImage = sharedPreferences.getString("SELECTED_LOGIN_IMAGE", null)
        val selectedMainImage = sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)

        if (isThemeCustomized && selectedMainImage != null) {
            // اگر تم شخصی‌سازی شده و تصویر انتخاب شده است، به MainActivityTheme9 بروید
            val intent = Intent(this, MainActivityTheme9::class.java)
            startActivity(intent)
            finish()
            return
        }

        // بررسی وضعیت لاگین
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val userId = sharedPreferences.getInt("userId", -1)
            if (userId != -1) {
                // اگر کاربر لاگین کرده باشد، به HomeActivityTheme10 هدایت می‌شود
                val intent = Intent(this, HomeActivityTheme10::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } else {
                // اگر کاربر لاگین نکرده باشد، به صفحه لاگین منتقل می‌شود
                startActivity(Intent(this, LoginActivityTheme10::class.java))
                finish()
            }
            return
        }

        // تنظیم دکمه‌های ثبت‌نام و ورود
        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            if (selectedRegisterImage != null) {
                startActivity(Intent(this, RegisterActivityTheme9::class.java))
            } else {
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
