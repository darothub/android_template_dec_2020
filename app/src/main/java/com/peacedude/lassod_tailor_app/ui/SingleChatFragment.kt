package com.peacedude.lassod_tailor_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.goto
import com.vanniktech.emoji.EmojiPopup
import dagger.android.support.DaggerFragment
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions.KeyboardListener
import kotlinx.android.synthetic.main.fragment_single_chat.*


/**
 * A simple [Fragment] subclass.
 * Use the [SingleChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleChatFragment : DaggerFragment() {

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


        single_chat_emoji_iv.setOnClickListener {
            val  emojiPopup = EmojiPopup.Builder.fromRootView(single_chat_root_view).build(
                single_chat_emoji_et
            )
            emojiPopup.toggle()
//            emojiPopup.dismiss()

        }

        single_chat_nav_back_iv.setOnClickListener {
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
        }

//        val emojIcon = EmojIconActions(
//            requireContext(),
//            single_chat_root_view,
//            single_chat_emoji_et,
//            single_chat_emoji_iv
//        )
//        emojIcon.setKeyboardListener(object : KeyboardListener {
//            override fun onKeyboardOpen() {
//                Log.e("Keyboard", "open")
//            }
//
//            override fun onKeyboardClose() {
//                Log.e("Keyboard", "close")
//            }
//        })
        single_chat_send_fab.setOnClickListener {
            val newText: String = single_chat_emoji_et.text.toString()

        }

        requireActivity().onBackPressedDispatcher.addCallback {
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
        }
    }

}


//<hani.momanii.supernova_emoji_library.Helper.EmojiconEditText
//android:id="@+id/emojicon_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_alignParentBottom="true"
//android:layout_toLeftOf="@id/submit_btn"
//android:layout_toRightOf="@id/emoji_btn"
//android:imeOptions="actionSend"
//android:inputType="text"
//emojicon:emojiconSize="28sp"/>
//
//
//<hani.momanii.supernova_emoji_library.Helper.EmojiconEditText
//android:id="@+id/emojicon_edit_text2"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_above="@id/emojicon_edit_text"
//android:imeOptions="actionSend"
//android:inputType="text"
//emojicon:emojiconSize="28sp"/>

//<hani.momanii.supernova_emoji_library.Helper.EmojiconTextView
//android:id="@+id/textView"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_centerHorizontal="true"
//android:layout_centerVertical="true"
//android:layout_marginTop="26dp"
//android:lineSpacingExtra="17sp"
//android:text="Hello Emojis !"
//android:textAppearance="@style/TextAppearance.AppCompat.Large"
//android:textColor="#000000"
//emojicon:emojiconAlignment="bottom"/>