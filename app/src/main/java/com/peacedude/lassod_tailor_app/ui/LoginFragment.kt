package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_phone_signup.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.colorPrimary)
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


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar =
            login_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(login_appbar)


        toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        buttonTransactions({
            loginBtn = login_page_btn.findViewById(R.id.btn)
            progressBar = login_page_btn.findViewById(R.id.progress_bar)
        }, {
            loginBtn.text = getString(R.string.login)
        })
        userViewModel.lastLoginForm.toString()

        login_use_email_tv.setOnClickListener {
            login_vf.showNext()
        }
        login_use_phone_tv.setOnClickListener {
            login_vf.showPrevious()
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

        if (login_vf[0].isShown) {
            validateAndLogin(
                login_phone_number_et, "phone"
            ) { phoneNumber, password ->
                loginWithPhoneNumber(phoneNumber, password)
            }
        } else {
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
                "res $user \ntoken ${user.role}\ntoken 2${user.token}"
            )
            loginWithGoogle(user)
        }
    }

    private fun loginWithGoogle(user: User) {
        val googleAuthHeader = "$bearer ${user.token.toString()}"
        val req = userViewModel.loginWithGoogle(googleAuthHeader)
        i(title, "header ${user.token}")

        observeRequest<User>(req, progressBar, loginBtn, false, {userDetails->
            val remoteDetails = userDetails.data
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
        },{err->
            i(title, "LoginWithPhoneError $err")
        })

    }




    private inline fun validateAndLogin(
        editText: EditText,
        filter: String,
        action: (String, String) -> Unit
    ) {
        var input = editText.text.toString().trim()
        val passwordString = login_password_et.text.toString()
        val emptyEditText = IsEmptyCheck(editText, login_password_et)
        var v: String? = ""
        if (filter == EMAIL) {
            v = IsEmptyCheck.fieldsValidation(email = input, password = passwordString)
        } else if (filter == PHONE) {


            val dialCode = login_ccp.selectedCountryCode
            var phoneNumber = editText.text.toString().trim()
            val firstZeroPattern = Regex("""[0]\d+""")
            val checkFirstZero = firstZeroPattern.matches(phoneNumber)
            if(checkFirstZero){
                phoneNumber = phoneNumber.removePrefix("0")
                editText.setText(phoneNumber)
            }
            input = dialCode + phoneNumber
            v = IsEmptyCheck.fieldsValidation(phone_number = input, password = passwordString)
        }

        when {
            emptyEditText != null -> {
                emptyEditText.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${emptyEditText.tag} is empty", Gravity.BOTTOM)
            }
            v != null -> {
                requireActivity().gdToast("$v is invalid", Gravity.BOTTOM)
            }
            else -> {
                action(input, passwordString)
            }
        }

    }

    private fun loginWithPhoneNumber(phoneNumberOrEmail: String, passwordString: String) {
        val req = userViewModel.loginUserRequest(phoneNumberOrEmail, passwordString)
        observeRequest<User>(req, progressBar, loginBtn, false, {userDetails->
            val user = userDetails.data
            user?.loggedIn = true
            userViewModel.currentUser = user
            userViewModel.lastLoginForm = PHONE
//                val res = userViewModel.saveUser
            goto(DashboardActivity::class.java)
            requireActivity().finish()
        },{err->
            requireActivity().gdErrorToast(err, Gravity.BOTTOM)
            i(title, "LoginWithPhoneError $err")
        })

    }

    private fun loginWithEmail(email: String, passwordString: String) {
        val req = userViewModel.loginWithEmailOrPhoneNumber(email, passwordString)
        observeRequest<User>(req, progressBar, loginBtn, false, {userDetails->
            val user = userDetails.data
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
        },{err->
            requireActivity().gdErrorToast(err, Gravity.BOTTOM)
            i(title, "LoginWithEmailError $err")
        })

    }
}
