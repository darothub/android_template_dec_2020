package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.peacedude.lassod_tailor_app.R

class SplashScreenActivity : AppCompatActivity() {
    val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        handler.postDelayed ({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)

    }
}