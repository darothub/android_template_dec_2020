package com.peacedude.lassod_tailor_app.ui.resources

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.hide
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.helpers.show
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_resources.*

class ResourcesActivity : AppCompatActivity() {
    private val title by lazy {
        getName()
    }
    private val toolbar by lazy {
        resources_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
    }
    val navController by lazy {
        Navigation.findNavController(this, R.id.resourcesFragment)
    }

    val navListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
           when(destination.id){
               R.id.allVideoFragment -> {
                   toolbar?.show()
                   toolbar?.setNavigationOnClickListener {
                       controller.popBackStack()
                   }
               }
               R.id.resourcesFragment -> toolbar?.show()
               R.id.singleVideoFragment -> toolbar?.hide()
           }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        val activityTitle = resources_activity_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.resources)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        toolbar?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

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