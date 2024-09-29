package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.DrawableCompat

class LoginActivityTheme9 : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var loginButton: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    @SuppressLint("MissingInflatedId", "CutPasteId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login9)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        relativeLayout = findViewById(R.id.relativeLayout) // Root layout

        // بارگذاری و اعمال رنگ پس‌زمینه از SharedPreferences (در صورت وجود)
        val savedColor = sharedPreferences.getString("BACKGROUND_COLOR", null)
        if (savedColor != null) {
            relativeLayout.setBackgroundColor(Color.parseColor(savedColor))
        }
        loginButton = findViewById(R.id.btnLogin)
        val loginButtonColor = sharedPreferences.getString("LOGIN_BUTTON_COLOR", null)
        if (loginButtonColor != null) {
            val drawable = loginButton.background
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(loginButtonColor))
            loginButton.background = wrappedDrawable
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
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val loginImageView = findViewById<ImageView>(R.id.loginImageView)

        // واکشی تصویر انتخاب شده برای رجیستر از SharedPreferences
        val selectedLoginImage = sharedPreferences.getString("SELECTED_LOGIN_IMAGE", null)

        // اگر تصویری برای رجیستر انتخاب شده بود، آن را نمایش می‌دهیم
        if (selectedLoginImage != null) {
            val imageByteArray = Base64.decode(selectedLoginImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            loginImageView.setImageBitmap(bitmap)
        }
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

                        val isHomeCustomized = sharedPreferences.getBoolean("isHomeCustomized", false)
                        val intent = if (isHomeCustomized) {
                            Intent(this, HomeActivityTheme9::class.java)
                        } else {
                            Intent(this, HomeActivityTheme10::class.java)
                        }
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
