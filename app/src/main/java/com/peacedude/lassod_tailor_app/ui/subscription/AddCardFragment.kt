package com.peacedude.lassod_tailor_app.ui.subscription

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.hide
import com.peacedude.lassod_tailor_app.helpers.show
import kotlinx.android.synthetic.main.activity_subscription.*
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.android.synthetic.main.profile_header.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCardFragment : Fragment() {


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

        add_card_fragment_add_card_layout.setOnClickListener {
            add_card_fragment_card_detail_layout.show()
            add_card_fragment_add_card_layout.hide()
        }
        (requireActivity().subscription_activity_tb as androidx.appcompat.widget.Toolbar).setNavigationOnClickListener {
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

}