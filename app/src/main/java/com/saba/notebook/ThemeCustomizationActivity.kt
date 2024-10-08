package com.saba.notebook

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.*

class ThemeCustomizationActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1
    private val ioScope = CoroutineScope(Dispatchers.IO) // برای اجرای عملیات در رشته پس‌زمینه

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_customization)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        ioScope.launch {
            userId = getUserIdFromIntentOrPrefs()
            if (userId == -1) {
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@ThemeCustomizationActivity, LoginActivityTheme1::class.java))
                    finish()
                }
            }
        }

        findViewById<LinearLayout>(R.id.splash_screen).setOnClickListener {
            showLoadingDialog()

            handleCustomization("SPLASH")
        }

        findViewById<LinearLayout>(R.id.main).setOnClickListener {
            showLoadingDialog()

            handleCustomization("MAIN", "your_image_data_here") // Replace with actual image data
        }

        findViewById<LinearLayout>(R.id.register).setOnClickListener {
            showLoadingDialog()

            handleCustomization("REGISTER", "your_register_image_data_here")
        }

        findViewById<LinearLayout>(R.id.login).setOnClickListener {
            showLoadingDialog()

            handleCustomization("LOGIN", "your_login_image_data_here")
        }

        findViewById<LinearLayout>(R.id.home).setOnClickListener {
            showLoadingDialog()

            handleCustomization("HOME")
        }

        findViewById<LinearLayout>(R.id.add_note).setOnClickListener {
            showLoadingDialog()

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
        findViewById<LinearLayout>(R.id.text_color).setOnClickListener {
            showColorPickerDialog("انتخاب رنگ متن", "TEXT_COLOR")
        }

    }
    private fun showLoadingDialog() {
        val loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("Loading, please wait...")
        loadingDialog.setCancelable(false)
        loadingDialog.show()

        // Simulate delay for loading (you can replace this with actual loading process)
        Handler().postDelayed({
            loadingDialog.dismiss()
            // Navigate to the activity once loading is done
        }, 5000) // 2-second delay, replace with real data loading time
    }

    private suspend fun getUserIdFromIntentOrPrefs(): Int {
        return withContext(Dispatchers.IO) {
            intent.getIntExtra("USER_ID", sharedPreferences.getInt("userId", -1))
        }
    }


    private fun handleCustomization(imageType: String, imageData: String? = null) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Save data in the background
            sharedPreferences.edit().apply {
                putBoolean("isTheme9Customized", true)
                imageData?.let {
                    putString("SELECTED_${imageType}_IMAGE", it)
                }
                apply()
            }
            withContext(Dispatchers.Main) {
                // Back to the main thread to update the UI or start activity
                startImagesActivity(imageType)
            }
        }
    }

    private fun handleButtonCustomization(buttonType: String) {
        ioScope.launch {
            sharedPreferences.edit().apply {
                putBoolean("is${buttonType}Customized", true)
                apply()
            }
            withContext(Dispatchers.Main) {
                startButtonImagesActivity(buttonType)
            }
        }
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
                ioScope.launch {
                    saveColor(preferenceKey, hexColor)
                    withContext(Dispatchers.Main) {
                        showColorSelectedMessage(hexColor)
                    }
                }
            })
            .setNegativeButton("لغو") { dialogInterface, _ -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    private suspend fun saveColor(preferenceKey: String, hexColor: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().apply {
                putString(preferenceKey, hexColor)
                apply()
            }
        }
    }

    private fun showColorSelectedMessage(hexColor: String) {
        Toast.makeText(this, "رنگ $hexColor انتخاب شد", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ioScope.launch {
            val isHomeCustomized = sharedPreferences.getBoolean("isHomeCustomized", false)
            withContext(Dispatchers.Main) {
                val intent = if (isHomeCustomized) {
                    Intent(this@ThemeCustomizationActivity, HomeActivityTheme9::class.java)
                } else {
                    Intent(this@ThemeCustomizationActivity, HomeActivityTheme10::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            }
        }
    }
}
