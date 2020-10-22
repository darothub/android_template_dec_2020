package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import kotlinx.android.synthetic.main.activity_dashboard.*


/**
 * A simple [Fragment] subclass.
 * Use the [VerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerificationFragment : Fragment() {
    private val navController by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragment4)
    }
    val title: String by lazy {
        getName()
    }
    val profileName by lazy{
        profile_drawer_view.findViewById<TextView>(R.id.profile_name)
    }
    //Get logged-in user
//    private val currentUser: User? by lazy {
//        authViewModel.currentUser
//    }

//    private val header by lazy {
//        authViewModel.header
//    }
    private val menuIcon: ImageView? by lazy{
        profile_header.findViewById<ImageView>(R.id.menu_icon)
    }
    private val greeting: TextView by lazy{
        profile_header.findViewById<TextView>(R.id.hi_user_name)
    }
    private val destinationChangedListener by lazy {
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.profileManagementFragment -> profile_header.hide()
            }
        }
    }
    private lateinit var editBtn: Button
    private lateinit var logoutText: TextView
    private lateinit var logoutImage: ImageView
    private lateinit var clientImage: ImageView
    private lateinit var clientText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuIcon?.setOnClickListener {
            drawer_layout.openDrawer(profile_drawer_view, true)
        }

        buttonTransactions({
            editBtn = profile_drawer_view.findViewById(R.id.edit_profile_btn)
            logoutText = profile_drawer_view.findViewById(R.id.logout_tv)
            logoutImage = profile_drawer_view.findViewById(R.id.logout_image)
            clientText = profile_drawer_view.findViewById(R.id.clients_tv)
            clientImage = profile_drawer_view.findViewById(R.id.client_image)

        },{
            editBtn.setOnClickListener {
                navController.navigate(R.id.profileManagementFragment)
                drawer_layout.closeDrawer(profile_drawer_view, true)
            }
            logoutText.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
//                logoutRequest()
            }
            logoutImage.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
//                logoutRequest()
            }
            clientText.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
//                startActivity(Intent(this, ClientActivity::class.java))
            }
            clientImage.setOnClickListener {
                drawer_layout.closeDrawer(profile_drawer_view, true)
//                startActivity(Intent(this, ClientActivity::class.java))
            }
        })

    }


    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

//    if (result.resultCode == Activity.RESULT_OK) {
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        task.addOnCompleteListener {
//            if (it.isSuccessful) {
//                val account: GoogleSignInAccount? =
//                    it.getResult(ApiException::class.java)
//                val idToken = it.result?.idToken
//                val email = account?.email
//                val lastName = account?.familyName
//                val firstName = account?.givenName
//                val otherName = account?.displayName
//                val imageUrl = account?.photoUrl
//                val category = arg.category
//                val newUser = User()
//                newUser.firstName = firstName
//                newUser.lastName = lastName
//                newUser.otherName = otherName
//                newUser.category = category
//                newUser.email = email
//                newUser.imageUrl = imageUrl.toString()
//                userViewModel.currentUser = newUser
//                newUser.token = idToken
//
//                i(title, "idToken $idToken")
//
//                requireActivity().gdToast("Authentication successful", Gravity.BOTTOM)
//                val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToEmailSignupFragment()
//                action.newUser = newUser
//                goto(action)
//            } else {
//                requireActivity().gdToast(
//                    "Authentication Unsuccessful",
//                    Gravity.BOTTOM
//                )
//                Log.i(title, "Task not successful")
//            }
//
//        }
//    } else {
//        Log.i(title, "OKCODE ${Activity.RESULT_OK} RESULTCODE ${result.resultCode}")
//    }
}