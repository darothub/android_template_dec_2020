package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import kotlinx.android.synthetic.main.custom_dialog.*

class CustomDialogFragment : DialogFragment() {

    lateinit var confirmBtn: Button
    lateinit var resendCodeBtn: Button
    lateinit private var progressBar:ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.custom_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val confirmBtnBackgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_primary)
        //set widgets parameters
        buttonTransactions({
            confirmBtn = confirm_btn.findViewById(R.id.btn)
            confirmBtn.background = confirmBtnBackgroundDrawable
            resendCodeBtn = resend_code_btn.findViewById(R.id.btn)
            resendCodeBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_trans)
            resendCodeBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            progressBar = resend_code_btn.findViewById(R.id.progress_bar)
        },{
            confirmBtn.text = getString(R.string.confirm)
            resendCodeBtn.text = getString(R.string.resend_code)
        })
    }

}