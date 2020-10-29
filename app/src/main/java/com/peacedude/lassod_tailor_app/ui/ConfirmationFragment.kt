package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.changeStatusBarColor
import com.peacedude.lassod_tailor_app.helpers.goto
import kotlinx.android.synthetic.main.fragment_confirmation.*


/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationFragment : Fragment(R.layout.fragment_confirmation) {
    private lateinit var okBtn: Button
    private val arg: ConfirmationFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        changeStatusBarColor(R.color.colorTransparentWhite)
        requireActivity().window.setBackgroundDrawableResource(R.color.colorTransparentWhite)
        super.onCreate(savedInstanceState)
        arguments?.let {
        }


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val okBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_primary)
        buttonTransactions({
            okBtn = confirmation_include_btn.findViewById(R.id.btn)
            okBtn.background = okBtnBackground
            okBtn.text = getString(R.string.ok)
            okBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        }, {
            okBtn.setOnClickListener {
                goto(R.id.loginFragment)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.colorPrimary
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.colorPrimary
        )
    }

}