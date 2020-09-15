package com.peacedude.lassod_tailor_app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.CustomWebViewClient
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.model.request.User
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

    val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }
    val mGoogleSignInClient by lazy{ GoogleSignIn.getClient(requireContext(), gso)}

    val arg:SignupChoicesFragmentArgs by navArgs()
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
        return inflater.inflate(R.layout.fragment_signup_choices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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




        val someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->

                if (result.resultCode == Activity.RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    task.addOnCompleteListener {
                        if(it.isSuccessful){
                            val account: GoogleSignInAccount? =
                                it.getResult(ApiException::class.java)

                            val email = account?.email
                            val lastName = account?.familyName
                            val firstName = account?.givenName
                            val otherName = account?.displayName
                            val imageUrl = account?.photoUrl
                            val category = arg.category
                            val newUser = User(firstName, lastName, otherName, category, null)
                            newUser.email = email

                            requireActivity().gdToast("Aunthentication successful", Gravity.BOTTOM)
                            val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToEmailSignupFragment()
                            action.newUser = newUser
                            goto(action)

                            Log.i(title, "token $firstName")
//                           requireActivity().startActivity(Intent(requireActivity(), ProfileActivity::class.java))
                        }
                    }
                }

            }
        )

        google_sign_in_button.setOnClickListener {
//            webviev.webChromeClient = CustomWebViewClient()
//            val setting = webviev.settings
//            setting.javaScriptEnabled = true
//            webviev.loadUrl("https://obioma-staging.herokuapp.com/api/v1/auth/google")

            val intent = mGoogleSignInClient.getSignInIntent()
            someActivityResultLauncher?.launch(intent)
//            startActivityForResult(intent, RC_SIGN_IN)
        }

    }
    override fun onResume() {
        super.onResume()

        mGoogleSignInClient.silentSignIn().addOnCompleteListener {task ->
            when(task.isSuccessful){
                true -> requireActivity().gdToast("Already signed in", Gravity.BOTTOM)

            }


        }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == RC_SIGN_IN){
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            task.addOnCompleteListener {
//                if(it.isSuccessful){
//                    val account: GoogleSignInAccount? =
//                        it.getResult(ApiException::class.java)
//
//                    val email = account?.email
//                    val lastName = account?.familyName
//                    val firstName = account?.givenName
//                    val otherName = account?.displayName
//                    val imageUrl = account?.photoUrl
//                    val category = arg.category
//                    val newUser = User(firstName, lastName, otherName, category, null)
//                    newUser.email = email
//
//                    requireActivity().gdToast("Aunthentication successful", Gravity.BOTTOM)
//                    val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToSignupCategoryFragment()
//                    action.newUser = newUser
//                    goto(action)
//
//                    Log.i(title, "token $firstName")
////                           requireActivity().startActivity(Intent(requireActivity(), ProfileActivity::class.java))
//                }
//            }
//        }

//    }



}