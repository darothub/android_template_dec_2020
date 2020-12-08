package com.peacedude.lassod_tailor_app.ui.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.hide
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.helpers.show
import com.peacedude.lassod_tailor_app.ui.BaseActivity
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
            when(destination.id){
                R.id.searchFragment -> {
                    customer_activity_appbar.hide()
                }
                R.id.reviewFragment ->{
                    customer_activity_appbar.show()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
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