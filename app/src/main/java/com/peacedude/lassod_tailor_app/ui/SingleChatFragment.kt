package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.ui.adapters.ChatBubbleAdapter
import com.utsman.recycling.setupAdapter
import com.vanniktech.emoji.EmojiPopup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.message_list_item.view.*
import kotlinx.android.synthetic.main.single_chat_customer_item.view.*
import kotlinx.android.synthetic.main.single_chat_outgoing_item.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [SingleChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

const val CHATSENDER = 0
const val CHATRECEIVER = 1

class SingleChatFragment : DaggerFragment() {

    val title: String by lazy {
        getName()
    }

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emojiPopup = EmojiPopup.Builder.fromRootView(single_chat_root_view)
            .setOnEmojiBackspaceClickListener { Log.d(title, "Clicked on Backspace") }
            .setOnEmojiClickListener { emoji, imageView -> Log.d(title, "Clicked on emoji") }
            .setOnSoftKeyboardOpenListener { Log.d(title, "Opened soft keyboard") }
            .setOnSoftKeyboardCloseListener { Log.d(title, "Closed soft keyboard") }
            .setOnEmojiPopupShownListener { single_chat_emoji_iv.setImageResource(R.drawable.ic_baseline_keyboard_24) }
            .setOnEmojiPopupDismissListener { single_chat_emoji_iv.setImageResource(R.drawable.ic_baseline_insert_emoticon_24) }
            .build(single_chat_emoji_et)

        single_chat_emoji_iv.setOnClickListener {
            emojiPopup.toggle()
        }

        single_chat_nav_back_iv.setOnClickListener {
            findNavController().popBackStack()
        }
        

        var listOfChatMessageTwo = arrayListOf<ChatMessage>(
            ChatMessage("Hello", CHATSENDER)
        )
        val listOfChatMessage = arrayListOf<ChatMessage>(
            ChatMessage("Hi",  CHATRECEIVER),
            ChatMessage("Hello", CHATSENDER)
        )
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val chatBubbleAdapter = ChatBubbleAdapter(listOfChatMessage){
            i(title, "Message ${it.message} chatmessage $it")
        }
        single_chat_send_fab.setOnClickListener {
            val newMessage = single_chat_emoji_et.text.toString()
            single_chat_emoji_et.setText("")
            single_chat_emoji_et.clearFocus()
            val newChatMessage = ChatMessage(newMessage, CHATRECEIVER)
            listOfChatMessage.add(newChatMessage)
            chatBubbleAdapter.setChatMessageList(listOfChatMessage)
            single_chat_fragment_incoming_rv.smoothScrollToPosition(listOfChatMessage.size)
        }
        single_chat_emoji_iv.setOnClickListener {
            val newMessage = single_chat_emoji_et.text.toString()
            single_chat_emoji_et.setText("")
            single_chat_emoji_et.clearFocus()
            val newChatMessage = ChatMessage(newMessage, CHATSENDER)
            listOfChatMessage.add(newChatMessage)
            chatBubbleAdapter.setChatMessageList(listOfChatMessage)
            single_chat_fragment_incoming_rv.smoothScrollToPosition(listOfChatMessage.size)
        }
        single_chat_fragment_incoming_rv.adapter = chatBubbleAdapter

        single_chat_emoji_et.setOnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= single_chat_emoji_et.right - single_chat_emoji_et.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    // your action here
                    i(title, "Drawable touched")
                    return@setOnTouchListener true
                }
            }
            false
        }


        requireActivity().onBackPressedDispatcher.addCallback {
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
        }
    }

}


data class ChatMessage(
    var message: String?,
    var sender: Int = CHATSENDER
)


