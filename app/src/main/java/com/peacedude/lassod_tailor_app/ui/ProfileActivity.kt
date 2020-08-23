package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_login.*

class ProfileActivity : AppCompatActivity() {
    private val navController by lazy {
        Navigation.findNavController(this, R.id.fragment2)
    }
    private lateinit var editBtn:Button
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
            drawer_layout.openDrawer(drawer_view, true)
        }

        buttonTransactions({
            editBtn = drawer_view.findViewById(R.id.edit_profile_btn)
        },{
            editBtn.setOnClickListener {
                navController.navigate(R.id.profileManagementFragment)
                drawer_layout.closeDrawer(drawer_view, true)
            }
        })

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
}