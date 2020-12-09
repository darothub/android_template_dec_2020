package com.peacedude.lassod_tailor_app.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.changeStatusBarColor
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.hide
import com.peacedude.lassod_tailor_app.helpers.setCustomColor
import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_client.*
import kotlinx.android.synthetic.main.fragment_profile_management.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileManagementFragment : DaggerFragment() {

    private val title by lazy {
        getName()
    }

    val currentUser by lazy {
        userViewModel.currentUser
    }
    val header by lazy {
        userViewModel.header
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }

    lateinit var adapter : ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
//
    }

    private fun setupViewPager() {

//        val newView = View.inflate(requireContext(), R.layout.tab_custom_layout, null)
//        val tv = newView.findViewById<TextView>(R.id.textViewTab)



        adapter = ViewPagerAdapter(requireActivity(), 4) { position->
            when(position){
                0 -> UserAccountFragment()
                1 -> SpecialtyFragment()
                2 -> PaymentMethodFragment()
                3 -> SecurityFragment()
                else -> ProfileFragment()
            }
        }
        val profileManagementViewPager = (profile_management_included_viewPager as? ViewPager2)


        profile_management_tabLayout.setBackgroundColor(setCustomColor(R.color.colorWhite))
        (profile_management_included_viewPager as? ViewPager2)?.adapter = adapter
        val tabLayoutMediator =
            profileManagementViewPager?.let {
                TabLayoutMediator(profile_management_tabLayout, it,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when (position) {

                            0 -> tab.text = getString(R.string.account_str)
                            1 -> {
                                if (currentUser?.category == "fashionista") {
                                    tab.view.hide()
                                } else {
                                    tab.text = getString(R.string.specialty)
                                }

                            }
                            2 -> {
                                if (currentUser?.category == "fashionista") {
                                    tab.view.hide()
                                } else {
                                    tab.text = getString(R.string.payment_method)
                                }

                            }
                            3 -> {
                                tab.text = getString(R.string.security_str)
                            }
                            else -> ProfileFragment()
                        }
                    }).apply {
                    attach()
                }
            }

        profile_management_tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

//        if(currentUser?.category == "fashionista"){
//            profile_management_tabLayout.getChildAt(1).visibility = View.GONE
//            profile_management_tabLayout.getChildAt(2).visibility = View.GONE
//        }

    }

}