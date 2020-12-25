package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_phone_signup.*
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPassword : DaggerFragment(R.layout.fragment_forgot_password) {
    val title: String = getName()
    private lateinit var progressBar: ProgressBar
    lateinit var sendBtn: Button

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
        arguments?.let {

        }
    }


    override fun onStart() {
        super.onStart()

        val toolbar = forgot_password_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
        val viewsTitle =
            forgot_password_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        viewsTitle.text = getString(R.string.forgot_password_str)
        val navController = Navigation.findNavController(toolbar)
        toolbar.setNavigationOnClickListener {
            changeStatusBarColor(R.color.colorPrimary)
            findNavController().popBackStack()
        }

        buttonTransactions({
            progressBar = forgot_password_include_btn.findViewById(R.id.progress_bar)
            sendBtn = forgot_password_include_btn.findViewById(R.id.btn)
            sendBtn.apply {
                background.changeBackgroundColor(requireContext(), R.color.colorPrimary)
                text = getString(R.string.send)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            }

        }, {
            sendBtn.setOnClickListener {
                if(forgot_password_vf[0].isShown){
                    forgotPasswordRequestWithPhone()
                }
                else{
                    forgotPasswordRequest()
                }

            }

            forgot_password_use_email_tv.setOnClickListener {
                forgot_password_vf.showNext()
            }
            forgot_password_use_phone_tv.setOnClickListener {
                forgot_password_vf.showPrevious()
            }

        })
        initEnterKeyToSubmitForm(forgot_password_email_et) { forgotPasswordRequest() }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    private fun forgotPasswordRequest() {
        val field = forgot_password_email_et.text.toString().trim()
        val emptyEditText = IsEmptyCheck(forgot_password_email_et)
        val validationResult = IsEmptyCheck.fieldsValidation(email = field)

        when {
            emptyEditText != null -> {
                emptyEditText.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${emptyEditText.tag} is empty", Gravity.BOTTOM)
            }
            validationResult != null -> requireActivity().gdErrorToast(
                "$validationResult is an invalid email",
                Gravity.BOTTOM
            )
            else -> {

                val request = authViewModel.forgetPassword(field)
                observeRequest<String>(request, progressBar, sendBtn, false, {
                    Log.i(title, "${it.message}")
                }, { err ->
                    Log.i(title, "ForgotPasswordError $err")
                })

            }
        }
    }

    private fun forgotPasswordRequestWithPhone() {
        val emptyEditText = IsEmptyCheck(forgot_password_phone_et)
        val dialCode = forgot_password_ccp.selectedCountryCode
        var phoneNumber = forgot_password_phone_et.text.toString().trim()
        val phonePattern = Regex("""\d{10,13}""")
        val firstZeroPattern = Regex("""[0]\d+""")
        val checkFirstZero = firstZeroPattern.matches(phoneNumber)
        val checkPhoneStandard = phonePattern.matches(phoneNumber)


        when {
            emptyEditText != null -> {
                emptyEditText.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${emptyEditText.tag} is empty", Gravity.BOTTOM)
            }
            !checkPhoneStandard -> requireActivity().gdErrorToast(
                "Invalid phone number",
                Gravity.BOTTOM
            )
            checkFirstZero -> {
                phoneNumber = phoneNumber.removePrefix("0")
                phone_signup_phone_number_et.setText(phoneNumber)
            }
            else -> {
                val field = dialCode + phoneNumber
                val request = authViewModel.forgetPassword(field)
                observeRequest<String>(request, progressBar, sendBtn, false, {
                    Log.i(title, "${it.message}")
                }, { err ->
                    requireActivity().gdErrorToast(
                        err,
                        Gravity.BOTTOM
                    )
                    Log.i(title, "ForgotPasswordError $err")
                })

            }
        }
    }

}
