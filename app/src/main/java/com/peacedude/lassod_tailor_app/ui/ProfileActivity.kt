package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.adapters.ProfileViewPagerAdapter
import com.peacedude.lassod_tailor_app.utils.bearer
import com.peacedude.lassod_tailor_app.utils.loggedInUser
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.profile_header.*
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
    val currentUser: User? by lazy {
        storageRequest.checkUser(loggedInUser)
    }
    val token by lazy {
        currentUser?.token
    }
    val header by lazy {
        "$bearer $token"
    }
    val menuIcon by lazy{
        profile_header.findViewById<ImageView>(R.id.menu_icon)
    }
    val greeting by lazy{
        profile_header.findViewById<TextView>(R.id.hi_user_name)
    }


    @Inject
    lateinit var storageRequest: StorageRequest
    private lateinit var editBtn:Button
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    lateinit var profileViewPager:ViewPager
    lateinit var profileTabLayout:TabLayout
    lateinit var adapter : ProfileViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNav.setupWithNavController(navController)

//        setSupportActionBar(profile_toolbar)
//
//        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, profile_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()

        profile_header.show()
        menuIcon.setOnClickListener {
            drawer_layout.openDrawer(profile_drawer_view, true)
        }

        buttonTransactions({
            editBtn = profile_drawer_view.findViewById(R.id.edit_profile_btn)
        },{
            editBtn.setOnClickListener {
                navController.navigate(R.id.profileManagementFragment)
                drawer_layout.closeDrawer(profile_drawer_view, true)
            }
        })
        Log.i(title, "Oncreate")




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
        val user = storageRequest.checkUser(loggedInUser)



        getUserData()

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
        val request = authViewModel.getUserData(header)
        val response = observeRequest(request, null, null)
        response.observe(this, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask(bool, result){
                val userDetails = result as? UserResponse
                val user = userDetails?.data
                greeting.text = "Hi ${user?.firstName}"
                profileName.text = "${user?.firstName} ${user?.lastName}"
                Log.i(title, "UserToken ${currentUser?.token} loggedIn\n${user?.firstName}")
            }
        })
    }

//    val masterKey by lazy {
//        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS).
//        setKeyScheme(MasterKey.KeyScheme.AES256_GCM).
//        build()
//    }
//    val sharedPreferences by lazy {
//        SharedPref.sharedPref(requireContext(), masterKey)
//    }
}