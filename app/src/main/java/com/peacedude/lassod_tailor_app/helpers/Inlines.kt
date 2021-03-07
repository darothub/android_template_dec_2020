package com.peacedude.lassod_tailor_app.helpers

import android.os.Build
import android.view.KeyEvent
import android.widget.EditText
import com.auth0.android.jwt.JWT
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


inline fun buildVersion(forSdkGreaterThankM: () -> Unit, forSdkLesserThanM: () -> Unit) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        forSdkGreaterThankM()
    } else {
        forSdkLesserThanM()
    }
}

inline fun <reified T> user(json: String): T? {
    val jwt = JWT(json)
    return jwt.claims["payload"]?.asObject(T::class.java)
}

/**
 * Set enter key for form submission
 *
 * @param editText
 * @param request
 */
inline fun initEnterKeyToSubmitForm(editText: EditText, crossinline request: () -> Unit) {
    editText.setOnKeyListener { view, keyCode, keyEvent ->
        if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

            request()
            return@setOnKeyListener true
        }
        return@setOnKeyListener false
    }
}

suspend fun Flow<ServicesResponseWrapper<ParentData>>.handleResponse(
     success: suspend (ServicesResponseWrapper<ParentData>) -> Boolean,
    error: (String) -> Unit
){
    try {

        collectLatest {
            success(it)
        }

    } catch (e: Throwable) {
        error(e.message.toString())
    }
}