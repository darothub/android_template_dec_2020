package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

class ProfileActivity : BaseActivity() {
    private val navController by lazy {
        Navigation.findNavController(this, R.id.fragment2)
    }
    val title: String by lazy {
        getName()
    }
    val profileName by lazy{
        profile_drawer_view.findViewById<TextView>(R.id.profile_name)
    }
    //Get logged-in user
//    private val currentUser: User? by lazy {
//        authViewModel.currentUser
//    }

    private val header by lazy {
        authViewModel.header
    }
    private val menuIcon: ImageView? by lazy{
        profile_header.findViewById<ImageView>(R.id.menu_icon)
    }
    private val greeting: TextView by lazy{
        profile_header.findViewById<TextView>(R.id.hi_user_name)
    }
    private val destinationChangedListener by lazy {
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.profileManagementFragment -> profile_header.hide()
                R.id.mediaFragment -> {
                    authViewModel.lastFragmentId = R.id.mediaFragment
                    i(title, "id ${R.id.mediaFragment}")
                }
                R.id.messageFragment -> {
                    authViewModel.lastFragmentId = R.id.messageFragment
                    i(title, "id ${R.id.messageFragment}")
                }
                R.id.profileFragment -> {
                    authViewModel.lastFragmentId = R.id.profileFragment
                    i(title, "id ${R.id.profileFragment}")
                }
                R.id.clientFragment -> {
                    profile_header.hide()
                }
                R.id.clientAccountFragment ->{
                    profile_fab.alpha = 0.0f
                }
            }
        }
    }
    private lateinit var editBtn:Button
    private lateinit var logoutText:TextView
    private lateinit var logoutImage:ImageView
    private lateinit var clientImage:ImageView
    private lateinit var clientText:TextView
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        i(title, "Oncreate")
        bottomNav.setupWithNavController(navController)
//        setBottomNavController()

        profile_header.show()
//        getUserData()
        menuIcon?.setOnClickListener {
            drawer_layout.openDrawer(profile_drawer_view, true)
        }

        buttonTransactions({
            editBtn = profile_drawer_view.findViewById(R.id.edit_profile_btn)
            logoutText = profile_drawer_view.findViewById(R.id.logout_tv)
            logoutImage = profile_drawer_view.findViewById(R.id.logout_image)
            clientText = profile_drawer_view.findViewById(R.id.clients_tv)
            clientImage = profile_drawer_view.findViewById(R.id.client_image)

        },{
            editBtn.setOnClickListener {
                navController.navigate(R.id.profileManagementFragment)
                drawer_layout.closeDrawer(profile_drawer_view, true)
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
        })
        Log.i(title, "Oncreate")

        profile_fab.setOnClickListener {
            drawer_layout.closeDrawer(profile_drawer_view, true)
            startActivity(Intent(this, ClientActivity::class.java))
        }

        val  lastFragmentId = authViewModel.lastFragmentId!!
        i(title, "lastFragmentId $lastFragmentId")
        if(lastFragmentId == 0) {
            bottomNav.selectedItemId = bottomNav.menu[0].itemId
        }else{
            bottomNav.selectedItemId = lastFragmentId
        }

//


    }

    private fun goto(destinationId:Int) {
        navController.navigate(destinationId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putSerializable(loggedInUserKey, currentUser)
//        outState.putString("header", header)


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
        navController.addOnDestinationChangedListener(destinationChangedListener)
        Log.i(title, "OnResume")
        val token = intent.getStringExtra("token")

//        val user = authViewModel.currentUser

//        getUserData()

    }

    override fun onBackPressed() {
//        finish()
//        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
//            drawer_layout.closeDrawer(GravityCompat.START)
//        }
//        else{
//            super.onBackPressed()
//        }

    }

    override fun onPause() {
        super.onPause()
        Log.i(title, "On pause")
        navController.removeOnDestinationChangedListener(destinationChangedListener)
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
        val response = observeRequest(request, null, null)
        response.observe(this, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask<User>(bool, result){
                val userDetails = result as? UserResponse<User>
                val user = userDetails?.data
                greeting.text = "Hi ${user?.firstName}"
                profileName.text = "${user?.firstName} ${user?.lastName}"
                authViewModel?.currentUser = user
//                Log.i(title, "UserToken ${currentUser?.token} loggedIn\n${user?.firstName}")
            }
        })


    }




}