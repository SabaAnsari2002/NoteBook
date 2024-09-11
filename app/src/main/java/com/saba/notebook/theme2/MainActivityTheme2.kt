package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivityTheme2 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startActivity(Intent(this, HomeActivityTheme2::class.java))
            finish()
            return
        }

        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivityTheme2::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivityTheme2::class.java))
        }
    }
}