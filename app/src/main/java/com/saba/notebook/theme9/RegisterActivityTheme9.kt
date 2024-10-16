package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.graphics.drawable.DrawableCompat

class RegisterActivityTheme9 : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var registerButton: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register9)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        relativeLayout = findViewById(R.id.relativeLayout) // Root layout

        // بارگذاری و اعمال رنگ پس‌زمینه از SharedPreferences (در صورت وجود)
        val savedColor = sharedPreferences.getString("BACKGROUND_COLOR", null)
        if (savedColor != null) {
            relativeLayout.setBackgroundColor(Color.parseColor(savedColor))
        }
        registerButton = findViewById(R.id.btnRegister)
        val registerButtonColor = sharedPreferences.getString("REGISTER_BUTTON_COLOR", null)
        if (registerButtonColor != null) {
            val drawable = registerButton.background
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(registerButtonColor))
            registerButton.background = wrappedDrawable
        }
        editTextUsername = findViewById(R.id.etUsername)

        val editTextUsernameColor = sharedPreferences.getString("EDIT_TEXT_BUTTON_COLOR", null)
        if (editTextUsernameColor != null) {
            val drawable = editTextUsername.background
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(editTextUsernameColor))
            editTextUsername.background = wrappedDrawable
        }

        editTextPassword = findViewById(R.id.etPassword)

        val editTextPasswordColor = sharedPreferences.getString("EDIT_TEXT_BUTTON_COLOR", null)
        if (editTextPasswordColor != null) {
            val drawable = editTextPassword.background
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(editTextPasswordColor))
            editTextPassword.background = wrappedDrawable
        }

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val registerImageView = findViewById<ImageView>(R.id.registerImageView)

        // بارگذاری و نمایش تصویر انتخاب‌شده برای ثبت‌نام از SharedPreferences (در صورت وجود)
        val selectedRegisterImage = sharedPreferences.getString("SELECTED_REGISTER_IMAGE", null)
        if (selectedRegisterImage != null) {
            val imageByteArray = Base64.decode(selectedRegisterImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            registerImageView.setImageBitmap(bitmap)
        }

        // رویداد کلیک برای دکمه ثبت‌نام
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // فراخوانی توابع ولیدیشن
            val usernameValidationResult = validateUsername(username)
            val passwordValidationResult = validatePassword(password)

            if (usernameValidationResult == "valid" && passwordValidationResult == "valid") {
                // ثبت‌نام در دیتابیس
                val result = dbHelper.addUser(username, password)
                if (result != -1L) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    val isLoginCustomized = sharedPreferences.getBoolean("isLoginCustomized", false)

                    // هدایت به صفحه ورود مناسب با توجه به سفارشی‌سازی
                    if (isLoginCustomized) {
                        startActivity(Intent(this, LoginActivityTheme9::class.java))
                    } else {
                        startActivity(Intent(this, LoginActivityTheme10::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this, "This username is already taken. Please choose another username.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // نمایش پیام‌های خطا در صورت عدم موفقیت ولیدیشن
                if (usernameValidationResult != "valid") {
                    Toast.makeText(this, usernameValidationResult, Toast.LENGTH_LONG).show()
                }
                if (passwordValidationResult != "valid") {
                    Toast.makeText(this, passwordValidationResult, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // تابع ولیدیشن برای یوزرنیم
    private fun validateUsername(username: String): String {
        val errors = mutableListOf<String>()

        // چک طول یوزرنیم
        if (username.length < 8) {
            errors.add("Username must be at least 8 characters long.")
        }
        // چک وجود حداقل یک حرف بزرگ
        if (!username.any { it.isUpperCase() }) {
            errors.add("Username must contain at least one uppercase letter.")
        }
        // چک وجود حداقل یک حرف کوچک
        if (!username.any { it.isLowerCase() }) {
            errors.add("Username must contain at least one lowercase letter.")
        }
        // چک وجود حداقل یک رقم
        if (!username.any { it.isDigit() }) {
            errors.add("Username must contain at least one digit.")
        }

        return if (errors.isEmpty()) "valid" else errors.joinToString("\n")
    }

    // تابع ولیدیشن برای پسورد
    private fun validatePassword(password: String): String {
        val errors = mutableListOf<String>()

        // چک طول پسورد
        if (password.length < 8) {
            errors.add("Password must be at least 8 characters long.")
        }
        // چک وجود حداقل یک حرف بزرگ
        if (!password.any { it.isUpperCase() }) {
            errors.add("Password must contain at least one uppercase letter.")
        }
        // چک وجود حداقل یک حرف کوچک
        if (!password.any { it.isLowerCase() }) {
            errors.add("Password must contain at least one lowercase letter.")
        }
        // چک وجود حداقل یک رقم
        if (!password.any { it.isDigit() }) {
            errors.add("Password must contain at least one digit.")
        }

        return if (errors.isEmpty()) "valid" else errors.joinToString("\n")
    }
}
