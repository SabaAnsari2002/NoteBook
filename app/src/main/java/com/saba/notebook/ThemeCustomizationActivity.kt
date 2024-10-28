package com.saba.notebook

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class ThemeCustomizationActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_customization)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // دریافت حالت دارک مود یا لایت مود
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        applyMode(isDarkMode)  // فراخوانی تابع برای اعمال حالت مود

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
            showLoadingMessageOnce("splashScreenButton")
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
            showLoadingMessageOnce("mainButton")
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
            showLoadingMessageOnce("registerButton")
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
            showLoadingMessageOnce("loginButton")
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
            showLoadingMessageOnce("homeButton")
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
            showLoadingMessageOnce("addNoteButton")
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


        val logoutButton = findViewById<LinearLayout>(R.id.logout)
        logoutButton.setOnClickListener {
            showLoadingMessageOnce("logoutButton")
            sharedPreferences.edit().apply {
                putBoolean("isLogoutCustomized", true)
                apply()
            }
            val intent = Intent(this, ButtonImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("BUTTON_TYPE", "LOGOUT")
            startActivity(intent)

        }

        val addNoteButtonIcon = findViewById<LinearLayout>(R.id.addNote)
        addNoteButtonIcon.setOnClickListener {
            showLoadingMessageOnce("addNoteButtonIcon")
            sharedPreferences.edit().apply {
                putBoolean("isAddNoteCustomized", true)
                apply()
            }
            val intent = Intent(this, ButtonImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("BUTTON_TYPE", "ADD_NOTE")
            startActivity(intent)

        }

        val attachButton = findViewById<LinearLayout>(R.id.attach)
        attachButton.setOnClickListener {
            showLoadingMessageOnce("attachButton")
            // Save attach button customization
            sharedPreferences.edit().apply {
                putBoolean("isAttachCustomized", true)
                apply()
            }
            val intent = Intent(this, ButtonImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("BUTTON_TYPE", "ATTACH")
            startActivity(intent)
        }

        val deleteButton = findViewById<LinearLayout>(R.id.delete)
        deleteButton.setOnClickListener {
            showLoadingMessageOnce("deleteButton")
            // Save delete button customization
            sharedPreferences.edit().apply {
                putBoolean("isDeleteCustomized", true)
                apply()
            }
            val intent = Intent(this, ButtonImagesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("BUTTON_TYPE", "DELETE")
            startActivity(intent)
        }

        val backgroundColorButton = findViewById<LinearLayout>(R.id.background_color)
        backgroundColorButton.setOnClickListener {
            showColorPickerDialog("انتخاب رنگ پس‌ زمینه", "BACKGROUND_COLOR")
        }
        val saveButtonColorButton = findViewById<LinearLayout>(R.id.save_button)
        saveButtonColorButton.setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه ذخیره", "SAVE_BUTTON_COLOR")
        }

        val registerColorButton = findViewById<LinearLayout>(R.id.register_button)
        registerColorButton.setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه ثبت نام", "REGISTER_BUTTON_COLOR")
        }

        val loginColorButton = findViewById<LinearLayout>(R.id.login_button)
        loginColorButton.setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه ورود", "LOGIN_BUTTON_COLOR")
        }

        val editTextButton = findViewById<LinearLayout>(R.id.edit_text_button)
        editTextButton.setOnClickListener {
            showColorPickerDialog("انتخاب رنگ دکمه یوزر و پسورد", "EDIT_TEXT_BUTTON_COLOR")
        }

        val textColor = findViewById<LinearLayout>(R.id.text_color)
        textColor.setOnClickListener {
            showColorPickerDialog("انتخاب رنگ متن", "TEXT_COLOR")
        }

    }
    private fun showLoadingMessageOnce(buttonKey: String) {
        val hasShownMessage = sharedPreferences.getBoolean(buttonKey, false)
        if (!hasShownMessage) {
            Toast.makeText(this, "لطفا صبر کنید تا دیتا بارگذاری شود.", Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().putBoolean(buttonKey, true).apply()
        }
    }
    private fun applyMode(isDarkMode: Boolean) {
        val mainLayout = findViewById<RelativeLayout>(R.id.mainLayout)
        val themeSelectionText = findViewById<TextView>(R.id.theme_selection_text)
        val splashScreenText = findViewById<TextView>(R.id.splash_screen_text)
        val mainText = findViewById<TextView>(R.id.main_text)
        val registerText = findViewById<TextView>(R.id.register_text)
        val loginText = findViewById<TextView>(R.id.login_text)
        val homeText = findViewById<TextView>(R.id.home_text)
        val addNoteText = findViewById<TextView>(R.id.add_note_text)
        val backgroundColorText = findViewById<TextView>(R.id.background_color_text)
        val logoutText = findViewById<TextView>(R.id.logout_text)
        val addnoteText = findViewById<TextView>(R.id.addNote_text)
        val deleteText = findViewById<TextView>(R.id.delete_text)
        val attachText = findViewById<TextView>(R.id.attach_text)
        val saveButtonText = findViewById<TextView>(R.id.save_button_text)
        val registerButtonText = findViewById<TextView>(R.id.register_button_text)
        val loginButtonText = findViewById<TextView>(R.id.login_button_text)
        val editTextButtonText = findViewById<TextView>(R.id.edit_text_button_text)
        val textColorText = findViewById<TextView>(R.id.text_color_text)


        if (isDarkMode) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
            themeSelectionText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            splashScreenText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            mainText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            registerText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            loginText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            homeText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            addNoteText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            backgroundColorText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            logoutText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            addnoteText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            deleteText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            attachText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            saveButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            registerButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            loginButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            editTextButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            textColorText.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        } else {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
            themeSelectionText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            splashScreenText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            mainText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            registerText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            loginText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            homeText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            addNoteText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            backgroundColorText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            logoutText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            addnoteText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            deleteText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            attachText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            saveButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            registerButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            loginButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            editTextButtonText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            textColorText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }
    }

        private fun showColorPickerDialog(
        title: String,
        preferenceKey: String,
        isDarkMode: Boolean = sharedPreferences.getBoolean("DARK_MODE", false)
    ) {
        val dialogBuilder = ColorPickerDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton("تایید", ColorEnvelopeListener { envelope, _ ->
                val hexColor = "#" + envelope.hexCode
                saveColor(preferenceKey, hexColor)
                showColorSelectedMessage(hexColor)
            })
            .setNegativeButton("لغو") { dialogInterface, _ -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)

        val dialog = dialogBuilder.create()
        if (isDarkMode) {
            dialog.setOnShowListener {
                dialog.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)?.setTextColor(Color.WHITE)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.WHITE)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
            }
        }

        dialog.show()
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