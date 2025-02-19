package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R

fun String.setAsSpannable(): SpannableString {
    val spannableString: SpannableString by lazy {
        SpannableString(this)
    }
    return  spannableString
}
fun SpannableString.setColorToSubstring(color:Int, start:Int, end:Int){

    val color: ForegroundColorSpan by lazy {
        ForegroundColorSpan(color)
    }
    this.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}
inline fun SpannableString.enableClickOnSubstring(start:Int, end:Int, crossinline action:() -> Unit){
    var clickableSpan = object : ClickableSpan(){
        override fun onClick(widget: View) {
            action.invoke()

        }

    }
    this.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

class NounderLine : UnderlineSpan(){
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}

fun SpannableString.removeUnderLine(start:Int, end:Int){
    this.setSpan(NounderLine(), start, end, Spanned.SPAN_MARK_MARK)
}

fun setupSpannableLinkAndDestination(textView: TextView,
                                     textColor:Int, spannableString: SpannableString, start:Int, end:Int, onSpannableClick:()->Unit
) {
    onSpannableClick()
    spannableString.setColorToSubstring(textColor, start, end)
    spannableString.removeUnderLine(start, end)
    textView.text = spannableString
    textView.movementMethod = LinkMovementMethod.getInstance()

}
fun setupSpannableLinkAndDestination(view: Button,
                                     textColor:Int, spannableString: SpannableString, start:Int, end:Int, onSpannableClick:()->Unit
) {
    onSpannableClick()
    spannableString.setColorToSubstring(textColor, start, end)
    spannableString.removeUnderLine(start, end)
    view.text = spannableString
    view.movementMethod = LinkMovementMethod.getInstance()

}


inline fun String.setSpannableString(context: Context, color:Int, tv: TextView, start: Int, end:Int,  underline:Boolean=false, crossinline action:()->Unit){
    val ssText = SpannableString(this)
    // Implement ClickableSpan
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            action()
        }

        // Change color and remove underline
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(context, color)
            ds.isUnderlineText = underline
        }
    }
    // Set the span text
    ssText.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    tv.text = ssText
    tv.movementMethod = LinkMovementMethod.getInstance();
}
inline fun String.setSpannableString(context: Context, color:Int, btn: Button, start: Int, end:Int,  underline:Boolean=false, crossinline action:()->Unit){
    val ssText = SpannableString(this)
    // Implement ClickableSpan
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            action()
        }

        // Change color and remove underline
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(context, color)
            ds.isUnderlineText = underline
        }
    }
    // Set the span text
    ssText.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    btn.text = ssText
    btn.movementMethod = LinkMovementMethod.getInstance();
}
