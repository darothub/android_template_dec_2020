package com.peacedude.lassod_tailor_app.ui.subscription

import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.hide
import com.peacedude.lassod_tailor_app.helpers.show
import kotlinx.android.synthetic.main.activity_subscription.*
import kotlinx.android.synthetic.main.fragment_add_card.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCardFragment : Fragment() {

    val cardDetailIncludedBtnLayout by lazy {
        add_card_fragment_card_detail_layout.findViewById<View>(R.id.card_details_include_btn)
    }
    private val dialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.success_dialog_layout)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
    lateinit var button: Button
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
        return inflater.inflate(R.layout.fragment_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val payBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        payBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorGreen),
            PorterDuff.Mode.SRC_IN
        )

        buttonTransactions({
            button = cardDetailIncludedBtnLayout.findViewById(R.id.btn)
            button.background = payBtnBackground
            button.text = getString(R.string.pay)
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
        },{
            button.setOnClickListener {
                dialog.show()
            }
        })
        add_card_fragment_add_card_layout.setOnClickListener {
            add_card_fragment_card_detail_layout.show()
            add_card_fragment_add_card_layout.hide()
        }
        (requireActivity().subscription_activity_appbar as androidx.appcompat.widget.Toolbar).setNavigationOnClickListener {
            if(add_card_fragment_add_card_layout.hide()){
                add_card_fragment_add_card_layout.show()
                add_card_fragment_card_detail_layout.hide()
            }
            else{
                add_card_fragment_add_card_layout.show()
//                requireActivity().gdToast("Here", Gravity.BOTTOM)
                requireActivity().finish()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }

}