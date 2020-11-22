package com.peacedude.lassod_tailor_app.ui.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.changeStatusBarColor
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : BaseActivity() {
    private val navController by lazy {
        Navigation.findNavController(this, R.id.subscription_fragment)
    }
    val toolbar by lazy {
        subscription_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
    }
    val listener = NavController.OnDestinationChangedListener{controller, destination, bundle->
        when(destination.id){
            R.id.addCardFragment ->{
                title = getString(R.string.add_card)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
        setContentView(R.layout.activity_subscription)

        subscription_activity_appbar.elevation = 10F
        val activityTitle = subscription_activity_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.subscription)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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