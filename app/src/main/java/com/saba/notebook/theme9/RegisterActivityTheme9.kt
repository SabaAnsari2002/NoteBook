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

            if (username.isNotEmpty() && password.isNotEmpty()) {
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
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
