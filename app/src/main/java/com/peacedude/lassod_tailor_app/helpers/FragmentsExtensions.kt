package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.MainActivity


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
    requireActivity().hideKeyboard()
}

/**
 * Handles sign-up request live response
 *
 * @param bool
 * @param result
 */
inline fun <reified T>Fragment.onRequestResponseTask(
    bool: Boolean,
    result: Any?,
    action:()-> Unit
) {
    requireActivity().onRequestResponseTask<T>(bool, result, action)
}

/**
 * Observe request response
 * and manipulate progressbar
 * and button behaviour
 *
 * @param request
 * @param progressBar
 * @param button
 * @return
 */
fun Fragment.observeRequest(
    request: LiveData<ServicesResponseWrapper<ParentData>>?,
    progressBar: ProgressBar?, button: Button?, loader:Boolean=false
): LiveData<Pair<Boolean, Any?>> {
    return requireActivity().observeRequest(request, progressBar, button, loader)
}
fun Fragment.requestObserver(
    progressBar: ProgressBar?,
    btn: Button?,
    req: LiveData<ServicesResponseWrapper<ParentData>>,
    loader: Boolean=false,
    action: (Boolean, Any?) -> Unit
) {
    requireActivity().requestObserver(progressBar, btn, req, loader, action)
}

fun Fragment.changeStatusBarColor(colorRes: Int) {
    requireActivity().changeStatusBarColor(colorRes)
}
fun Fragment.setCustomColor(colorRes: Int): Int {
    return requireActivity().setCustomColor(colorRes)
}