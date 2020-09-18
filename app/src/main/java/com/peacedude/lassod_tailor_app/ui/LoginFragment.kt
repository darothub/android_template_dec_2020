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
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.facebook.shimmer.ShimmerFrameLayout
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
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
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

        val toolbar = login_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(login_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)

        loginBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))


        loginBtn.setOnClickListener {
            validateAndLogin()
        }
        initEnterKeyToSubmitForm(login_password_edittext) { validateAndLogin() }

        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        googleBtnTextColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val textLen = newUserText.length
        val start = 10
        setupSpannableLinkAndDestination(new_user, textColor, spannableString, start, textLen){
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.signupFragment)
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
        val someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->

                if (result.resultCode == Activity.RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    task.addOnCompleteListener {
                        if(it.isSuccessful){
                            val account: GoogleSignInAccount? =
                                it.getResult(ApiException::class.java)

                            val idToken = it.result?.idToken
                            val email = account?.email
                            val lastName = account?.familyName
                            val firstName = account?.givenName
                            val otherName = account?.displayName
                            val imageUrl = account?.photoUrl

                            val newUser = User(firstName, lastName, otherName, "", "")
                            newUser.email = email
                            newUser.token = idToken
                            Log.i(title, "idToken $idToken")
                            requireActivity().gdToast("Aunthentication successful", Gravity.BOTTOM)
//                            val action = SignupChoicesFragmentDirections.actionSignupChoicesFragmentToEmailSignupFragment()
//                            action.newUser = newUser
//                            goto(action)

                            Log.i(title, "token $firstName")
//                           requireActivity().startActivity(Intent(requireActivity(), ProfileActivity::class.java))
                        }
                    }
                }

            }
        )
        login_google_sign_in_button.setOnClickListener {
            val intent = mGoogleSignInClient.getSignInIntent()
            someActivityResultLauncher.launch(intent)
        }
    }


    private fun validateAndLogin() {
        val phoneNumber = login_phone_number_edittext.text.toString().trim()
        val passwordString = login_password_edittext.text.toString()

        val checkForEmpty = IsEmptyCheck(login_phone_number_edittext, login_phone_number_edittext)

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
                        val loginIntent = Intent(requireContext(), ProfileActivity::class.java)
                        Log.i("$this", "res ${res.size}")
                        startActivity(loginIntent)
                        requireActivity().finish()
                    }
                }
            }
        }

    }

//    fun request(req:(userViewModel:UserViewModel)->LiveData<ServicesResponseWrapper<ParentData>>) {
//
//        val request  = req(userViewModel)
//        val response = requireActivity().observeRequest(request, progressBar, loginBtn)
//        response.observe(viewLifecycleOwner, Observer {
//            val (bool, result) = it
//            onRequestResponseTask(bool, result) {
//                val userDetails = result as? UserResponse
//                val user = userDetails?.data
//                user?.loggedIn = true
//                userViewModel.currentUser = user
//                val res = userViewModel.saveUser
//                val loginIntent = Intent(requireContext(), ProfileActivity::class.java)
//                Log.i(title, "res ${res.size}")
//                startActivity(loginIntent)
//                requireActivity().finish()
//            }
//        })
//    }


}