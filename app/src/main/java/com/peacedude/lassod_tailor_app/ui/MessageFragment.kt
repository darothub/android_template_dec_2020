package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_media.media_fragment_rv
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.media_recycler_item.view.*
import kotlinx.android.synthetic.main.message_list_item.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [MessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessageFragment : DaggerFragment() {

    private val title by lazy {
        getName()
    }
    val header by lazy {
        authViewModel.header
    }

    lateinit var noDataFirstIcon:ImageView
    lateinit var noDataSecondIcon:ImageView
    lateinit var noDataText:TextView


    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }

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
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noDataFirstIcon = message_fragment_no_data_included_layout.findViewById<ImageView>(R.id.no_data_first_icon_iv)
        noDataSecondIcon = message_fragment_no_data_included_layout.findViewById<ImageView>(R.id.no_data_second_icon_iv)
        noDataText = message_fragment_no_data_included_layout.findViewById<TextView>(R.id.no_data_text_tv)

        i(title, "message frag")
        noDataFirstIcon.hide()
        noDataSecondIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_baseline_message_24
            )
        )
        noDataText.text = getString(R.string.you_have_no_photo_str)

        val listOfMessage = arrayListOf<MessageItem>(
            MessageItem(
                "Bola",
                "I miss you man",
                "https://i.pinimg.com/originals/5a/61/62/5a616249f9a5ea04a6b35153e650b6bd.jpg",
                "8:45Am"
            ),
            MessageItem(
                "Bola",
                "I miss you man",
                "https://i.pinimg.com/originals/5a/61/62/5a616249f9a5ea04a6b35153e650b6bd.jpg",
                "8:45Am"
            ),
            MessageItem(
                "Bola",
                "I miss you man",
                "https://i.pinimg.com/originals/5a/61/62/5a616249f9a5ea04a6b35153e650b6bd.jpg",
                "8:45Am"
            )
        )

        if (listOfMessage.isNotEmpty()) {
            message_fragment_rv.setupAdapter<MessageItem>(R.layout.message_list_item) { adapter, context, list ->

                bind { itemView, position, item ->
                    itemView.message_item_name_tv.text = item?.senderName
                    itemView.message_item_time_tv.text = item?.time
                    itemView.message_item_message_tv.text = item?.message
                    itemView.message_item_thumbnail_iv.load(item?.senderImage){
                        crossfade(true)
                        placeholder(R.drawable.profile_image)
                        transformations(CircleCropTransformation())
                    }
                    itemView.setOnClickListener {
                        goto(R.id.singleChatFragment)
                    }
                }
                setLayoutManager(
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                )
                submitList(listOfMessage)
            }

        } else {
            message_fragment_vf.showNext()
        }

    }

}



data class MessageItem(
    var senderName: String,
    var message: String,
    var senderImage: String,
    var time: String
)