package com.peacedude.lassod_tailor_app.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.buttonTransactions
import com.peacedude.lassod_tailor_app.helpers.changeBackgroundColor
import com.peacedude.lassod_tailor_app.helpers.goto
import kotlinx.android.synthetic.main.fragment_single_fashionista.*
import kotlinx.android.synthetic.main.search_result_media_item.view.*

class SingleFashionistaFragment : Fragment() {


    lateinit var chatButton: Button
    lateinit var callButton: Button
    lateinit var galleryButton: Button
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
        return inflater.inflate(R.layout.fragment_single_fashionista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        single_fashionista_fragment_image_iv.load(getString(R.string.test_photo)) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.profile_image)
        }

        buttonTransactions({
            chatButton = single_fashionista_fragment_chat_btn.findViewById(R.id.btn)
            callButton = single_fashionista_fragment_call_btn.findViewById(R.id.btn)
            galleryButton = single_fashionista_fragment_gallery_btn.findViewById(R.id.btn)
            val chatButtonBackground = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rounded_blue_outline_white_backgrnd
            )

            val callButtonBackground = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rounded_blue_outline_white_backgrnd
            )

            val galleryButtonBackground = galleryButton.background
            galleryButtonBackground.changeBackgroundColor(requireContext(), R.color.colorPrimary)
            callButtonBackground?.setTint(ContextCompat.getColor(requireContext(), R.color.colorGreen))
            chatButton.background = chatButtonBackground
            callButton.background = callButtonBackground
            galleryButton.background = galleryButtonBackground

            chatButton.text = getString(R.string.chat_str)
            chatButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            callButton.text = getString(R.string.call)
            galleryButton.text = getString(R.string.goto_gallery)
        },{

        })

        single_fashionista_fragment_favourite_iv.setOnClickListener {
            single_fashionista_fragment_favourite_vf.showNext()
        }
        single_fashionista_fragment_favourite_fill_iv.setOnClickListener {
            single_fashionista_fragment_favourite_vf.showPrevious()
        }

        single_fashionista_fragment_see_review_tv.setOnClickListener {
            goto(R.id.reviewFragment)
        }
    }


}