package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buildVersion
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    val topAnimation by lazy{
        AnimationUtils.loadAnimation(this, R.anim.top_animation)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        app_logo.animation = topAnimation


    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed ({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        if (hasFocus) hideSystemUI()
    }


    private fun hideSystemUI() {
        buildVersion({
//            window.setDecorFitsSystemWindows(false)
        },{
            // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        })


    }
}