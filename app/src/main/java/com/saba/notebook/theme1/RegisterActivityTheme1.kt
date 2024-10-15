package com.saba.notebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class RegisterActivityTheme1 : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        dbHelper = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // اعتبارسنجی نام کاربری و رمز عبور
            val usernameValidationResult = validateUsername(username)
            val passwordValidationResult = validatePassword(password)

            if (usernameValidationResult.isEmpty() && passwordValidationResult.isEmpty()) {
                // اگر اعتبارسنجی موفق بود، کاربر ثبت‌نام می‌شود
                val result = dbHelper.addUser(username, password)
                if (result != -1L) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivityTheme1::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "This username is already taken. Please choose another username.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // نمایش پیام‌های اعتبارسنجی
                val validationMessage = usernameValidationResult + passwordValidationResult
                Toast.makeText(this, validationMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // تابع برای اعتبارسنجی نام کاربری
    private fun validateUsername(username: String): String {
        val errors = mutableListOf<String>()

        if (username.length < 8) {
            errors.add("Username must be at least 8 characters long.")
        }
        if (!username.any { it.isUpperCase() }) {
            errors.add("Username must contain at least one uppercase letter.")
        }
        if (!username.any { it.isLowerCase() }) {
            errors.add("Username must contain at least one lowercase letter.")
        }
        if (!username.any { it.isDigit() }) {
            errors.add("Username must contain at least one digit.")
        }

        return if (errors.isEmpty()) "" else errors.joinToString("\n")
    }

    // تابع برای اعتبارسنجی رمز عبور
    private fun validatePassword(password: String): String {
        val errors = mutableListOf<String>()

        if (password.length < 8) {
            errors.add("Password must be at least 8 characters long.")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("Password must contain at least one uppercase letter.")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("Password must contain at least one lowercase letter.")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("Password must contain at least one digit.")
        }

        return if (errors.isEmpty()) "" else errors.joinToString("\n")
    }
}
