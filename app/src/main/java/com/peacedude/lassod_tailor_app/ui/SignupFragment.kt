package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_signup.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : DaggerFragment() {

    val title: String by lazy {
        getName()
    }
    private lateinit var confirmBtn: Button
    lateinit var resendCodeBtn: Button
    private lateinit var confirmProgressBar:ProgressBar
    private lateinit var resendProgressBar:ProgressBar
    lateinit var continueBtn: Button
    private lateinit var progressBar:ProgressBar
    private val loginAviseText: String by lazy {
        getString(R.string.have_an_account)
    }
    private val spannableString: SpannableString by lazy {
        loginAviseText.setAsSpannable()
    }
    private var textColor = 0;
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.custom_dialog)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        continueBtnTransaction()

        initEnterKeyToSubmitForm(password_edittext) { signupRequest() }

        setupToolbarAndNavigationUI()

        setupLoginSpannableString()

        setupCategorySpinner()

        dialogBtnsAndProgressBarsSetup()
    }

    private fun dialogBtnsAndProgressBarsSetup() {
        //Drawable background for confirm button
        val confirmBtnBackgroundDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.rounded_corner_background_primary
        )
        //Drawable background for resend code button
        val resendCodeBtnBackgroudDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_trans)
        buttonTransactions({
            //Get included view for confirm button
            val includedViewForConfirmBtn = dialog.findViewById<View>(R.id.confirm_btn)
            //Get included view for resend code button
            val includedViewForResendBtn = dialog.findViewById<View>(R.id.resend_code_btn)
            //Set confirm button
            confirmBtn = includedViewForConfirmBtn.findViewById(R.id.btn)
            //Set confirm button background
            confirmBtn.background = confirmBtnBackgroundDrawable
            //Set resend code button
            resendCodeBtn = includedViewForResendBtn.findViewById(R.id.btn)
            //Set resend code button background
            resendCodeBtn.background = resendCodeBtnBackgroudDrawable
            //Set resend code button text color
            resendCodeBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            //Set resend code button progress bar
            resendProgressBar = includedViewForResendBtn.findViewById(R.id.progress_bar)
            //Set confirm button progress bar
            confirmProgressBar = includedViewForConfirmBtn.findViewById(R.id.progress_bar)


        }, {
            confirmBtn.text = getString(R.string.confirm)
            resendCodeBtn.text = getString(R.string.resend_code)
        })
    }

    private fun setupCategorySpinner() {
        val adapterState =
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayListOf("User", "Tailor")
            )

        signup_category_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                (parentView.getChildAt(0) as TextView).setTextColor(Color.WHITE)
            }
        }
        signup_category_spinner.adapter = adapterState
    }

    private fun setupLoginSpannableString() {
        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = loginAviseText.length
        val start = 17
        setupSpannableLinkAndDestination(
            loginAviseText,
            login_text,
            textColor,
            spannableString,
            start,
            textLen
        ) {
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.loginFragment)
            }
        }
    }

    private fun setupToolbarAndNavigationUI() {
        val toolbar = signup_appbar.findViewById<Toolbar>(R.id.reusable_toolbar)
        val navController = Navigation.findNavController(signup_appbar)
        NavigationUI.setupWithNavController(toolbar, navController)
    }

    private fun continueBtnTransaction() {
        val continueBtnBackgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)

        buttonTransactions({
            continueBtn = continue_btn.findViewById(R.id.btn)
            progressBar = continue_btn.findViewById(R.id.progress_bar)

            continueBtn.setOnClickListener {
                signupRequest()
            }
        }, {
            continueBtn.background = continueBtnBackgroundDrawable
            continueBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            continueBtn.text = getString(R.string.continue_text)

        })
    }

    /**
     * Sign up request
     *
     */
    private fun signupRequest() {

        val firstName = first_name_edittext.text.toString().trim()
        val lastName = last_name_edittext.text.toString().trim()
        val otherName = other_name_edittext.text.toString().trim().toLowerCase()
        val category = signup_category_spinner.selectedItem as String
        val phoneNumber = signup_phone_number_edittext.text.toString().trim()
        val passwordString = password_edittext.text.toString().trim()
        val user = User(firstName, lastName, otherName, category, phoneNumber, passwordString)

//        val u = storageRequest.saveData(user,"u")
//        Log.i(title, "reg1 $u")
//        userViewModel.registerFormData = user

//        Log.i(title, "reg2 ${userViewModel.registerFormData}")
        val checkForEmpty =
            IsEmptyCheck(first_name_edittext, last_name_edittext, other_name_edittext, signup_phone_number_edittext, password_edittext)
        val validation = IsEmptyCheck.fieldsValidation(null, passwordString)
        when {
            checkForEmpty != null -> {
                checkForEmpty.error = getString(R.string.field_required)
                requireActivity().gdErrorToast("${resources.getResourceEntryName(checkForEmpty.id)} is empty", Gravity.BOTTOM)

            }
            validation != null -> requireActivity().gdErrorToast("$validation is invalid", Gravity.BOTTOM)

            else -> {
                val userData = User(firstName, lastName, otherName, category, phoneNumber, passwordString)

                val request = userViewModel.registerUser(userData)
                val response = observeRequest(request, progressBar, continueBtn)
                response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    val (bool, result) = it
                    onRequestResponseTask(
                        bool,
                        result
                    )
                })
            }
        }


    }

    /**
     * Handles sign-up request live response
     *
     * @param bool
     * @param result
     */
    private fun onRequestResponseTask(
        bool: Boolean,
        result: Any?
    ) {
        when (bool) {
            true -> {
                val res = result as UserResponse
                requireActivity().gdToast(res.message.toString(), Gravity.BOTTOM)
//                val clearRegister = storageRequest.clearByKey<User>("u")
                var code = ""
                val pinEditText = dialog.findViewById<PinEntryEditText>(R.id.txt_pin_entry)
                val phoneNumber = signup_phone_number_edittext.text.toString().trim()
                pinEditText.setOnPinEnteredListener {
                    code += it
                }
                confirmBtn.setOnClickListener {

//                    userViewModel.activateUser(phoneNumber, code)
                    requireActivity().gdToast("$code $phoneNumber", Gravity.BOTTOM)
                }
                dialog.show()

//                goto(R.id.loginFragment)
                Log.i(title, "result of registration ${res.data}")
//                Log.i(title, "clearedRegister $clearRegister")
            }
            else -> Log.i(title, "error $result")
        }
    }

}