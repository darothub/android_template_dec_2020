package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.text.SpannableString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    lateinit var continueBtn: Button
    private val loginAviseText: String by lazy {
        getString(R.string.have_an_account)
    }
    private val spannableString: SpannableString by lazy {
        loginAviseText.setAsSpannable()
    }
    private var textColor = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val continueBtnBackgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        buttonTransactions({
            continueBtn = continue_btn.findViewById(R.id.btn)

            continueBtn.setOnClickListener {
                val customDialog = CustomDialogFragment()
                customDialog.show(parentFragmentManager, "My dialog")
            }
        },{
            continueBtn.background = continueBtnBackgroundDrawable
            continueBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            continueBtn.text = getString(R.string.continue_text)

        })

        val toolbar = signup_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(signup_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)

        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = loginAviseText.length
        val start = 17
        setupSpannableLinkAndDestination(loginAviseText, login_text, textColor, spannableString, start, textLen){
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.loginFragment)
            }
        }
    }

}