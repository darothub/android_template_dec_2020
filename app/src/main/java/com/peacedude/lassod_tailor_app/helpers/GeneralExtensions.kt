package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R
import kotlinx.android.synthetic.main.fragment_login.*

inline fun buttonTransactions(funct1:()->Unit, funct2:()->Unit){
    funct1()
    funct2()
}
