package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.os.Build
import android.view.KeyEvent
import android.widget.EditText
import androidx.fragment.app.Fragment


inline fun Any.buildVersion(forSdkGreaterThankM:()->Unit, forSdkLesserThanM:()->Unit){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        forSdkGreaterThankM()
    }else{
        forSdkLesserThanM()
    }
}

/**
 * Set enter key for form submission
 *
 * @param editText
 * @param request
 */
inline fun Fragment.initEnterKeyToSubmitForm(editText: EditText, crossinline request: () -> Unit) {
    editText.setOnKeyListener { view, keyCode, keyEvent ->
        if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

            request()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}