package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ThemeSelectionActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1
    private lateinit var mainLayout: RelativeLayout
    private lateinit var darkModeButton: ImageView
    private lateinit var themeSelectionText: TextView
    private lateinit var themeGrid: GridLayout

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

        // دریافت عناصر از XML
        mainLayout = findViewById(R.id.mainLayout) // لایه اصلی
        darkModeButton = findViewById(R.id.darkModeButton) // دکمه دارک/لایت مود
        themeSelectionText = findViewById(R.id.theme_selection_text) // متن انتخاب تم
        themeGrid = findViewById(R.id.theme_grid) // گرید تم‌ها

        // بررسی حالت دارک مود یا لایت مود و اعمال آن
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        applyMode(isDarkMode)

        // تنظیم عملکرد دکمه دارک/لایت مود
        darkModeButton.setOnClickListener {
            val currentMode = sharedPreferences.getBoolean("DARK_MODE", false)
            val newMode = !currentMode
            sharedPreferences.edit().putBoolean("DARK_MODE", newMode).apply()
            applyMode(newMode)
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

        findViewById<LinearLayout>(R.id.theme7).setOnClickListener {
            applyTheme("theme7")
        }

        findViewById<LinearLayout>(R.id.theme8).setOnClickListener {
            applyTheme("theme8")
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

    // تابع برای اعمال حالت دارک یا لایت مود
    private fun applyMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
            themeSelectionText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            updateThemeGridTextColor(android.R.color.white)
        } else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            themeSelectionText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            updateThemeGridTextColor(android.R.color.black)
        }
    }

    // تابع برای اعمال رنگ متن در گرید تم‌ها

    private fun updateThemeGridTextColor(colorResId: Int) {
        for (i in 0 until themeGrid.childCount) {
            val child = themeGrid.getChildAt(i)
            if (child is LinearLayout) {
                // Use the actual IDs of the TextViews, e.g.:
                val textView = when (child.id) {
                    R.id.theme1 -> child.findViewById<TextView>(R.id.textView1)
                    R.id.theme2 -> child.findViewById<TextView>(R.id.textView2)
                    R.id.theme3 -> child.findViewById<TextView>(R.id.textView3)
                    R.id.theme4 -> child.findViewById<TextView>(R.id.textView4)
                    R.id.theme5 -> child.findViewById<TextView>(R.id.textView5)
                    R.id.theme6 -> child.findViewById<TextView>(R.id.textView6)
                    R.id.theme7 -> child.findViewById<TextView>(R.id.textView7)
                    R.id.theme8 -> child.findViewById<TextView>(R.id.textView8)
                    R.id.theme9 -> child.findViewById<TextView>(R.id.textView9)
                    else -> null
                }
                textView?.setTextColor(ContextCompat.getColor(this, colorResId))
            }
        }
    }

    private fun applyTheme(themeName: String) {
        sharedPreferences.edit().putString("SELECTED_THEME", themeName).apply()

        val intent = when (themeName) {
            "theme2" -> Intent(this, HomeActivityTheme2::class.java)
            "theme3" -> Intent(this, HomeActivityTheme3::class.java)
            "theme4" -> Intent(this, HomeActivityTheme4::class.java)
            "theme5" -> Intent(this, HomeActivityTheme5::class.java)
            "theme6" -> Intent(this, HomeActivityTheme6::class.java)
            "theme7" -> Intent(this, HomeActivityTheme7::class.java)
            "theme8" -> Intent(this, HomeActivityTheme8::class.java)
            "theme9", "theme10" -> Intent(this, HomeActivityTheme10::class.java)
            else -> Intent(this, HomeActivityTheme1::class.java)
        }

        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }
}
