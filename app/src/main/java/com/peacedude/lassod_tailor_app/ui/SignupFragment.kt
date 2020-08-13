package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.AppBarLayout
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buildVersion
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.signup_btn
import kotlinx.android.synthetic.main.fragment_signup.*


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    lateinit var signupBtn: Button
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
        signupBtn = signup_btn.findViewById(R.id.btn)
        signupBtn.text = getString(R.string.continue_text)

        buildVersion({
            signupBtn.setBackgroundColor(resources.getColor(R.color.colorAccent, requireActivity().theme))
            signupBtn.setTextColor(resources.getColor(R.color.colorPrimary, requireActivity().theme))
        },{
            signupBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
            signupBtn.setTextColor(resources.getColor(R.color.colorPrimary))
        })

        val appBar = signup_appbar.findViewById<AppBarLayout>(R.id.appbar)
        val toolbar = signup_appbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.reusable_toolbar)

        val navController = Navigation.findNavController(signup_appbar)

        NavigationUI.setupWithNavController(toolbar, navController)
    }

}