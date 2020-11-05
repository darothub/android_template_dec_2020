package com.peacedude.lassod_tailor_app.ui

import IsEmptyCheck
import android.annotation.SuppressLint
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
import androidx.navigation.fragment.findNavController
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
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_phone_signup.*
import validatePasswordAndAdvise

import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [PhoneSignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneSignupFragment : DaggerFragment() {
    val title: String = getName()
    lateinit var signupPhoneBtn: Button
    private lateinit var confirmBtn: Button
    lateinit var resendCodeBtn: Button
    private lateinit var confirmProgressBar: ProgressBar
    private lateinit var resendProgressBar: ProgressBar
    private lateinit var progressBar:ProgressBar
    lateinit var continueBtn: Button

    private val loginAdviseText: String by lazy {
        getString(R.string.have_an_account)
    }
    private var spannableTextColor = 0;
    private val spannableString: SpannableString by lazy {
        loginAdviseText.setAsSpannable()
    }

    val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.custom_dialog)
        }
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_phone_signup, container, false)
    }

    override fun onResume() {
        super.onResume()
        val toolbar = phone_signup_appbar.findViewById<Toolbar>(R.id.reusable_toolbar)
        val navController = Navigation.findNavController(phone_signup_appbar)

        setupToolbarAndNavigationUI(toolbar, navController)
        buttonAndProgressBarActivity()

        setUpCountrySpinner(getString(R.string.select_your_country_str), phone_signup_country_spinner)
        setupLoginSpannableString()
        initEnterKeyToSubmitForm(phone_signup_password_et) { signupRequest() }
    }

    private fun buttonAndProgressBarActivity() {
        //Drawable background for confirm button
        val confirmBtnBackgroundDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.rounded_corner_background_primary
        )
        //Drawable background for resend code button
        val resendCodeBtnBackgroudDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_trans)
        val signupBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            continueBtn = phone_signup_btn.findViewById(R.id.btn)
            continueBtn = phone_signup_btn.findViewById(R.id.btn)
            progressBar = phone_signup_btn.findViewById(R.id.progress_bar)
            continueBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
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
            confirmBtn.text = getString(R.string.confirm)
            resendCodeBtn.text = getString(R.string.resend_code)
            continueBtn.text = getString(R.string.continue_text)
            //Set resend code button progress bar
            resendProgressBar = includedViewForResendBtn.findViewById(R.id.progress_bar)
            //Set confirm button progress bar
            confirmProgressBar = includedViewForConfirmBtn.findViewById(R.id.progress_bar)

        }, {
            continueBtn.setOnClickListener {
                signupRequest()
            }


        })

        phone_signup_password_et.doOnTextChanged { text, start, count, after ->
            if (text != null) {
                validatePasswordAndAdvise(text, phone_signup_password_standard_tv)
            }
            return@doOnTextChanged
        }
        setupCategorySpinner(requireContext(), phone_signup_category_spinner, R.array.categories_array)
    }



    private fun setupLoginSpannableString() {
        spannableTextColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = loginAdviseText.length
        val start = 17
        setupSpannableLinkAndDestination(
            phone_signup_login_tv,
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

    /**
     * Sign up request
     *
     */
    @SuppressLint("SetTextI18n")
    private fun signupRequest() {
        var dialCodePattern = Regex("""(\d{1,3})""")
        val country = phone_signup_country_spinner.selectedItem as String
        var dialCode = dialCodePattern.find(country)?.value
        var phoneNumber = phone_signup_phone_number_et.text.toString().trim()
        val passwordString = phone_signup_password_et.text.toString().trim()
        var phonePattern = Regex("""\d{10,13}""")
        val firstZeroPattern = Regex("""[0]\d+""")
        var checkFirstZero = firstZeroPattern.matches(phoneNumber)
        val checkPhoneStandard = phonePattern.matches(phoneNumber)

        Log.i(title, "check $checkFirstZero")
        val checkForEmpty =
            IsEmptyCheck(phone_signup_phone_number_et, phone_signup_password_et)
        val validation = IsEmptyCheck.fieldsValidation(null, passwordString)
        when {
            country == getString(R.string.select_your_country_str) -> requireActivity().gdToast(getString(R.string.select_your_country_str), Gravity.BOTTOM)
            checkForEmpty != null -> {
                val pattern = Regex("""phone_number|password""")
                requireActivity().getEditTextName(checkForEmpty, pattern)
            }
            validation != null -> requireActivity().gdErrorToast("$validation is invalid", Gravity.BOTTOM)
            !checkPhoneStandard -> requireActivity().gdErrorToast("Invalid phone number", Gravity.BOTTOM)
            checkFirstZero -> {
                phoneNumber = phoneNumber.removePrefix("0")
                phone_signup_phone_number_et.setText(phoneNumber)
            }


            else -> {
                phoneNumber = dialCode + phoneNumber
                val category = phone_signup_category_spinner.selectedItem as String
                val userData = User()
                userData.category = category
                userData.phone = phoneNumber
                userData.password = passwordString
                requireActivity().gdToast("$phoneNumber", Gravity.BOTTOM)
                val request = userViewModel.registerUser(userData)
                val response = requireActivity().observeRequest(request, progressBar, continueBtn)
                response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    val (bool, result) = it
                    onRequestResponseTask<User>(bool, result){
                        activateUserRequest(phoneNumber)
                    }
                })
            }
        }

    }
    private fun activateUserRequest(phone:String) {
//        var code:String? = null
        val pinEditText = dialog.findViewById<PinEntryEditText>(R.id.txt_pin_entry)

        requireActivity().gdToast("$phone", Gravity.BOTTOM)
        confirmBtn.setOnClickListener {
            val code = pinEditText.text.toString()
            if (code.isBlank()) {
                requireActivity().gdErrorToast("Empty code is not allowed", Gravity.BOTTOM)
            }
            else{
                Log.i(title, "$code")
                val request = userViewModel.activateUser(phone, code)
//                requireActivity().gdToast("$code $phone", Gravity.BOTTOM)
                val response =
                    requireActivity().observeRequest(request, confirmProgressBar, confirmBtn)
                response.observe(viewLifecycleOwner, Observer {
                    val (bool, result) = it
                    onRequestResponseTask<User>(
                        bool,
                        result
                    ){
                        dialog.dismiss()
                        findNavController().navigate(R.id.loginFragment)
                    }
                })

            }

        }
        resendCodeBtn.setOnClickListener {
            val request = userViewModel.resendCode(phone)
            val response =
                requireActivity().observeRequest(request, confirmProgressBar, confirmBtn)
            response.observe(viewLifecycleOwner, Observer {
                val (bool, result) = it
                onRequestResponseTask<User>(
                    bool,
                    result
                ){}
            })

        }
        dialog.show{
            cornerRadius(15F)
        }
    }

}


