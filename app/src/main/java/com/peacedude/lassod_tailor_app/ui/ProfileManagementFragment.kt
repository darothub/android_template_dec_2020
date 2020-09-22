package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_profile_management.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileManagementFragment : Fragment() {

    lateinit var adapter : ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_management, container, false)
    }

    override fun onStart() {
        super.onStart()

        setupViewPager()
//
        val toolbar = profile_management_toolbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.profile_management_toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))

        val navController = Navigation.findNavController(profile_management_appBar)


        NavigationUI.setupWithNavController(toolbar, navController)

    }

    private fun setupViewPager() {

        adapter = ViewPagerAdapter(requireActivity(), 3) { position->
            when(position){
                0 -> UserAccountFragment()
                1 -> SpecialtyFragment()
                2 -> PaymentMethodFragment()
                else -> ProfileFragment()
            }
        }
        profile_management_viewPager.adapter = adapter
        val tabLayoutMediator = TabLayoutMediator(profile_management_tabLayout, profile_management_viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0 -> tab.text = "Account"
                    1 -> tab.text = "Specialty"
                    2 -> tab.text = "Payment method"
                    else -> ProfileFragment()
                }
            }).apply {
            attach()
        }

//        profile_management_tabLayout.setupWithViewPager(profile_management_viewPager)
//        profile_management_tabLayout.setSelectedTabIndicatorColor(
//            ContextCompat.getColor(
//                requireContext(),
//                R.color.colorAccent
//            )
//        )

        profile_management_tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

    }

}