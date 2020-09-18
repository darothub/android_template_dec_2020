package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.adapters.ProfileViewPagerAdapter
import com.peacedude.lassod_tailor_app.utils.bearer
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
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        savedInstanceState?.let {instateBundle ->
            instateBundle.apply {
                authViewModel.header = this["header"] as String?
                (this[loggedInUserKey] as User).let {
                    authViewModel.currentUser = it
                }
            }

        }

        bottomNav.setupWithNavController(navController)

//        setSupportActionBar(profile_toolbar)
//
//        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, profile_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()

        profile_header.show()
        menuIcon?.setOnClickListener {
            drawer_layout.openDrawer(profile_drawer_view, true)
        }

        buttonTransactions({
            editBtn = profile_drawer_view.findViewById(R.id.edit_profile_btn)
            logoutText = profile_drawer_view.findViewById(R.id.logout_tv)
            logoutImage = profile_drawer_view.findViewById(R.id.logout_image)
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
        })
        Log.i(title, "Oncreate")


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(loggedInUserKey, currentUser)
        outState.putString("header", header)

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
        Log.i(title, "Token $token")
        val user = authViewModel.currentUser



//        getUserData()

    }

    override fun onBackPressed() {


        finish()
//        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
//            drawer_layout.closeDrawer(GravityCompat.START)
//        }
//        else{
//            super.onBackPressed()
//        }

    }

    @SuppressLint("SetTextI18n")
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
                authViewModel.currentUser = user
                Log.i(title, "UserToken ${currentUser?.token} loggedIn\n${user?.firstName}")
            }
        })
    }




}