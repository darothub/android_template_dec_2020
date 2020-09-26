package com.peacedude.lassod_tailor_app.ui.clientmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_client.*
import kotlinx.android.synthetic.main.fragment_client.*

class ClientActivity : BaseActivity() {
    lateinit var adapter : ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)


        setupViewPager()
        val toolbar = (client_activity_toolbar as Toolbar?)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

    }


    private fun setupViewPager() {

        adapter = ViewPagerAdapter(this, 3) { position->
            when(position){
                0 -> ClientAccountFragment()
                1 -> NativeMeasurementFragment()
                2 -> EnglishMeasurementFragment()
                else -> ClientFragment()
            }
        }
        val clientManagementViewPager = (client_activity_included_viewPager as? ViewPager2)
        (client_activity_included_viewPager as? ViewPager2)?.adapter = adapter
        val tabLayoutMediator =
            clientManagementViewPager?.let {
                TabLayoutMediator(client_activity_tabLayout, it,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when(position){
                            0 -> tab.text = "Client Account"
                            1 -> tab.text = "Native Measurement"
                            2 -> tab.text = "English Measurement"
                            else -> tab.text = "Client"
                        }
                    }).apply {
                    attach()
                }
            }

        client_activity_tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary))

    }
}