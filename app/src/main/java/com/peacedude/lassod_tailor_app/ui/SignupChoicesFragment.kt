package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.ui.profile.ProfileActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_signup_choices.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SignupChoicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupChoicesFragment : DaggerFragment() {
    val title = getName()
    lateinit var signupEmailBtn: Button
    lateinit var signupPhoneBtn: Button
    var RC_SIGN_IN = 1

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }

    @Inject
    lateinit var mGoogleSignInClient:GoogleSignInClient
    lateinit var observer : StartActivityForResults
    val arg:SignupChoicesFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = StartActivityForResults(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_choices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = mGoogleSignInClient.signInIntent

        val backgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_outline)
        buttonTransactions({
            signupEmailBtn = signup_option_email_btn.findViewById(R.id.btn)
            signupPhoneBtn = signup_phone_btn.findViewById(R.id.btn)
            signupEmailBtn.text = getString(R.string.signup_with_email_str)
            signupPhoneBtn.text = getString(R.string.signup_with_phone_str)
            signupEmailBtn.background = backgroundDrawable
            signupPhoneBtn.background = backgroundDrawable

        }, {
            signupEmailBtn.setOnClickListener {

                goto(R.id.emailSignupFragment)
            }
            signupPhoneBtn.setOnClickListener {

                goto(R.id.phoneSignupFragment)
            }

        })


        userViewModel.netWorkLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                signupEmailBtn.show()
                signupPhoneBtn.show()
                google_sign_in_button.show()
            } else {
                signupEmailBtn.invisible()
                signupPhoneBtn.invisible()
                google_sign_in_button.invisible()
            }
        })

        google_sign_in_button.setOnClickListener {
            observer.launchIntentToSignIn(intent, viewLifecycleOwner){}
            observer.getUserLiveData.observe(viewLifecycleOwner, Observer {
                i(title, "Firstname ${it.firstName}")
                GlobalVariables.globalUser = it
                i(title, "UserGlobal ${GlobalVariables.globalUser}")
                val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToEmailSignupFragment()
                action.newUser = it
                goto(action)

            })
//            startActivityForResult(intent, RC_SIGN_IN)
        }

    }
    override fun onResume() {
        super.onResume()

//        mGoogleSignInClient.silentSignIn().addOnCompleteListener {task ->
//            when(task.isSuccessful){
//                true -> requireActivity().gdToast("Already signed in", Gravity.BOTTOM)
//
//            }
//
//
//        }

    }


}