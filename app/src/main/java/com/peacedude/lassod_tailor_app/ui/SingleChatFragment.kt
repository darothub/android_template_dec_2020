package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.vanniktech.emoji.EmojiPopup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_single_chat.*


/**
 * A simple [Fragment] subclass.
 * Use the [SingleChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
        }

        single_chat_send_fab.setOnClickListener {
            val newText: String = single_chat_emoji_et.text.toString()

        }

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


