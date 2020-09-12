package com.peacedude.lassod_tailor_app.ui

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_email_signup.*
import kotlinx.android.synthetic.main.fragment_phone_signup.*
import kotlinx.android.synthetic.main.fragment_signup_choices.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
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

    private val loginAdviseText: String by lazy {
        getString(R.string.have_an_account)
    }
    private var spannableTextColor = 0;
    private val spannableString: SpannableString by lazy {
        loginAdviseText.setAsSpannable()
    }

    private lateinit var emailSignupBtn: Button
    private lateinit var progressBar:ProgressBar
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }
    private val arg:EmailSignupFragmentArgs by navArgs()
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_signup, container, false)
    }

    override fun onResume() {
        super.onResume()

        Log.i(title, "email ${user?.email}")
        
        email_signup_phone_number_et.setText(user?.email)
        val backgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            emailSignupBtn = email_signup_btn.findViewById(R.id.btn)
            progressBar = email_signup_btn.findViewById(R.id.progress_bar)
            emailSignupBtn.background = backgroundDrawable
            emailSignupBtn.text = getString(R.string.signup)
            emailSignupBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }, {

            emailSignupBtn.setOnClickListener {
                if (user != null) {
                    CoroutineScope(Main).launch {
                        var req = userViewModel.registerUser(user)
                        val observer = requireActivity().observeRequest(req, progressBar, emailSignupBtn)
                        observer.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                            val (bool, result) = it
                            onRequestResponseTask(bool, result){
                                requireActivity().gdToast(getString(R.string.check_email), Gravity.BOTTOM)
                                Log.i(title, getString(R.string.check_email))
                            }
                        })
                    }

                }
            }
        })

        setupLoginSpannableString()
    }

    private fun setupLoginSpannableString() {
        spannableTextColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = loginAdviseText.length
        val start = 17
        setupSpannableLinkAndDestination(
            loginAdviseText,
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