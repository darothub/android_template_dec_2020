package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.ui.PaymentMethodFragment
import com.peacedude.lassod_tailor_app.ui.ProfileFragment
import com.peacedude.lassod_tailor_app.ui.SpecialtyFragment
import com.peacedude.lassod_tailor_app.ui.UserAccountFragment
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.fragment_profile_management.*


/**
 * A simple [Fragment] subclass.
 * Use the [ClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientFragment : Fragment() {
    lateinit var adapter : ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    override fun onResume() {
        super.onResume()

        setupViewPager()
        val toolbar = (client_management_toolbar as Toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))

        val navController = Navigation.findNavController(client_management_appBar)


        NavigationUI.setupWithNavController(toolbar, navController)
    }

    private fun setupViewPager() {

        adapter = ViewPagerAdapter(requireActivity(), 3) { position->
            when(position){
                0 -> ClientAccountFragment()
                1 -> NativeMeasurementFragment()
                2 -> EnglishMeasurementFragment()
                else -> ClientFragment()
            }
        }
        val clientManagementViewPager = (client_management_included_viewPager as? ViewPager2)
        (client_management_included_viewPager as? ViewPager2)?.adapter = adapter
        val tabLayoutMediator =
            clientManagementViewPager?.let {
                TabLayoutMediator(client_management_tabLayout, it,
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

        client_management_tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

    }
}