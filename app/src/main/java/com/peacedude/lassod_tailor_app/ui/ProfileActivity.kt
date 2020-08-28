package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.observeRequest
import com.peacedude.lassod_tailor_app.helpers.onRequestResponseTask
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.utils.bearer
import com.peacedude.lassod_tailor_app.utils.loggedInUser
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
    val currentUser: User? by lazy {
        storageRequest.checkUser(loggedInUser)
    }
    val token by lazy {
        currentUser?.token
    }
    val header by lazy {
        "$bearer $token"
    }
    @Inject
    lateinit var storageRequest: StorageRequest
    private lateinit var editBtn:Button
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bottomNav.setupWithNavController(navController)

//        setSupportActionBar(profile_toolbar)
//
//        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, profile_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()

        menu_icon.setOnClickListener {
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

    }

    override fun onResume() {
        super.onResume()

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

    private fun getUserData(){
        val request = authViewModel.getUserData(header)
        val response = observeRequest(request, null, null)
        response.observe(this, androidx.lifecycle.Observer {
            val (bool, result) = it
            onRequestResponseTask(bool, result){
                val userDetails = result as? UserResponse
                val user = userDetails?.data
                hi_user_name.append(" ${user?.firstName}")
                profileName.append("${user?.firstName} ${user?.lastName}")
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