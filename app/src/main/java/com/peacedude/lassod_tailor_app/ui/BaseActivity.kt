package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
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

//        networkMonitor().observe(this, Observer {
//            if (it) {
//                baseDialog.dismiss()
//                baseCheckConnectionTv.hide()
//            } else {
//                baseDialog.show()
//                baseCheckConnectionTv.show()
//            }
//        })
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }


}




