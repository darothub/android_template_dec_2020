package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.content.Intent
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey

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
    val currentUser = storageRequest.checkUser(loggedInUserKey)
//    Log.i("return", "res $res")
    context.startActivity(Intent(context, MainActivity::class.java))
}



