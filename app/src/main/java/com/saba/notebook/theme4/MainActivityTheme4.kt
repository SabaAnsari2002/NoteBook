package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivityTheme4 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val userId = sharedPreferences.getInt("userId", -1)
            if (userId != -1) {
                val intent = Intent(this, HomeActivityTheme4::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } else {
                // اگر userId پیدا نشد، به صفحه ورود هدایت کنید
                startActivity(Intent(this, LoginActivityTheme4::class.java))
                finish()
            }
            return
        }

        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivityTheme4::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivityTheme4::class.java))
        }
    }
}