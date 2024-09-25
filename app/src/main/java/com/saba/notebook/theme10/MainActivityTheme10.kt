package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivityTheme10 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main10)

        initializeSharedPreferences()
        checkThemeCustomization()
        checkLoginStatus()
        setupButtons()
    }

    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
    }

    private fun checkThemeCustomization() {
        val isThemeCustomized = sharedPreferences.getBoolean("isTheme9Customized", false)
        val selectedMainImage = sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)

        if (isThemeCustomized && selectedMainImage != null) {
            startActivity(Intent(this, MainActivityTheme9::class.java))
            finish()
        }
    }

    private fun checkLoginStatus() {
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val userId = sharedPreferences.getInt("userId", -1)
            if (userId != -1) {
                navigateToHome(userId)
            } else {
                startActivity(Intent(this, LoginActivityTheme10::class.java))
            }
            finish()
        }
    }

    private fun navigateToHome(userId: Int) {
        val isHomeCustomized = sharedPreferences.getBoolean("isHomeCustomized", false)
        val intent = if (isHomeCustomized) {
            Intent(this, HomeActivityTheme9::class.java)
        } else {
            Intent(this, HomeActivityTheme10::class.java)
        }
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun setupButtons() {
        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            val selectedRegisterImage = sharedPreferences.getString("SELECTED_REGISTER_IMAGE", null)
            val intent = if (selectedRegisterImage != null) {
                Intent(this, RegisterActivityTheme9::class.java)
            } else {
                Intent(this, RegisterActivityTheme10::class.java)
            }
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val selectedLoginImage = sharedPreferences.getString("SELECTED_LOGIN_IMAGE", null)
            val intent = if (selectedLoginImage != null) {
                Intent(this, LoginActivityTheme9::class.java)
            } else {
                Intent(this, LoginActivityTheme10::class.java)
            }
            startActivity(intent)
        }
    }
}