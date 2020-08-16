package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.peacedude.lassod_tailor_app.R

fun Fragment.goto(destinationId: Int) {
    findNavController().navigate(destinationId)
}

fun Fragment.setupSpannableLinkAndDestination(text:String, textView: TextView,
                       textColor:Int, spannableString: SpannableString, start:Int, end:Int, onSpannableClick:()->Unit
) {
    onSpannableClick()
    spannableString.setColorToSubstring(textColor, start, end)
    spannableString.removeUnderLine(start, end)
    textView.text = spannableString
    textView.movementMethod = LinkMovementMethod.getInstance()

}