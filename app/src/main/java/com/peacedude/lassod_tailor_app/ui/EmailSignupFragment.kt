package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
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
import kotlinx.android.synthetic.main.drawerlayout_header.*
import kotlinx.android.synthetic.main.fragment_email_signup.*
import validatePasswordAndAdvise
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [EmailSignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailSignupFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
    }

    //Get logged-in user
    private val currentUser: User? by lazy {
        userViewModel.currentUser
    }

    private val header by lazy {
        userViewModel.header
    }

    private val loginAdviseText: String by lazy {
        getString(R.string.have_an_account)
    }
    private var spannableTextColor = 0;
    private val spannableString: SpannableString by lazy {
        loginAdviseText.setAsSpannable()
    }

    private lateinit var emailSignupBtn: Button
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }
    private val arg: EmailSignupFragmentArgs by navArgs()
    val user by lazy { arg.newUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_signup, container, false)
    }

    override fun onResume() {
        super.onResume()
        val toolbar = email_signup_appbar.findViewById<Toolbar>(R.id.reusable_toolbar)
        val navController = Navigation.findNavController(email_signup_appbar)

        setupToolbarAndNavigationUI(toolbar, navController)

        initEnterKeyToSubmitForm(password_field) { signupRequest() }
        setupCategorySpinner(
            requireContext(),
            email_signup_category_spinner,
            R.array.categories_array
        )
        email_field.setText(user?.email)
        val backgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            emailSignupBtn = email_signup_btn.findViewById(R.id.btn)
            progressBar = email_signup_btn.findViewById(R.id.progress_bar)
            emailSignupBtn.background = backgroundDrawable
            emailSignupBtn.text = getString(R.string.signup)
            emailSignupBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }, {

            emailSignupBtn.setOnClickListener {
                signupRequest()
            }
        })

        if(user != null){
            email_field.hide()
        }

        password_field.doOnTextChanged { text, start, count, after ->
            if (text != null) {
                validatePasswordAndAdvise(text, email_signup_password_standard_tv)
            }
            return@doOnTextChanged
        }

        setupLoginSpannableString()
    }

    private fun signupRequest() {
        val email = email_field.text.toString().trim()
        val passwordString = password_field.text.toString().trim()
        val checkForEmpty =
            IsEmptyCheck(email_field, password_field)
        val validation = IsEmptyCheck.fieldsValidation(null, passwordString)

        when {
            checkForEmpty != null -> {
                checkForEmpty.error = getString(R.string.field_required)
                requireActivity().gdErrorToast(
                    "${resources.getResourceEntryName(
                        checkForEmpty.id
                    )} is empty", Gravity.BOTTOM
                )

            }
            validation != null -> requireActivity().gdErrorToast(
                "$validation is invalid",
                Gravity.BOTTOM
            )
            user != null -> {

                if (email != user?.email) {
                    requireActivity().gdErrorToast(
                        "Authenticated email address must not be changed",
                        Gravity.BOTTOM
                    )
                    email_field.error = user?.email
                } else {
                    val googleAuthHeader = "$bearer ${user?.token.toString()}"
                    user?.password = passwordString
                    var req = userViewModel.registerUser(googleAuthHeader, user)
                    i(title, "header ${user?.token}")
                    requireActivity().requestObserver(
                        progressBar,
                        emailSignupBtn,
                        req
                    ) { bool, result ->
                        onRequestResponseTask(bool, result) {
                            val userDetails = result as? UserResponse<User>
                            val user = userDetails?.data
                            currentUser?.loggedIn = true
                            currentUser?.token = user?.token
                            userViewModel.currentUser = currentUser
//                            val res = userViewModel.saveUser
                            val loginIntent =
                                Intent(requireContext(), ProfileActivity::class.java)
                            i(
                                "$this",
                                "res $currentUser \ntoken ${user?.token}\ntoken 2${currentUser?.token}"
                            )
                            startActivity(loginIntent)
                            requireActivity().finish()

                        }
                    }

                }
            }
            else -> {
                val category = arg.category
                val newUser = User("", "", "", category, "")
                newUser.email = email
                newUser.password = passwordString
                var req = userViewModel.registerUser(newUser)
                val observer =
                    requireActivity().observeRequest(req, progressBar, emailSignupBtn)
                observer.observe(viewLifecycleOwner, Observer {
                    val (bool, result) = it
                    onRequestResponseTask(bool, result) {
                        requireActivity().gdToast(
                            "Check your email for verification",
                            Gravity.BOTTOM
                        )
//                        goto(R.id.loginFragment)
                        //                                requireActivity().gdToast(getString(R.string.check_email), Gravity.BOTTOM)
                        Log.i(title, getString(R.string.check_email))
                    }
                })
            }
        }
    }

    private fun setupLoginSpannableString() {
        spannableTextColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = loginAdviseText.length
        val start = 17
        setupSpannableLinkAndDestination(
            email_signup_login_tv,
            spannableTextColor,
            spannableString,
            start,
            textLen
        ) {
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.loginFragment)
            }
        }
    }


}