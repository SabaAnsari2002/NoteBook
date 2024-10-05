package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class LoginActivityTheme8 : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login8)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (dbHelper.getUser(username, password)) {
                    val userId = dbHelper.getUserId(username)
                    if (userId != -1) {
                        // ذخیره وضعیت ورود و شناسه کاربر
                        sharedPreferences.edit()
                            .putBoolean("isLoggedIn", true)
                            .putInt("userId", userId)
                            .apply()

                        val intent = Intent(this, HomeActivityTheme8::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "خطا در بازیابی شناسه کاربر", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "نام کاربری یا رمز عبور نامعتبر است", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "لطفاً تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
            }
        }
    }
}