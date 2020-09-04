package com.peacedude.lassod_tailor_app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class HomeFragment : DaggerFragment() {

    lateinit var loginBtn: Button
    lateinit var signupBtn: Button
    val leftAnimation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.left_animation)
    }
    val rightAnimation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation)
    }

    @Inject
    lateinit var storageRequest: StorageRequest

    val currentUser: User? by lazy {
        storageRequest.checkUser(loggedInUserKey)
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
        val signupBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        val loginBackgroundDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_outline)

        buttonTransactions({
            loginBtn = login_btn.findViewById(R.id.btn)
            signupBtn = signup_btn.findViewById(R.id.btn)

        }, {
            loginBtn.text = getString(R.string.login)
            signupBtn.text = getString(R.string.signup)

            loginBtn.animation = leftAnimation
            signupBtn.animation = rightAnimation

            signupBtn.background = signupBackgroundDrawable
            signupBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            loginBtn.background = loginBackgroundDrawable
        })



        signupBtn.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        loginBtn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        if (currentUser != null) {
            when (currentUser?.loggedIn) {
                true -> startActivity(Intent(requireContext(), ProfileActivity::class.java))
            }
        }
    }

    override fun onAttach(context: Context) {
        injectMembers()
        super.onAttach(context)


    }

    protected open fun injectMembers() =
        AndroidSupportInjection.inject(this)

}
