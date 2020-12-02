package com.peacedude.lassod_tailor_app.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buildVersion
import com.peacedude.lassod_tailor_app.helpers.networkMonitor
import com.peacedude.lassod_tailor_app.helpers.show
import com.peacedude.lassod_tailor_app.ui.customer.CustomerActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    val topAnimation by lazy{
        AnimationUtils.loadAnimation(this, R.anim.top_animation)
    }
    val splashDialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }
    val splashCheckConnectionTv by lazy{
        splashDialog.findViewById<TextView>(R.id.loader_layout_tv)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        app_logo.animation = topAnimation
        val netWorkLiveData = networkMonitor()
//        netWorkLiveData.observe(this, androidx.lifecycle.Observer {
//            if (it) {
//                splashDialog.dismiss()
//                Log.i("Base", "Network On")
//
//            } else {
//                splashDialog.show()
//                splashCheckConnectionTv.show()
//
//                Log.i("Base", "Network OFF")
//            }
//        })


    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed ({
            startActivity(Intent(this, CustomerActivity::class.java))
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