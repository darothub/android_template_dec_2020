package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.utils.bearer
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_reset_password.*
import validatePasswordAndAdvise
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : DaggerFragment() {

    val title by lazy {
        getName()
    }

    //Get logged-in user
    val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    val token by lazy {
        currentUser?.token
    }
    val header by lazy {
        "$bearer $token"
    }
    private lateinit var resetPasswordProgressabar: ProgressBar
    private lateinit var resetBtn: Button
    private val arg: ResetPasswordFragmentArgs by navArgs()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onResume() {
        super.onResume()
        val resendCodeBtnBackgroudDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_trans)
        val signupBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            resetBtn = reset_password_btn.findViewById(R.id.btn)
            resetBtn.text = getString((R.string.send))
            resetBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            resetPasswordProgressabar = reset_password_btn.findViewById(R.id.progress_bar)


        }, {
            resetBtn.setOnClickListener {
                resetPasswordRequest()
            }

        })

        initEnterKeyToSubmitForm(reset_password_conf_password_et) { resetPasswordRequest() }
        reset_password_conf_password_et.doOnTextChanged { text, start, count, after ->
            if (text != null) {
                validatePasswordAndAdvise(text, reset_password_standard_tv)
            }
            return@doOnTextChanged
        }


        val token = arg.token
        Log.i(title, token.toString())
    }

    private fun resetPasswordRequest() {
        val password = reset_password_password_et.text.toString().trim()
        val cpassword = reset_password_conf_password_et.text.toString().trim()
        val validation = IsEmptyCheck.fieldsValidation(null, password)
        if (validation != null) {
            requireActivity().gdErrorToast("$validation is invalid", Gravity.BOTTOM)
        } else if (password != cpassword) {
            requireActivity().gdErrorToast("passwords do not match", Gravity.BOTTOM)
        } else {
            val req = requireActivity().request(
                resetPasswordProgressabar,
                resetBtn,
                authViewModel,
                authViewModel.resetPassword(header, password, cpassword)
            ) { b, any ->
                onRequestResponseTask(b, any) {
                    val userDetails = any as? UserResponse<String>
                    val user = userDetails?.data
                    requireActivity().gdErrorToast(user.toString(), Gravity.BOTTOM)
                }
            }
        }
    }

}