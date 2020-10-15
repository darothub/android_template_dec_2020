package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.utils.bearer
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : DaggerFragment() {

    val title: String by lazy {
        getName()
    }
    private lateinit var loginBtn: Button
    private lateinit var progressBar: ProgressBar

    private val newUserText: String by lazy {
        getString(R.string.new_user)
    }
    private val googleBtnText: String by lazy {
        getString(R.string.signin_with_google)
    }

    private val googleBtnTextSpannable: SpannableString by lazy {
        googleBtnText.setAsSpannable()

    }
    private val spannableString: SpannableString by lazy {
        newUserText.setAsSpannable()
    }
    private var textColor = 0;
    private var googleBtnTextColor = 0
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }
    @Inject
    lateinit var mGoogleSignInClient:GoogleSignInClient

    private val leftAnimation: Animation? by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.left_animation)
    }
    private val rightAnimation: Animation? by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonTransactions({
            loginBtn = login_page_btn.findViewById(R.id.btn)
            progressBar = login_page_btn.findViewById(R.id.progress_bar)
        },{
            loginBtn.text = getString(R.string.login)
        })

        login_with_email_tv.setOnClickListener {
            login_email_input.show()
            login_email_input.animation = leftAnimation
            login_phone_number_input.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_phone_number_input.invisible()

            login_with_phone_tv.show()
            login_with_phone_tv.animation = leftAnimation
            login_with_email_tv.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_with_email_tv.invisible()

        }
        login_with_phone_tv.setOnClickListener {
            login_phone_number_input.show()
            login_phone_number_input.animation = leftAnimation
            login_email_input.animation =AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_email_input.invisible()

            login_with_email_tv.show()
            login_with_email_tv.animation = leftAnimation
            login_with_phone_tv.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_with_phone_tv.invisible()


        }

        val toolbar = login_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(login_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)

        loginBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))


        loginBtn.setOnClickListener {
            validateAndLogin()
        }
        initEnterKeyToSubmitForm(login_password_et) { validateAndLogin() }

        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        googleBtnTextColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val textLen = newUserText.length
        val start = 10
        setupSpannableLinkAndDestination(new_user, textColor, spannableString, start, textLen){
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.signupChoicesFragment)
            }
        }
        val googleTextLen = googleBtnText.length
        setupSpannableLinkAndDestination(login_google_sign_in_button, googleBtnTextColor, googleBtnTextSpannable, 0, googleTextLen){}
        requireActivity().onBackPressedDispatcher.addCallback {
            goto(R.id.homeFragment)
        }

        forgot_password_tv.setOnClickListener {
            goto(R.id.forgotPassword)
        }
        login_google_sign_in_button.setOnClickListener {
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
                            val firstName = account?.givenName
                            val lastName = account?.familyName
                            val otherName = account?.displayName
                            val email = account?.email
                            val password = ""
                            requireActivity().gdToast(
                                "Authentication successful",
                                Gravity.BOTTOM
                            )
                            val user = User(firstName, lastName, otherName)
                            user.email = email
                            user.token = account?.idToken
                            val googleAuthHeader = "$bearer ${user.token.toString()}"
                            val req = userViewModel.loginWithGoogle(googleAuthHeader)
                            i(title, "header ${user.token}")
                            requireActivity().requestObserver(
                                null,
                                null,
                                req
                            ) { bool, result ->
                                onRequestResponseTask(bool, result) {
                                    val userDetails = result as? UserResponse<User>
                                    val user = userDetails?.data
                                    user?.loggedIn = true
                                    user?.token = user?.token
                                    userViewModel.currentUser = user
//                            val res = userViewModel.saveUser
                                    val loginIntent =
                                        Intent(requireContext(), DashboardActivity::class.java)
                                    i(
                                        "$this",
                                        "res $user \ntoken ${user?.token}\ntoken 2${user?.token}"
                                    )
                                    startActivity(loginIntent)
                                    requireActivity().finish()
                                }
                            }
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
//            val intent = mGoogleSignInClient.getSignInIntent()
//            someActivityResultLauncher.launch(intent)
        }
    }


    private fun validateAndLogin() {
        val phoneNumber = login_phone_number_et.text.toString().trim()
        val passwordString = login_password_et.text.toString()

        val checkForEmpty = IsEmptyCheck(login_password_et, login_phone_number_et)

        when {
            checkForEmpty != null -> {
                checkForEmpty.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${resources.getResourceEntryName(checkForEmpty.id)} is empty", Gravity.BOTTOM)
            }
            else -> {

                requireActivity().requestObserver(progressBar, loginBtn,
                    userViewModel.loginUserRequest(phoneNumber, passwordString)
                ) { b, any ->
                    onRequestResponseTask(b, any) {
                        val userDetails = any as? UserResponse<User>
                        val user = userDetails?.data
                        user?.loggedIn = true
                        userViewModel.currentUser = user
                        val res = userViewModel.saveUser
                        val loginIntent = Intent(requireContext(), DashboardActivity::class.java)
                        Log.i("$this", "res ${res.size}")
                        startActivity(loginIntent)
                        requireActivity().finish()
                    }
                }
            }
        }

    }
}