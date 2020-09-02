package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.utils.loggedInUser
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.StringBuilder

inline fun buttonTransactions(funct1:()->Unit, funct2:()->Unit){
    funct1()
    funct2()
}
/**
 * Get any name
 *
 * @return
 */
fun Any.getName(): String {
    return this::class.qualifiedName!!
}

fun logout(storageRequest:StorageRequest, context: Context) {
    val currentUser = storageRequest.checkUser(loggedInUser)
//    Log.i("return", "res $res")
    context.startActivity(Intent(context, MainActivity::class.java))
}



