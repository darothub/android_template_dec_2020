package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.ui.resources.ResourcesActivity
import com.peacedude.lassod_tailor_app.ui.subscription.SubscriptionActivity
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.drawer_menu_item.view.*
import kotlinx.android.synthetic.main.fragment_drawer.*
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [DrawerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrawerFragment : DaggerFragment() {

    val title: String by lazy {
        getName()
    }
    //    Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val authViewModel: AuthViewModel by activityViewModels {
        viewModelProviderFactory
    }
    val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.custom_dialog)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfDrawerMenuItem = arrayListOf<DrawerMenuItem>(
            DrawerMenuItem(R.drawable.ic_contacts_24px, getString(R.string.clients)),
            DrawerMenuItem(R.drawable.theaters_24px, getString(R.string.resources)),
            DrawerMenuItem(R.drawable.ic_shopping_cart_24px, getString(R.string.subscription)),
            DrawerMenuItem(R.drawable.ic_power_settings_new_24px, getString(R.string.logout))
        )

//        drawer_fragment_menu_rv.setupAdapter<DrawerMenuItem>(R.layout.drawer_menu_item) { _, _, list ->
//
//            bind { itemView, _, item ->
//                item?.drawable?.let { itemView.drawer_menu_iv.setImageResource(it) }
//                itemView.drawer_menu_tv.text = item?.title
//
//                itemView.setOnClickListener {
//                    when(item?.title){
//                        getString(R.string.clients) -> {
//                            requireActivity().drawer_layout.closeDrawer(requireActivity().profile_drawer_view.requireView(), true)
//                            goto(DashboardActivity::class.java)
//                        }
//                        getString(R.string.resources) -> {
//                            requireActivity().drawer_layout.closeDrawer(requireActivity().profile_drawer_view.requireView(), true)
//                            goto(ResourcesActivity::class.java)
//                        }
//                        getString(R.string.subscription) -> {
//                            requireActivity().drawer_layout.closeDrawer(requireActivity().profile_drawer_view.requireView(), true)
//                            goto(SubscriptionActivity::class.java)
//
//                        }
//                        getString(R.string.logout) -> {
//                            requireActivity().drawer_layout.closeDrawer(requireActivity().profile_drawer_view.requireView(), true)
////                            logoutRequest()
//                        }
//
//                    }
//                }
//            }
//
//
//            setLayoutManager(
//                LinearLayoutManager(
//                    requireContext(),
//                    LinearLayoutManager.VERTICAL,
//                    false
//                )
//            )
//
//            if(currentUser?.category == "fashionista"){
//                submitList(listOfDrawerMenuItem.takeLast(2))
//            }
//            else{
//                submitList(listOfDrawerMenuItem)
//            }
//
//
//
//        }


    }

    override fun onStart() {
        super.onStart()
        getUserData()
    }

    private fun getUserData() {
//        authViewModel.responseLiveData.observe(viewLifecycleOwner,  {
//
//        })
//        val request = authViewModel.getUserData()

//        observeRequest<User>(authViewModel.responseLiveData, null, null, false, { userDetails ->
//            val user = userDetails?.data
//            profile_name.text = "${user?.firstName} ${user?.lastName}"
//            profile_image.load(user?.avatar) {
//                crossfade(true)
//                placeholder(R.drawable.profile_image)
//                transformations(CircleCropTransformation())
//            }
//            profileHeaderImage.load(user?.avatar) {
//                crossfade(true)
//                placeholder(R.drawable.profile_image)
//                transformations(CircleCropTransformation())
//            }
//
//            i(title, "UserToken ${currentUser?.token} ID\n${user?.id}")
//        }, { err ->
//            requireActivity().gdErrorToast(
//                err,
//                Gravity.BOTTOM
//            )
//            i(title, "DashActError $err")
//        })


    }



}