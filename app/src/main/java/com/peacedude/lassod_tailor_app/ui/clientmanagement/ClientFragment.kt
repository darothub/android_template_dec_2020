package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.ui.adapters.ViewPagerAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_client.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientFragment : DaggerFragment() {
    lateinit var adapter: ViewPagerAdapter
    private lateinit var nextBtn: Button
    private lateinit var progressBar: ProgressBar
    private val title by lazy {
        getName()
    }
    val clientManagementViewPager by lazy {
        (client_management_included_viewPager as? ViewPager2)
    }

    //    Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.colorTransparentWhite)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        nextBtn = client_account_next_btn2.findViewById(R.id.btn)
        progressBar = client_account_next_btn2.findViewById(R.id.progress_bar)

        val nextBtnBackground = nextBtn.background
        nextBtnBackground?.changeBackgroundColor(requireContext(), R.color.colorPrimary)


        nextBtn.text = getString(R.string.next)
        nextBtn.background = nextBtnBackground
        nextBtn.setTextColor(setCustomColor(R.color.colorWhite))

        nextBtn.setOnClickListener {
            clientManagementViewPager?.currentItem = 1
        }

        clientManagementViewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    1 -> nextBtn.hide()
                    0 -> nextBtn.show()
                }
            }

        })

    }

    private fun setupViewPager() {

        adapter = ViewPagerAdapter(requireActivity(), 3) { position ->
            when (position) {
                0 -> ClientAccountFragment()
                1 -> MeasurementFragment()
                2 -> DeliveryAddressFragment()
                else -> ClientFragment()
            }
        }
//        clientManagementViewPager = (client_management_included_viewPager as? ViewPager2)
        (client_management_included_viewPager as? ViewPager2)?.adapter = adapter
        val tabLayoutMediator =
            clientManagementViewPager?.let {
                TabLayoutMediator(client_management_tabLayout, it,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when (position) {
                            0 -> tab.text = "Client Account"
                            1 -> {
                                if (currentUser?.category == getString(R.string.weaver)) {
                                    tab.view.hide()
                                } else {
                                    tab.text = getString(R.string.measurement)
                                }
                            }
                            2 -> tab.text = "Delivery Address"
                            else -> tab.text = "Client"
                        }
                    }).apply {
                    attach()
                }
            }

        client_management_tabLayout.setBackgroundColor(setCustomColor(R.color.colorWhite))
        client_management_tabLayout.setSelectedTabIndicatorColor(
            setCustomColor(
                R.color.colorPrimary
            )
        )


    }



    fun setItem(item: Int) {
        Log.i(title, "here")
        clientManagementViewPager?.currentItem = item
    }

//    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//        source.lifecycle.addObserver(this)
//    }

}