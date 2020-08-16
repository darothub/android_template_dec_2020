package com.peacedude.lassod_tailor_app.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buildVersion
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.sign


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    lateinit var loginBtn:Button
    lateinit var signupBtn:Button
    val leftAnimation by lazy{
        AnimationUtils.loadAnimation(requireContext(), R.anim.left_animation)
    }
    val rightAnimation by lazy{
        AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        loginBtn = login_btn.findViewById(R.id.btn)
        loginBtn.text = getString(R.string.login)
        signupBtn = signup_btn.findViewById(R.id.btn)
        signupBtn.text = getString(R.string.signup)

        loginBtn.animation = leftAnimation
        signupBtn.animation = rightAnimation
        buildVersion({
            signupBtn.setBackgroundColor(resources.getColor(R.color.colorAccent, requireActivity().theme))
            signupBtn.setTextColor(resources.getColor(R.color.colorPrimary, requireActivity().theme))
        },{
            signupBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
            signupBtn.setTextColor(resources.getColor(R.color.colorPrimary))
        })

        signupBtn.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        loginBtn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

}
