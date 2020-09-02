package com.peacedude.lassod_tailor_app.ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_user_account.*


/**
 * A simple [Fragment] subclass.
 * Use the [UserAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserAccountFragment : Fragment() {

    private lateinit var saveBtn: Button
    private lateinit var progressBar: ProgressBar

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
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onResume() {
        super.onResume()

        val saveBtnBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        saveBtnBackground?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor( requireContext(), R.color.colorPrimary),
        PorterDuff.Mode.SRC_IN
        )

        buttonTransactions({
            saveBtn = account_save_changes_btn.findViewById(R.id.btn)
            progressBar = account_save_changes_btn.findViewById(R.id.progress_bar)
        },{
            saveBtn.text = getString(R.string.save_changes)
            saveBtn.background = saveBtnBackground
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        })
    }

}