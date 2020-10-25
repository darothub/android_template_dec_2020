package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.*
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
        arguments?.let {

        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
//    }

    override fun onStart() {
        super.onStart()

        val toolbar = forgot_password_appbar.findViewById<Toolbar>(R.id.reusable_toolbar)
        val navController = Navigation.findNavController(forgot_password_appbar)

        setupToolbarAndNavigationUI(toolbar, navController)
        buttonTransactions({
            sendBtn = forgot_password_include_btn.findViewById(R.id.btn)
            sendBtn.text = getString(R.string.send)
            progressBar = forgot_password_include_btn.findViewById(R.id.progress_bar)
            sendBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        }, {
            sendBtn.setOnClickListener {
                forgotPasswordRequest()
            }

        })
        initEnterKeyToSubmitForm(forgot_password_email_et) { forgotPasswordRequest() }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun forgotPasswordRequest() {
        val field = forgot_password_email_et.text.toString().trim()
        val emptyEditText = IsEmptyCheck(forgot_password_email_et)
        val validationResult = IsEmptyCheck.fieldsValidation(email = field)

        when {
            emptyEditText != null ->{
                emptyEditText.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${emptyEditText.tag} is empty", Gravity.BOTTOM)
            }
            validationResult != null -> requireActivity().gdErrorToast(
                "$validationResult is an invalid email",
                Gravity.BOTTOM
            )
            else -> {

                val request = authViewModel.forgetPassword(field)
                val response = requireActivity().observeRequest(request, progressBar, sendBtn)
                response.observe(viewLifecycleOwner, Observer {
                    val (bool, result) = it
                    onRequestResponseTask<String>(bool, result) {
                        Log.i(title, "Request successful action needed")
                    }
                })
            }
        }
    }

}