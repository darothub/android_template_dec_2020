package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_client.*

class ClientActivity : BaseActivity() {
    val title by lazy {
        getName()
    }
    lateinit var adapter: ViewPagerAdapter
    private lateinit var navController: NavController
    private lateinit var clientManagementViewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        changeStatusBarColor(R.color.colorWhite)

        val toolbar = client_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
        val activityTitle = client_activity_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.add_client)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            i(title, "Toolbar")
            GlobalVariables.globalClient = null
            goto(DashboardActivity::class.java)
        }
    }
}
