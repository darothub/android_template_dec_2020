package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit private var loginBtn: Button

    private val newUserText: String by lazy {
        getString(R.string.new_user)
    }
    private val spannableString: SpannableString by lazy {
        newUserText.setAsSpannable()
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonTransactions({
            loginBtn = login_page_btn.findViewById(R.id.btn)
        },{
            loginBtn.text = getString(R.string.login)
        })

        val toolbar = login_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(login_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)

        loginBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))


        loginBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val textLen = newUserText.length
        val start = 10
        setupSpannableLinkAndDestination(newUserText, new_user, textColor, spannableString, start, textLen){
            spannableString.enableClickOnSubstring(start, textLen) {
                goto(R.id.signupFragment)
            }
        }

    }

    /**
     * Spannable sign-up link
     *
     */
    private fun setupSignUpLink() {
        val textLen: Int by lazy {
            newUserText.length
        }
        val start = 10
        val end = textLen
        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        spannableString.enableClickOnSubstring(start, end) {
            goto(R.id.signupFragment)
        }
        spannableString.setColorToSubstring(textColor, start, end)
        spannableString.removeUnderLine(start, end)
        new_user.text = spannableString
        new_user.movementMethod = LinkMovementMethod.getInstance()
    }


}