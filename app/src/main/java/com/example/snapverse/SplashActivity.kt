package com.example.snapverse

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay to show the splash screen for 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000)
    }

    private fun checkLoginStatus() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("SnapVersePrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
