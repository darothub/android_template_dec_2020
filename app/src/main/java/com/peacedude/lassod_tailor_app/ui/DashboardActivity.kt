package com.peacedude.lassod_tailor_app.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.peacedude.lassod_tailor_app.ui.profile.ProfileActivity
import com.peacedude.lassod_tailor_app.ui.resources.ResourcesActivity
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionActivity
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.drawer_menu_item.view.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.search_filter_item.view.*
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    val title: String by lazy {
        getName()
    }
    val profileName by lazy {
        profile_drawer_view.findViewById<TextView>(R.id.profile_name)
    }

    val profileImage by lazy {
        profile_drawer_view.findViewById<ImageView>(R.id.profile_image)
    }

    val profileHeaderImage by lazy {
        profile_header.findViewById<ImageView>(R.id.profile_pix_image)
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
        NavController.OnDestinationChangedListener { _, destination, _ ->
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
                R.id.favouritesFragment ->{
                    profile_header.show()
                    profile_fab.hide()
                    bottomNav.show()
                }
            }
        }

    private lateinit var editBtn: Button
    private lateinit var drawerMenuRv:RecyclerView

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel by viewModels<AuthViewModel> {
        viewModelProviderFactory
    }

    val dialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }


    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        changeStatusBarColor(R.color.colorWhite)
        i(title, "Oncreate")


        navController = Navigation.findNavController(this, R.id.dashboard_fragment)

        bottomNav.setupWithNavController(navController)
        drawerMenuRv = profile_drawer_view.findViewById(R.id.drawer_menu_rv)


        Log.i(title, "currentUser ${currentUser?.loggedIn}")



        GlobalVariables.globalUser = currentUser
        getUserData()

        when(currentUser?.category){
            getString(R.string.tailor) -> bottomNav.menu.findItem(R.id.favouritesFragment).isVisible = false
            getString(R.string.weaver) ->  bottomNav.menu.findItem(R.id.favouritesFragment).isVisible = false

        }


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
        drawerMenuRv.setupAdapter<DrawerMenuItem>(R.layout.drawer_menu_item) { _, _, list ->

            bind { itemView, _, item ->
                item?.drawable?.let { itemView.drawer_menu_iv.setImageResource(it) }
                itemView.drawer_menu_tv.text = item?.title

                itemView.setOnClickListener {
                    when(item?.title){
                        getString(R.string.clients) -> {
                            drawer_layout.closeDrawer(profile_drawer_view, true)
                            navController.navigate(R.id.profileFragment)
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
                submitList(listOfDrawerMenuItem.takeLast(2))
            }
            else{
                submitList(listOfDrawerMenuItem)
            }



        }

        navController.navigate(authViewModel.lastFragmentId)


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
            profileImage.load(user?.avatar) {
                crossfade(true)
                placeholder(R.drawable.profile_image)
                transformations(CircleCropTransformation())
            }
            profileHeaderImage.load(user?.avatar) {
                crossfade(true)
                placeholder(R.drawable.profile_image)
                transformations(CircleCropTransformation())
            }

            i(title, "UserToken ${currentUser?.token} ID\n${user?.id}")
        },{err->
            gdErrorToast(
                err,
                Gravity.BOTTOM
            )
            i(title, "DashActError $err")
        })


    }


}

data class DrawerMenuItem(var drawable:Int, var title:String)