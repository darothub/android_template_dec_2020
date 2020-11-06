package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.auth0.android.jwt.JWT
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.utils.bearer
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.Serializable
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val PHONE = "phone"
const val EMAIL = "email"

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
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var observer: StartActivityForResults

    private val leftAnimation: Animation? by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.left_animation)
    }
    private val rightAnimation: Animation? by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = StartActivityForResults(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)
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

        val toolbar =
            login_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(login_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)
        buttonTransactions({
            loginBtn = login_page_btn.findViewById(R.id.btn)
            progressBar = login_page_btn.findViewById(R.id.progress_bar)
        }, {
            loginBtn.text = getString(R.string.login)
        })
        var lastLoginForm = userViewModel.lastLoginForm.toString()

        if (lastLoginForm == PHONE) {
            login_phone_number_input.show()
            login_with_email_tv.show()
            login_email_input.invisible()
            login_with_phone_tv.invisible()
        } else {
            login_email_input.show()
            login_with_phone_tv.show()
            login_phone_number_input.invisible()
            login_with_email_tv.invisible()
        }

        login_with_email_tv.setOnClickListener {
            login_email_input.animation = leftAnimation
            login_phone_number_input.animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_email_input.show()
            login_phone_number_input.invisible()

            login_with_phone_tv.show()
            login_with_phone_tv.animation = leftAnimation
            login_with_email_tv.animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_with_email_tv.invisible()

        }
        login_with_phone_tv.setOnClickListener {
            login_phone_number_input.animation = leftAnimation
            login_email_input.animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_phone_number_input.show()
            login_email_input.invisible()

            login_with_email_tv.animation = leftAnimation
            login_with_phone_tv.animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.right_move_out)
            login_with_email_tv.show()
            login_with_phone_tv.invisible()

        }


        loginBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))


        loginBtn.setOnClickListener {
            emailOrPhoneNumberLoginRequest()
        }
        initEnterKeyToSubmitForm(login_password_et) {
            emailOrPhoneNumberLoginRequest()
        }

        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        googleBtnTextColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val textLen = newUserText.length
        val start = 10

        newUserText.setSpannableString(
            requireContext(),
            R.color.colorAccent,
            new_user,
            start,
            textLen
        ) {
            goto(R.id.signupChoicesFragment)
        }
        val googleTextLen = googleBtnText.length
        googleBtnText.setSpannableString(
            requireContext(),
            R.color.colorPrimary,
            login_google_sign_in_button,
            0,
            googleTextLen
        ) {
            authWithGoogle()
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            goto(R.id.homeFragment)
        }

        forgot_password_tv.setOnClickListener {
            goto(R.id.forgotPassword)
        }

    }

    private fun emailOrPhoneNumberLoginRequest() {
        val phoneInputLayout = login_phone_number_input.show()
        val emailInputLayout = login_email_input.show()
        //            requireActivity().gdToast("phone $phoneInputLayout email $emailInputLayout", Gravity.BOTTOM)

        if (phoneInputLayout) {
            login_email_input.invisible()
            validateAndLogin(
                login_phone_number_et, "phone"
            ) { phoneNumber, password ->
                loginWithPhoneNumber(phoneNumber, password)
            }
        } else {
            login_phone_number_input.invisible()
            validateAndLogin(login_email_address_et, "email") { email, password ->
                loginWithEmail(email, password)
            }
        }
    }

    private fun authWithGoogle() {
        val intent = mGoogleSignInClient.signInIntent
        observer.launchIntentToSignIn(intent, viewLifecycleOwner) { user ->
            i(
                "$this",
                "res $user \ntoken ${user.role}\ntoken 2${user?.token}"
            )
            loginWithGoogle(user)
        }
    }

    private fun loginWithGoogle(user: User) {
        val googleAuthHeader = "$bearer ${user.token.toString()}"
        val req = userViewModel.loginWithGoogle(googleAuthHeader)
        i(title, "header ${user.token}")
        requestObserver(null, null, req) { bool, result ->
            onRequestResponseTask<User>(bool, result) {

                val userDetails = result as? UserResponse<User>
                val remoteDetails = userDetails?.data
                user.loggedIn = true
                user.token = remoteDetails?.token

                val payloadString = user<User>(user.token.toString())
                user.role = payloadString?.category
                user.category = payloadString?.category
                userViewModel.currentUser = user
                //                            val res = userViewModel.saveUser
                val loginIntent =
                    Intent(requireContext(), DashboardActivity::class.java)
                i(
                    title,
                    "res $user \nCategory ${payloadString?.category}\ntoken 2${remoteDetails}"
                )
                requireActivity().gdToast(getString(R.string.you_are_signed) + " ${user.email}", Gravity.BOTTOM)
                startActivity(loginIntent)
                requireActivity().finish()
            }
        }
    }




    private inline fun validateAndLogin(
        editText: EditText,
        filter: String,
        action: (String, String) -> Unit
    ) {
        val input = editText.text.toString().trim()
        val passwordString = login_password_et.text.toString()
        val emptyEditText = IsEmptyCheck(editText, login_password_et)
        var v: String? = ""
        if (filter == "email") {
            v = IsEmptyCheck.fieldsValidation(email = input, password = passwordString)
        } else if (filter == "phone") {
            v = IsEmptyCheck.fieldsValidation(phone_number = input, password = passwordString)
        }

        when {
            emptyEditText != null -> {
                emptyEditText.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${emptyEditText.tag} is empty", Gravity.BOTTOM)
            }
            v != null -> {
                if (filter == "phone") {
                    requireActivity().gdToast("$v must be 13 character long", Gravity.BOTTOM)
                } else {
                    requireActivity().gdToast("$v is invalid", Gravity.BOTTOM)
                }
            }
            else -> {
                action(input, passwordString)
            }
        }

    }

    private fun loginWithPhoneNumber(phoneNumberOrEmail: String, passwordString: String) {
        val req = userViewModel.loginUserRequest(phoneNumberOrEmail, passwordString)
        requestObserver(progressBar, loginBtn, req) { b, any ->
            onRequestResponseTask<User>(b, any) {
                val userDetails = any as? UserResponse<User>
                val user = userDetails?.data
                user?.loggedIn = true
                userViewModel.currentUser = user
                userViewModel.lastLoginForm = PHONE
//                val res = userViewModel.saveUser
                val loginIntent = Intent(requireContext(), DashboardActivity::class.java)
//                Log.i("$this", "res ${res.size}")
                startActivity(loginIntent)
                requireActivity().finish()
            }
        }
    }

    private fun loginWithEmail(email: String, passwordString: String) {
        val req = userViewModel.loginWithEmailOrPhoneNumber(email, passwordString)
        requestObserver(progressBar, loginBtn, req) { b, result ->
            onRequestResponseTask<User>(b, result) {

                val userDetails = result as? UserResponse<User>
                val user = userDetails?.data
                user?.loggedIn = true
                userViewModel.currentUser = user
                if(login_fragment_remember_login_choice_cb.isChecked){
                    userViewModel.lastLoginForm = EMAIL
                }
                else{
                    userViewModel.lastLoginForm = PHONE
                }

//                val res = userViewModel.saveUser
                val loginIntent = Intent(requireContext(), DashboardActivity::class.java)
//                Log.i("$this", "res ${res.size}")
                requireActivity().gdToast(getString(R.string.you_are_signed) + " $email", Gravity.BOTTOM)
                startActivity(loginIntent)
                requireActivity().finish()
            }
        }
    }
}
