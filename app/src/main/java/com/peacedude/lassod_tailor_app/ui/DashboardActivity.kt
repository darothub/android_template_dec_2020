package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.Photo
import com.peacedude.lassod_tailor_app.model.response.PhotoList
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.customer.SearchFilter
import com.peacedude.lassod_tailor_app.ui.profile.ProfileActivity
import com.peacedude.lassod_tailor_app.ui.resources.ResourcesActivity
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionActivity
import com.squareup.picasso.Picasso
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.drawer_menu_item.view.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.search_filter_item.view.*
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    companion object Factory {
        lateinit var dashInstance: DashboardActivity
        fun getMainActInstance(): DashboardActivity {
            return dashInstance
        }
    }

    val title: String by lazy {
        getName()
    }
    val profileName by lazy {
        profile_drawer_view.findViewById<TextView>(R.id.profile_name)
    }

    //    Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }

    private val header by lazy {
        authViewModel.header
    }
    private val menuIcon: ImageView? by lazy {
        profile_header.findViewById<ImageView>(R.id.menu_icon)
    }
    private val greeting: TextView by lazy {
        profile_header.findViewById<TextView>(R.id.hi_user_name)
    }

    val navListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.singleChatFragment -> {
                    profile_fab.hide()
                    bottomNav.hide()
                    profile_header.hide()
                }
                R.id.messageFragment -> {
                    profile_header.show()
                    profile_fab.show()
                    bottomNav.show()
                }
                R.id.mediaFragment->{
                    profile_header.show()
                    profile_fab.show()
                    bottomNav.show()
                }
                R.id.profileFragment ->{
                    profile_header.show()
                    profile_fab.show()
                    bottomNav.show()
                }
            }
        }

    private lateinit var editBtn: Button
    private lateinit var logoutText: TextView
    private lateinit var logoutImage: ImageView
    private lateinit var clientImage: ImageView
    private lateinit var clientText: TextView
    private lateinit var resourcesTv: TextView
    private lateinit var resourcesIv: ImageView
    private lateinit var subscriptionTv: TextView
    private lateinit var subscriptionIv: ImageView
    private lateinit var drawerMenuRv:RecyclerView

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    val dialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }

    val checkConnectionTv by lazy {
        dialog.findViewById<TextView>(R.id.loader_layout_tv)
    }

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        changeStatusBarColor(R.color.colorWhite)
        i(title, "Oncreate")

        dashInstance = this

        navController = Navigation.findNavController(this, R.id.dashboard_fragment)

        bottomNav.setupWithNavController(navController)
        drawerMenuRv = profile_drawer_view.findViewById(R.id.drawer_menu_rv)


        Log.i(title, "currentUser $currentUser")


       networkMonitor().observe(this, Observer {
            if (it) {
                Log.i(title, "Network On")
                getUserData()
                dashboard_fragment.view?.show()
            } else {
                dashboard_fragment.view?.invisible()
                Log.i(title, "Network OFF")
            }
        })




//        profile_header.show()

        menuIcon?.setOnClickListener {
            drawer_layout.openDrawer(profile_drawer_view, true)
        }

        buttonTransactions({
            editBtn = profile_drawer_view.findViewById(R.id.edit_profile_btn)
        }, {
            editBtn.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        })
        Log.i(title, "Oncreate")

        profile_fab.setOnClickListener {
            drawer_layout.closeDrawer(profile_drawer_view, true)
            startActivity(Intent(this, ClientActivity::class.java))
        }



        val listOfDrawerMenuItem = arrayListOf<DrawerMenuItem>(
            DrawerMenuItem(R.drawable.ic_contacts_24px, getString(R.string.clients)),
            DrawerMenuItem(R.drawable.theaters_24px, getString(R.string.resources)),
            DrawerMenuItem(R.drawable.ic_shopping_cart_24px, getString(R.string.subscription)),
            DrawerMenuItem(R.drawable.ic_power_settings_new_24px, getString(R.string.logout))

        )
        drawerMenuRv.setupAdapter<DrawerMenuItem>(R.layout.drawer_menu_item) { adapter, context, list ->

            bind { itemView, position, item ->
                item?.drawable?.let { itemView.drawer_menu_iv.setImageResource(it) }
                itemView.drawer_menu_tv.text = item?.title

                itemView.setOnClickListener {
                    when(item?.title){
                        getString(R.string.clients) -> {
                            drawer_layout.closeDrawer(profile_drawer_view, true)
                            goto(ClientActivity::class.java)
                        }
                        getString(R.string.resources) -> {
                            drawer_layout.closeDrawer(profile_drawer_view, true)
                            goto(ResourcesActivity::class.java)
                        }
                        getString(R.string.subscription) -> {
                            drawer_layout.closeDrawer(profile_drawer_view, true)
                            goto(SubscriptionActivity::class.java)

                        }
                        getString(R.string.logout) -> {
                            drawer_layout.closeDrawer(profile_drawer_view, true)
                            logoutRequest()
                        }

                    }
                }
            }


            setLayoutManager(
                LinearLayoutManager(
                    this@DashboardActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )

            if(currentUser?.category == "fashionista"){
                submitList(listOfDrawerMenuItem.takeLast(1))
            }
            else{
                submitList(listOfDrawerMenuItem)
            }



        }


    }

    private fun setInvisible(vararg views: View) {
        for (v in views) {
            v.invisible()
        }
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

        navController.addOnDestinationChangedListener(navListener)
//        navController.navigate(authViewModel.lastFragmentId)


    }


    override fun onPause() {
        super.onPause()
        authViewModel.lastFragmentId = bottomNav.selectedItemId
        navController.removeOnDestinationChangedListener(navListener)
        Log.i(
            title,
            "On pause idsaved ${authViewModel.lastFragmentId} bottomNavId ${bottomNav.selectedItemId}"
        )
    }

    override fun onRestart() {
        super.onRestart()
        i(title, "Restart")
    }

    override fun onDestroy() {
        super.onDestroy()
        i(title, "onDestroy")

    }

    private fun getUserData() {
        val request = authViewModel.getUserData()
        i(title, "header $header")
        observeRequest<User>(request, null, null, false, {userDetails->
            val user = userDetails?.data
            greeting.text = "Hi ${user?.firstName}"
            profileName.text = "${user?.firstName} ${user?.lastName}"
//                authViewModel?.currentUser = user
            i(title, "UserToken ${currentUser?.token} ID\n${user?.id}")
        },{err->
            i(title, "DashActError $err")
        })
//        response.observe(this, androidx.lifecycle.Observer {
//            val (bool, result) = it
//            onRequestResponseTask<User>(bool, result) {
//
//            }
//        })

    }


}

data class DrawerMenuItem(var drawable:Int, var title:String)