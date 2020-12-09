package com.peacedude.lassod_tailor_app.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.helpers.invisible
import com.peacedude.lassod_tailor_app.helpers.networkMonitor
import com.peacedude.lassod_tailor_app.helpers.show
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.lang.Math.abs
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

abstract class BaseActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelProviderFactori: ViewModelFactory
    val baseDialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }

    val baseCheckConnectionTv by lazy{
        baseDialog.findViewById<TextView>(R.id.loader_layout_tv)
    }
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactori).get(AuthViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseDialog.show()
        baseCheckConnectionTv.show()
        networkMonitor().observe(this, androidx.lifecycle.Observer {
            if (it) {
                baseDialog.dismiss()
                Log.i("Base", "Network On")

            } else {
                baseDialog.show()
                baseCheckConnectionTv.show()
                Log.i("Base", "Network OFF")
            }
        })

    }

    override fun onResume() {
        super.onResume()
    }

}




