package com.peacedude.lassod_tailor_app.ui.profile

import IsEmptyCheck
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_email_signup.*
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.android.synthetic.main.fragment_security.*
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SecurityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecurityFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
    }

    // Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    val header by lazy {
        authViewModel.header
    }
    private lateinit var saveChangesBtn: Button
    private lateinit var saveChangesProgressbar: ProgressBar

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        security_fragment_phone_number_value_tv.text = currentUser?.phone

        buttonTransactions(
            {
                saveChangesBtn = security_fragment_btn_include.findViewById(R.id.btn)
                saveChangesProgressbar = security_fragment_btn_include.findViewById(R.id.progress_bar)
                val saveChangesBtnBackground = saveChangesBtn.background
                saveChangesBtnBackground.changeBackgroundColor(requireContext(), R.color.colorPrimary)
                saveChangesBtn.background = saveChangesBtnBackground
                saveChangesBtn.text = getString(R.string.save_changes)
            },
            {
            }
        )

        security_fragment_old_password_et.doOnTextChanged { text, start, count, after ->
            if (text != null) {
                val b = security_fragment_old_password_et.background
                val passwordPattern = Regex("""^[a-zA-Z0-9@$!%*#?&]{6,}$""")
                val matchedPassword = passwordPattern.matches(text)
                if (!matchedPassword) {
                    security_fragment_old_password_et.error = "Password must be at least 8 characters long"
                } else {
                }
            }
            return@doOnTextChanged
        }

        saveChangesBtn.setOnClickListener {
            val oldPassword = security_fragment_old_password_et.text.toString()
            val newPassword = security_fragment_new_password_et.text.toString()
            val emptyeditText =
                IsEmptyCheck(security_fragment_old_password_et, security_fragment_new_password_et)
            when {
                emptyeditText != null -> {
                    emptyeditText.error = getString(R.string.field_required)
                    requireActivity().gdErrorToast("${emptyeditText.tag} is empty", Gravity.BOTTOM)
                }
                oldPassword != newPassword -> {
                    security_fragment_new_password_et.error =
                        getString(R.string.password_not_matched)
                    requireActivity().gdErrorToast(
                        getString(R.string.password_not_matched),
                        Gravity.BOTTOM
                    )
                }
                else -> {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        supervisorScope {
//                            val changePasswordRequest = async {
//                                authViewModel.changePassword(
//                                    header.toString(),
//                                    oldPassword,
//                                    newPassword
//                                )
//                            }
//                            changePasswordRequest.await()
//                                .collect {
//                                    onFlowResponse<UserResponse<NothingExpected>>(saveChangesBtn, saveChangesProgressbar, response = it) {
//                                        requireActivity().gdToast(it?.message.toString(), Gravity.BOTTOM)
//                                    }
//                                }
//                            try {
//
//
//                            } catch (e: Exception) {
//                                i(title, "Change password error data flow ${e.message}")
//                            }
//                        }
//                    }
                }
            }
        }
    }
}
