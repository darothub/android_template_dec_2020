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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_signup_choices.*
import javax.inject.Inject
import kotlin.properties.Delegates.observable


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

//    private val gso: GoogleSignInOptions by lazy {
//        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//    }
//    private val mGoogleSignInClient: GoogleSignInClient by lazy{ GoogleSignIn.getClient(requireContext(), gso)}
    @Inject
    lateinit var mGoogleSignInClient:GoogleSignInClient

    val arg:SignupChoicesFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
//        var maxCount: Int by observable(initialValue = 0) { property, oldValue, newValue ->
//            println("${property.name} is being changed from $oldValue to $newValue")
//        }
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



        google_sign_in_button.setOnClickListener {
            val registerForActivity = RegisterForActivityResult.getInstance(
                requireActivity().activityResultRegistry,
                requireActivity()
            )
            val intent = mGoogleSignInClient.signInIntent
            registerForActivity.onCreate(this, intent){result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    task.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val account: GoogleSignInAccount? =
                                it.getResult(ApiException::class.java)
                            val idToken = it.result?.idToken
                            val email = account?.email
                            val lastName = account?.familyName
                            val firstName = account?.givenName
                            val otherName = account?.displayName
                            val imageUrl = account?.photoUrl
                            val category = arg.category
                            val newUser = User(firstName, lastName, otherName, category, null)
                            newUser.email = email
                            newUser.imageUrl = imageUrl.toString()
                            userViewModel.currentUser = newUser
                            newUser.token = idToken

                            i(title, "idToken $idToken")

                            requireActivity().gdToast("Authentication successful", Gravity.BOTTOM)
                            val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToEmailSignupFragment()
                            action.newUser = newUser
                            goto(action)
                        } else {
                            requireActivity().gdToast(
                                "Authentication Unsuccessful",
                                Gravity.BOTTOM
                            )
                            Log.i(title, "Task not successful")
                        }

                    }
                } else {
                    Log.i(title, "OKCODE ${Activity.RESULT_OK} RESULTCODE ${result.resultCode}")
                }
            }

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
//                    requireActivity().gdToast("Authentication successful", Gravity.BOTTOM)
//                    val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToSignupCategoryFragment()
//                    action.newUser = newUser
//                    goto(action)
//
//                    Log.i(title, "token $firstName")
////                           requireActivity().startActivity(Intent(requireActivity(), ProfileActivity::class.java))
//                }
//            }
//        }
//
//    }



}