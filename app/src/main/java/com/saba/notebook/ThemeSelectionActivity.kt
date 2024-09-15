package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ThemeSelectionActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_selection)

        // دسترسی به SharedPreferences برای ذخیره و دریافت اطلاعات مربوط به تم و کوین‌ها
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

        // تنظیم کلیک لیسنر برای هر تم
        findViewById<LinearLayout>(R.id.theme1).setOnClickListener {
            applyTheme("theme1")
        }

        findViewById<LinearLayout>(R.id.theme2).setOnClickListener {
            applyTheme("theme2")
        }

        findViewById<LinearLayout>(R.id.theme3).setOnClickListener {
            applyTheme("theme3")
        }

        findViewById<LinearLayout>(R.id.theme4).setOnClickListener {
            applyTheme("theme4")
        }

        findViewById<LinearLayout>(R.id.theme5).setOnClickListener {
            applyTheme("theme5")
        }

        findViewById<LinearLayout>(R.id.theme6).setOnClickListener {
            applyTheme("theme6")
        }

        findViewById<LinearLayout>(R.id.theme9).setOnClickListener {
            // Apply theme 10 when theme 9 is selected
            sharedPreferences.edit().putString("SELECTED_THEME", "theme10").apply()

            // Open ThemeCustomizationActivity
            val intent = Intent(this, ThemeCustomizationActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }

    private fun applyTheme(themeName: String) {
        // ذخیره‌ی تم انتخاب شده در SharedPreferences
        sharedPreferences.edit().putString("SELECTED_THEME", themeName).apply()

        // باز کردن اکتیویتی مناسب براساس تم انتخاب شده
        val intent = when (themeName) {
            "theme2" -> Intent(this, HomeActivityTheme2::class.java)
            "theme3" -> Intent(this, HomeActivityTheme3::class.java)
            "theme4" -> Intent(this, HomeActivityTheme4::class.java)
            "theme5" -> Intent(this, HomeActivityTheme5::class.java)
            "theme6" -> Intent(this, HomeActivityTheme6::class.java)
            "theme9", "theme10" -> Intent(this, HomeActivityTheme10::class.java)
            else -> Intent(this, HomeActivityTheme1::class.java)
        }

        // اضافه کردن userId به Intent
        intent.putExtra("USER_ID", userId)

        startActivity(intent)
        finish()
    }
}