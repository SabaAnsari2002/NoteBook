package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ThemeSelectionActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_selection)

        // دسترسی به SharedPreferences برای ذخیره و دریافت اطلاعات مربوط به تم و کوین‌ها
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)


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
            else -> Intent(this, HomeActivityTheme1::class.java)
        }

        startActivity(intent)
        finish()
    }


}
