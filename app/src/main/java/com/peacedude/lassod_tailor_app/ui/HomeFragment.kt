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
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.invisible
import com.peacedude.lassod_tailor_app.helpers.show
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
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
        userViewModel.currentUser
    }

    @Inject
    lateinit var mGoogleSignInClient:GoogleSignInClient
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {instateBundle ->
            (instateBundle[loggedInUserKey] as? User)?.let {
                userViewModel.currentUser = it
            }
        }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

        userViewModel.netWorkLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                loginBtn.show()
                signupBtn.show()
                Log.i("Base", "Network On")

            } else {
                loginBtn.invisible()
                signupBtn.invisible()

                Log.i("Base", "Network OFF")
            }
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

        if (currentUser != null) {
            when (currentUser?.loggedIn) {
                true -> startActivity(Intent(requireContext(), DashboardActivity::class.java))
            }
        }
        else if(account != null && currentUser?.email == googleEmail){
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
            Log.i(title, "Emails $googleEmail $sharedPrefEmail")

//            requireActivity().request(null, null, userViewModel, {
//                userViewModel.loginUserRequest(account.email.toString(), "Password")
//            },{b, any ->
//                onRequestResponseTask(b, any) {
//                    val userDetails = any as? UserResponse<User>
//                    val user = userDetails?.data
//                    user?.loggedIn = true
//                    userViewModel.currentUser = user
//                    val res = userViewModel.saveUser
//                    val loginIntent = Intent(requireContext(), ProfileActivity::class.java)
//                    Log.i("$this", "res ${res.size}")
//                    startActivity(loginIntent)
//                    requireActivity().finish()
//                }
//            })
        }
    }

    override fun onResume() {
        super.onResume()

    }




}
