package com.saba.notebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class RegisterActivityTheme3 : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register3)

        dbHelper = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // ولیدیشن یوزرنیم و پسورد
            val usernameValidationResult = validateUsername(username)
            val passwordValidationResult = validatePassword(password)

            if (usernameValidationResult.isEmpty() && passwordValidationResult.isEmpty()) {
                // اگر همه شرایط صحیح بود، یوزر ثبت شود
                val result = dbHelper.addUser(username, password)
                if (result != -1L) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivityTheme3::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "This username is already taken. Please choose another username.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // نمایش پیام‌های خطا
                val errorMessage = usernameValidationResult + passwordValidationResult
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // تابع ولیدیشن یوزرنیم
    private fun validateUsername(username: String): String {
        val errors = StringBuilder()

        if (username.length < 8) {
            errors.append("Username must be at least 8 characters long.\n")
        }
        if (!username.any { it.isUpperCase() }) {
            errors.append("Username must contain at least one uppercase letter.\n")
        }
        if (!username.any { it.isLowerCase() }) {
            errors.append("Username must contain at least one lowercase letter.\n")
        }
        if (!username.any { it.isDigit() }) {
            errors.append("Username must contain at least one digit.\n")
        }

        return errors.toString()
    }

    // تابع ولیدیشن پسورد
    private fun validatePassword(password: String): String {
        val errors = StringBuilder()

        if (password.length < 8) {
            errors.append("Password must be at least 8 characters long.\n")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.append("Password must contain at least one uppercase letter.\n")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.append("Password must contain at least one lowercase letter.\n")
        }
        if (!password.any { it.isDigit() }) {
            errors.append("Password must contain at least one digit.\n")
        }

        return errors.toString()
    }
}
