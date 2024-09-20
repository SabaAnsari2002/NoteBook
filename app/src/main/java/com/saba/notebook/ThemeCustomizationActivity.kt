package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import android.graphics.Color
import android.widget.Toast

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



        val homeButton = findViewById<LinearLayout>(R.id.home)
        homeButton.setOnClickListener {
            sharedPreferences.edit().apply {
                putBoolean("isHomeCustomized", true)
                apply()
            }

            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("IMAGE_TYPE", "HOME")
            startActivity(intent)
        }

        val addNoteButton = findViewById<LinearLayout>(R.id.add_note)
        addNoteButton.setOnClickListener {
            // ذخیره اطلاعات شخصی‌سازی صفحه افزودن نوت در SharedPreferences
            sharedPreferences.edit().apply {
                putBoolean("isAddNoteCustomized", true) // مشخص کردن اینکه صفحه افزودن نوت شخصی‌سازی شده
                apply()
            }

            // هدایت به صفحه‌ی انتخاب تصویر برای افزودن نوت
            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)  // انتقال userId
            intent.putExtra("IMAGE_TYPE", "ADD_NOTE") // تعیین نوع تصویر انتخابی برای افزودن نوت
            startActivity(intent)
        }

        val backgroundColorButton = findViewById<LinearLayout>(R.id.background_color)
        backgroundColorButton.setOnClickListener {
            showColorPickerDialog()
        }
    }

    private fun showColorPickerDialog() {
        ColorPickerDialog.Builder(this)
            .setTitle("انتخاب رنگ پس‌زمینه")
            .setPositiveButton("تایید", ColorEnvelopeListener { envelope, fromUser ->
                val hexColor = "#" + envelope.hexCode
                saveBackgroundColor(hexColor)
                showColorSelectedMessage(hexColor)
            })
            .setNegativeButton("لغو") { dialogInterface, i -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    private fun saveBackgroundColor(hexColor: String) {
        sharedPreferences.edit().apply {
            putString("BACKGROUND_COLOR", hexColor)
            apply()
        }
    }

    private fun showColorSelectedMessage(hexColor: String) {
        Toast.makeText(this, "رنگ $hexColor انتخاب شد", Toast.LENGTH_SHORT).show()
    }





    override fun onBackPressed() {
        super.onBackPressed()
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isHomeCustomized = sharedPreferences.getBoolean("isHomeCustomized", false)

        if (isHomeCustomized) {
            // If home is customized, launch HomeActivityTheme9
            val intent = Intent(this, HomeActivityTheme9::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("USER_ID", userId)  // Make sure userId is passed correctly
            startActivity(intent)
        } else {
            // If home is not customized, launch HomeActivityTheme10
            val intent = Intent(this, HomeActivityTheme10::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("USER_ID", userId)  // Make sure userId is passed correctly
            startActivity(intent)
        }
        finish()
    }
}