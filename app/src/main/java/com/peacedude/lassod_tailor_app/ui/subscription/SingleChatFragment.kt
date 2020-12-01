package com.peacedude.lassod_tailor_app.ui.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peacedude.lassod_tailor_app.R
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.fragment_single_chat.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingleChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleChatFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  emojiPopup = EmojiPopup.Builder.fromRootView(single_chat_root_view).build(single_chat_emoji_et)
        single_chat_emoji_iv.setOnClickListener {
            emojiPopup.toggle()
        }
    }

}