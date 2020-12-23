package com.peacedude.lassod_tailor_app.ui.resources

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_resources.*
import javax.inject.Inject

class ResourcesActivity : BaseActivity() {
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
                   resources_activity_appbar.show()
                   toolbar?.setNavigationOnClickListener {
                       controller.popBackStack()
                   }
               }
               R.id.allArticlesFragment -> {
                   toolbar?.show()
                   resources_activity_appbar.show()
                   toolbar?.setNavigationOnClickListener {
                       controller.popBackStack()
                   }
               }
               R.id.resourcesFragment -> {
                   resources_activity_appbar.show()
                   toolbar?.show()
                   toolbar?.setNavigationOnClickListener {
                       finish()
                   }
               }
               R.id.singleVideoFragment -> {
                   resources_activity_appbar.hide()
               }
               R.id.singleArticleFragment ->{
                   resources_activity_appbar.hide()
               }
           }
        }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        val activityTitle = resources_activity_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.resources)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        toolbar?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))


//        networkMonitor().observe(this, Observer {
//            if (it) {
//                resourcesFragment.view?.show()
//
//            } else {
//                resourcesFragment.view?.invisible()
//            }
//        })

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