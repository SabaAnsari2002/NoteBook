package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import android.widget.Toast

class ThemeCustomizationActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_customization)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        userId = intent.getIntExtra("USER_ID", sharedPreferences.getInt("userId", -1))

        if (userId == -1) {
            startActivity(Intent(this, LoginActivityTheme1::class.java))
            finish()
            return
        }

        findViewById<LinearLayout>(R.id.splash_screen).setOnClickListener {
            handleCustomization("SPLASH")
        }

        findViewById<LinearLayout>(R.id.main).setOnClickListener {
            handleCustomization("MAIN", "your_image_data_here") // Replace with actual image data
        }

        findViewById<LinearLayout>(R.id.register).setOnClickListener {
            handleCustomization("REGISTER", "your_register_image_data_here")
        }

        findViewById<LinearLayout>(R.id.login).setOnClickListener {
            handleCustomization("LOGIN", "your_login_image_data_here")
        }

        findViewById<LinearLayout>(R.id.home).setOnClickListener {
            handleCustomization("HOME")
        }

        findViewById<LinearLayout>(R.id.add_note).setOnClickListener {
            handleCustomization("ADD_NOTE")
        }

        findViewById<LinearLayout>(R.id.logout).setOnClickListener {
            handleButtonCustomization("LOGOUT")
        }

        findViewById<LinearLayout>(R.id.addNote).setOnClickListener {
            handleButtonCustomization("ADD_NOTE")
        }

        findViewById<LinearLayout>(R.id.attach).setOnClickListener {
            handleButtonCustomization("ATTACH")
        }

        findViewById<LinearLayout>(R.id.delete).setOnClickListener {
            handleButtonCustomization("DELETE")
        }

        findViewById<LinearLayout>(R.id.background_color).setOnClickListener {
            showColorPickerDialog("انتخاب رنگ پس‌زمینه", "BACKGROUND_COLOR")
        }

        findViewById<LinearLayout>(R.id.save_button).setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه ذخیره", "SAVE_BUTTON_COLOR")
        }

        findViewById<LinearLayout>(R.id.register_button).setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه ثبت نام", "REGISTER_BUTTON_COLOR")
        }

        findViewById<LinearLayout>(R.id.login_button).setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه ورود", "LOGIN_BUTTON_COLOR")
        }

        findViewById<LinearLayout>(R.id.edit_text_button).setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه یوزر و پسورد", "EDIT_TEXT_BUTTON_COLOR")
        }
    }

    private fun handleCustomization(imageType: String, imageData: String? = null) {
        sharedPreferences.edit().apply {
            putBoolean("isTheme9Customized", true)
            imageData?.let {
                putString("SELECTED_${imageType}_IMAGE", it)
            }
            apply()
        }
        startImagesActivity(imageType)
    }

    private fun handleButtonCustomization(buttonType: String) {
        sharedPreferences.edit().apply {
            putBoolean("is${buttonType}Customized", true)
            apply()
        }
        startButtonImagesActivity(buttonType)
    }

    private fun startImagesActivity(imageType: String) {
        val intent = Intent(this, ImagesActivity::class.java)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("IMAGE_TYPE", imageType)
        startActivity(intent)
    }

    private fun startButtonImagesActivity(buttonType: String) {
        val intent = Intent(this, ButtonImagesActivity::class.java)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("BUTTON_TYPE", buttonType)
        startActivity(intent)
    }

    private fun showColorPickerDialog(title: String, preferenceKey: String) {
        ColorPickerDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton("تایید", ColorEnvelopeListener { envelope, fromUser ->
                val hexColor = "#" + envelope.hexCode
                saveColor(preferenceKey, hexColor)
                showColorSelectedMessage(hexColor)
            })
            .setNegativeButton("لغو") { dialogInterface, _ -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    private fun saveColor(preferenceKey: String, hexColor: String) {
        sharedPreferences.edit().apply {
            putString(preferenceKey, hexColor)
            apply()
        }
    }

    private fun showColorSelectedMessage(hexColor: String) {
        Toast.makeText(this, "رنگ $hexColor انتخاب شد", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val isHomeCustomized = sharedPreferences.getBoolean("isHomeCustomized", false)
        val intent = if (isHomeCustomized) {
            Intent(this, HomeActivityTheme9::class.java)
        } else {
            Intent(this, HomeActivityTheme10::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }
}
