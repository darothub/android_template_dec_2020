package com.peacedude.lassod_tailor_app.ui.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.goto
import com.peacedude.lassod_tailor_app.helpers.hide
import kotlinx.android.synthetic.main.fragment_all_video.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_subscription_home.*
import kotlinx.android.synthetic.main.profile_header.*

/**
 * A simple [Fragment] subclass.
 * Use the [SubscriptionHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubscriptionHomeFragment : Fragment() {
    private val toolbar by lazy {
        (subscription_home_fragment_tb as androidx.appcompat.widget.Toolbar)
    }

    lateinit var noDataFirstIcon:ImageView
    lateinit var noDataSecondIcon:ImageView
    lateinit var noDataText:TextView
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
        return inflater.inflate(R.layout.fragment_subscription_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        NavigationUI.setupWithNavController(toolbar, navController)

        noDataFirstIcon = subscription_home_fragment_no_data_included_layout.findViewById<ImageView>(R.id.no_data_first_icon_iv)
        noDataSecondIcon = subscription_home_fragment_no_data_included_layout.findViewById<ImageView>(R.id.no_data_second_icon_iv)
        noDataText = subscription_home_fragment_no_data_included_layout.findViewById<TextView>(R.id.no_data_text_tv)

        noDataFirstIcon.hide()
        noDataSecondIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.shopping_cart_24px))
        noDataText.text = getString(R.string.you_have_no_sub)


    }


}