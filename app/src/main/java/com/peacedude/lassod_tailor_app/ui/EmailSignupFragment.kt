package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import kotlinx.android.synthetic.main.fragment_email_signup.*
import kotlinx.android.synthetic.main.fragment_phone_signup.*
import kotlinx.android.synthetic.main.fragment_signup_choices.*


/**
 * A simple [Fragment] subclass.
 * Use the [EmailSignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailSignupFragment : Fragment() {
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
        val backgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            emailSignupBtn = email_signup_btn.findViewById(R.id.btn)
            emailSignupBtn.background = backgroundDrawable
            emailSignupBtn.text = getString(R.string.signup)
            emailSignupBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }, {


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