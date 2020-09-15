package com.peacedude.lassod_tailor_app.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlin.reflect.KFunction


fun Fragment.goto(destinationId: Int) {
    findNavController().navigate(destinationId)
}



fun setupToolbarAndNavigationUI(toolbar: Toolbar, navController: NavController) {
    NavigationUI.setupWithNavController(toolbar, navController)
}

/**
 * Navigate to destination id
 *
 * @param destinationId
 */
fun Fragment.goto(destinationId: NavDirections) {
    findNavController().navigate(destinationId)
}

/**
 * Navigate up
 *
 */
fun Fragment.gotoUp() {
    findNavController().navigateUp()
}

/**
 * Navigate to uri
 *
 * @param uri
 */
fun Fragment.goto(uri: Uri) {
    val request = NavDeepLinkRequest.Builder
        .fromUri(uri)
        .build()
    findNavController().navigate(request)
}

private fun Fragment.hideKeyboard() {

//    buildVersion({
//        val controller = requireView().windowInsetsController
//        controller?.hide(WindowInsets.Type.ime())
//    },{
//
//    })
    // this will give us the view
    // which is currently focus
    // in this layout
    val view: View? = requireActivity().getCurrentFocus()

    // if nothing is currently
    // focus then this will protect
    // the app from crash
    if (view != null) {

        // now assign the system
        // service to InputMethodManager
        val manager: InputMethodManager? = requireActivity().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager?
        manager
            ?.hideSoftInputFromWindow(
                view.getWindowToken(), 0
            )
    }

}

/**
 * Handles sign-up request live response
 *
 * @param bool
 * @param result
 */
fun Fragment.onRequestResponseTask(
    bool: Boolean,
    result: Any?,
    action:()-> Unit
) {
    when (bool) {

        true -> {
            try {
                val res = result as UserResponse
                requireActivity().gdToast(res.message.toString(), Gravity.BOTTOM)
                action()
                Log.i("onResponseTask", "result of registration ${res.message} ${res.data.token}\n${res.data.userId}")
            }
            catch (e:java.lang.Exception){
                requireActivity().gdErrorToast(getString(R.string.server_error), Gravity.BOTTOM)
            }

//               Log.i(title, "clearedRegister $clearRegister")
        }
        else -> Log.i("onResponseTask", "error $result")
    }
}

