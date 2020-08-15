package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.signup_btn
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit var loginBtn: Button

    val newUserText: String by lazy {
        getString(R.string.new_user)
    }
    val spannableString: SpannableString by lazy {
        newUserText.setAsSpannable()
    }
    var textColor = 0;
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

        loginBtn = login_page_btn.findViewById(R.id.btn)
        loginBtn.text = getString(R.string.login)

        val toolbar = login_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(login_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)

        buildVersion({
            loginBtn.setBackgroundColor(resources.getColor(R.color.colorAccent, requireActivity().theme))
            loginBtn.setTextColor(resources.getColor(R.color.colorPrimary, requireActivity().theme))
        },{
            loginBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
            loginBtn.setTextColor(resources.getColor(R.color.colorPrimary))
        })

        setupSignUpLink()
        loginBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
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
        buildVersion({
            textColor = resources.getColor(R.color.colorAccent, requireContext().theme)
        },{
            textColor = resources.getColor(R.color.colorAccent)
        })
        spannableString.enableClickOnSubstring(start, end) {
            goto(R.id.signupFragment)
        }
        spannableString.setColorToSubstring(textColor, start, end)
        spannableString.removeUnderLine(start, end)
        new_user.text = spannableString
        new_user.movementMethod = LinkMovementMethod.getInstance()
    }


}