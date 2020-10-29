package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.profile.ProfileActivity
import com.peacedude.lassod_tailor_app.ui.resources.ResourcesActivity
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import javax.inject.Inject

class DashboardActivity : BaseActivity() {
    private val navController by lazy {
        Navigation.findNavController(this, R.id.dashboard_fragment)
    }
    val title: String by lazy {
        getName()
    }
    val profileName by lazy{
        profile_drawer_view.findViewById<TextView>(R.id.profile_name)
    }
//    Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }

    private val header by lazy {
        authViewModel.header
    }
    private val menuIcon: ImageView? by lazy{
        profile_header.findViewById<ImageView>(R.id.menu_icon)
    }
    private val greeting: TextView by lazy{
        profile_header.findViewById<TextView>(R.id.hi_user_name)
    }

    private lateinit var editBtn:Button
    private lateinit var logoutText:TextView
    private lateinit var logoutImage:ImageView
    private lateinit var clientImage:ImageView
    private lateinit var clientText:TextView
    private lateinit var resourcesTv:TextView
    private lateinit var resourcesIv:ImageView
    private lateinit var subscriptionTv:TextView
    private lateinit var subscriptionIv:ImageView
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        changeStatusBarColor(R.color.colorWhite)
        i(title, "Oncreate")
        bottomNav.setupWithNavController(navController)

//        setBottomNavController()

        profile_header.show()

        menuIcon?.setOnClickListener {
            drawer_layout.openDrawer(profile_drawer_view, true)
        }

        buttonTransactions({
            editBtn = profile_drawer_view.findViewById(R.id.edit_profile_btn)
            logoutText = profile_drawer_view.findViewById(R.id.logout_tv)
            logoutImage = profile_drawer_view.findViewById(R.id.logout_image)
            clientText = profile_drawer_view.findViewById(R.id.clients_tv)
            clientImage = profile_drawer_view.findViewById(R.id.client_image)
            resourcesTv = profile_drawer_view.findViewById(R.id.resources_tv)
            resourcesIv = profile_drawer_view.findViewById(R.id.resources_image)
            subscriptionTv = profile_drawer_view.findViewById(R.id.subscription_text)
            subscriptionIv = profile_drawer_view.findViewById(R.id.subscription_image)

        },{
            editBtn.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            logoutText.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                logoutRequest()
            }
            logoutImage.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                logoutRequest()
            }
            clientText.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ClientActivity::class.java))
            }
            clientImage.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ClientActivity::class.java))
            }
            resourcesTv.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ResourcesActivity::class.java))
            }
            resourcesIv.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ResourcesActivity::class.java))
            }
            resourcesIv.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ResourcesActivity::class.java))
            }
            subscriptionTv.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, SubscriptionActivity::class.java))
            }
            subscriptionIv.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, SubscriptionActivity::class.java))
            }
        })
        Log.i(title, "Oncreate")

        profile_fab.setOnClickListener {
            drawer_layout.closeDrawer(profile_drawer_view, true)
            startActivity(Intent(this, ClientActivity::class.java))
        }

        //Set last bottom navigation page
        bottomNav.selectedItemId = authViewModel.lastFragmentId ?: 0
        getUserData()


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)


    }

    private fun logoutRequest() {
        authViewModel.logout(this)
    }


    override fun onStart() {
        super.onStart()
        Log.i(title, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(title, "OnResume")
        val token = intent.getStringExtra("token")

//        val user = authViewModel.currentUser



    }


    override fun onPause() {
        super.onPause()
        authViewModel.lastFragmentId = bottomNav.selectedItemId
        Log.i(title, "On pause idsaved ${authViewModel.lastFragmentId} bottomNavId ${bottomNav.selectedItemId}")
    }

    override fun onRestart() {
        super.onRestart()
        i(title, "Restart")
    }

    override fun onDestroy() {
        super.onDestroy()
        i(title, "onDestroy")

    }

    private fun getUserData(){
        val request = authViewModel.getUserData(header.toString())
        i(title, "header $header")
        val response = observeRequest(request, null, null)
        response.observe(this, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask<User>(bool, result){
                val userDetails = result as? UserResponse<User>
                val user = userDetails?.data
                greeting.text = "Hi ${user?.firstName}"
                profileName.text = "${user?.firstName} ${user?.lastName}"
//                authViewModel?.currentUser = user
                i(title, "UserToken ${currentUser?.token} ID\n${user?.id}")
            }
        })


    }




}