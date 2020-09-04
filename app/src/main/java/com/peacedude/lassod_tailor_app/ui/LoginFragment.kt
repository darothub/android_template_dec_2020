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
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
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

    private lateinit var loginBtn: Button

    val title: String by lazy {
        getName()
    }
    private lateinit var progressBar: ProgressBar
    private val newUserText: String by lazy {
        getString(R.string.new_user)
    }
    private val spannableString: SpannableString by lazy {
        newUserText.setAsSpannable()
    }
    private var textColor = 0;
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
            loginRequest()

        }
        initEnterKeyToSubmitForm(login_password_edittext) { loginRequest() }

        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = newUserText.length
        val start = 10
        setupSpannableLinkAndDestination(newUserText, new_user, textColor, spannableString, start, textLen){
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.signupFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            goto(R.id.homeFragment)
        }

    }


    fun loginRequest() {
        val phoneNumber = login_phone_number_edittext.text.toString().trim()
        val passwordString = login_password_edittext.text.toString()

        val checkForEmpty = IsEmptyCheck(login_phone_number_edittext, login_phone_number_edittext)

        when {
            checkForEmpty != null -> {
                checkForEmpty.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${resources.getResourceEntryName(checkForEmpty.id)} is empty", Gravity.BOTTOM)
            }
            else -> {
                val request = userViewModel.loginUserRequest(
                    phoneNumber, passwordString
                )
                val response = requireActivity().observeRequest(request, progressBar, loginBtn)
                response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    val (bool, result) = it
                    onRequestResponseTask(bool, result){
                        val userDetails = result as? UserResponse
                        val user = userDetails?.data
                        user?.loggedIn = true
                        userViewModel.currentUser = user
                        val res = userViewModel.saveUser
                        val loginIntent = Intent(requireContext(), ProfileActivity::class.java)
//                        loginIntent.putExtra("token", userDetails?.data?.token)
                        Log.i(title, "res ${res.size}")
                        startActivity(loginIntent)
                        requireActivity().finish()
                    }
                })
            }
        }

    }




}