package com.peacedude.lassod_tailor_app.ui.customer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_client.*
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_resources.*

class CustomerActivity : BaseActivity() {
    val title: String by lazy {
        getName()
    }

    private val toolbar by lazy {
        customer_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
    }
    val navController by lazy {
        Navigation.findNavController(this, R.id.customer_activity_fragment)
    }

    val navListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.searchFragment -> {
                    customer_activity_appbar.hide()
                }
                R.id.reviewFragment -> {
                    customer_activity_appbar.show()
                    toolbar.setNavigationOnClickListener {
                        controller.popBackStack()
                    }
                }
                R.id.singleFashionistaFragment -> {
                    customer_activity_appbar.show()
                    toolbar.setNavigationOnClickListener {
                        controller.popBackStack()
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        val activityTitle = customer_activity_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.review)
    }

    override fun onResume() {
        super.onResume()
        i(title, "onResume")
        navController.addOnDestinationChangedListener(navListener)
    }
    override fun onStart() {
        super.onStart()
        i(title, "OnStart")
    }

    override fun onPause() {
        super.onPause()
        i(title, "Onpause")
        navController.removeOnDestinationChangedListener(navListener)
    }
}
