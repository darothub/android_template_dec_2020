package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.android.synthetic.main.fragment_phone_signup.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup_choices.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PhoneSignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneSignupFragment : Fragment() {
    val title: String = getName()
    lateinit var signupPhoneBtn: Button

    private val loginAdviseText: String by lazy {
        getString(R.string.have_an_account)
    }
    private var spannableTextColor = 0;
    private val spannableString: SpannableString by lazy {
        loginAdviseText.setAsSpannable()
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
        return inflater.inflate(R.layout.fragment_phone_signup, container, false)
    }

    override fun onResume() {
        super.onResume()

        val signupBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            signupPhoneBtn = phone_signup_btn.findViewById(R.id.btn)
            signupPhoneBtn.text = getString(R.string.signup)
            signupPhoneBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        }, {
            signupPhoneBtn.setOnClickListener {
//                goto(R.id.emailSignupFragment)
            }

        })

        setUpSpinner()
        setupLoginSpannableString()
    }

    fun setUpSpinner(){
        val locale = Locale.getAvailableLocales()
        var countriesIsoAndName: HashMap<String, String> = HashMap()
        locale.associateByTo(countriesIsoAndName, {
            it.displayCountry
        },{
            "${it.displayCountry}(+${PhoneNumberUtil.createInstance(context).getCountryCodeForRegion(it.country)})"
        })
        val countries = countriesIsoAndName.values.sorted().toMutableList()
        countries[0] = getString(R.string.select_your_country_str)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_colored_text_layout, countries)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        phone_signup_category_spinner.adapter = adapter

    }

    private fun setupLoginSpannableString() {
        spannableTextColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = loginAdviseText.length
        val start = 17
        setupSpannableLinkAndDestination(
            loginAdviseText,
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
}