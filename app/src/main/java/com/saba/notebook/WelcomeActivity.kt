package com.saba.notebook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class WelcomeActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // Clear login state
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}