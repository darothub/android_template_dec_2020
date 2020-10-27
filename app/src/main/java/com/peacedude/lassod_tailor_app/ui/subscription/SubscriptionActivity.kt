package com.peacedude.lassod_tailor_app.ui.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peacedude.lassod_tailor_app.R
import kotlinx.android.synthetic.main.activity_client.*
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : AppCompatActivity() {
    private val navController by lazy {
        Navigation.findNavController(this, R.id.subscription_fragment)
    }
    val toolbar by lazy {
        (subscription_activity_tb as Toolbar?)
    }
    val listener = NavController.OnDestinationChangedListener{controller, destination, bundle->
        when(destination.id){
            R.id.subscription_fragment ->{
                toolbar?.title = "Hello"
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }
}