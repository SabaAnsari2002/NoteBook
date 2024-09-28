package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.DrawableCompat

class MainActivityTheme9 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        registerButton = findViewById(R.id.btnRegister)
        loginButton = findViewById(R.id.btnLogin)

        val registerButtonColor = sharedPreferences.getString("REGISTER_BUTTON_COLOR", null)
        val loginButtonColor = sharedPreferences.getString("LOGIN_BUTTON_COLOR", null)

        if (registerButtonColor != null) {
            val drawable = registerButton.background
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(registerButtonColor))
            registerButton.background = wrappedDrawable
        }
        if (loginButtonColor != null) {
            val drawable = loginButton.background
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(loginButtonColor))
            loginButton.background = wrappedDrawable
        }
        initializeViews()
        setupSharedPreferences()
        applyBackgroundColor()
        handleCustomization()
        checkLoginStatus()
        setupButtons()
    }

    private fun initializeViews() {
        relativeLayout = findViewById(R.id.relativeLayout)
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
    }

    private fun applyBackgroundColor() {
        val savedColor = sharedPreferences.getString("BACKGROUND_COLOR", null)
        savedColor?.let {
            relativeLayout.setBackgroundColor(Color.parseColor(it))
        }
    }

    private fun handleCustomization() {
        val isThemeCustomized = sharedPreferences.getBoolean("isTheme9Customized", false)
        val selectedMainImage = sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)

        if (isThemeCustomized && selectedMainImage != null) {
            val imageByteArray = android.util.Base64.decode(selectedMainImage, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            findViewById<ImageView>(R.id.mainImageView).setImageBitmap(bitmap)
        }
    }

    private fun checkLoginStatus() {
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val userId = sharedPreferences.getInt("userId", -1)
            if (userId != -1) {
                navigateToHome(userId)
            } else {
                startActivity(Intent(this, LoginActivityTheme9::class.java))
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