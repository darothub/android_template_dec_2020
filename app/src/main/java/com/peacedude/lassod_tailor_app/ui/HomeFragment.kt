package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class HomeFragment : DaggerFragment() {
    val title by lazy{
        getName()
    }
    lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private val leftAnimation: Animation? by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.left_animation)
    }
    private val rightAnimation: Animation? by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation)
    }


    private val currentUser: User? by lazy {
        userViewModel.currentUsers
    }

    @Inject
    lateinit var mGoogleSignInClient:GoogleSignInClient
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(loggedInUserKey, currentUser)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signupBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        val loginBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_outline)



        buttonTransactions({
            loginBtn = login_btn.findViewById(R.id.btn)
            signupBtn = signup_btn.findViewById(R.id.btn)

        }, {
            loginBtn.text = getString(R.string.login)
            signupBtn.text = getString(R.string.signup)

            loginBtn.animation = leftAnimation
            signupBtn.animation = rightAnimation

            signupBtn.background = signupBackgroundDrawable
            signupBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            loginBtn.background = loginBackgroundDrawable
        })



        signupBtn.setOnClickListener {
            findNavController().navigate(R.id.signupChoicesFragment)
        }

        loginBtn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        val  googleEmail = account?.email
        val sharedPrefEmail = currentUser?.email

        val user = GlobalVariables.globalUser
        i(title, "onResume1 $user loggedIn ${user?.loggedIn}")
        if(user != null && user.loggedIn){
            goto(DashboardActivity::class.java)
            finish()
        }
        else if(account != null && currentUser?.email == googleEmail){
            goto(DashboardActivity::class.java)
            finish()
            Log.i(title, "Emails $googleEmail $sharedPrefEmail")

        }
        else{
            goto(R.id.loginFragment)
        }


    }

    override fun onResume() {
        super.onResume()

    }




}
