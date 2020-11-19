package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import java.io.File
import java.util.ArrayList


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
    action:(UserResponse<T>?)-> Unit
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

fun Fragment.checkCameraPermission():Boolean{
    return requireActivity().checkCameraPermission()
}

fun Fragment.saveBitmap(bmp:Bitmap): File? {
    return requireActivity().saveBitmap(bmp)
}

fun Fragment.uriToBitmap(image:Uri):Bitmap?{
    return requireActivity().uriToBitmap(image)
}

fun Fragment.setUpCountrySpinnerWithDialCode(header:String, spinner: Spinner){
   requireActivity().setUpCountrySpinnerWithDialCode(header, spinner)

}

fun Fragment.setUpCountrySpinner(header:String, spinner: Spinner){
    requireActivity().setUpCountrySpinner(header, spinner)
}

fun Fragment.setUpSpinnerWithList(header:String, spinner: Spinner, list: ArrayList<String>){
    requireActivity().setUpSpinnerWithList(header, spinner, list)
}

inline fun <reified T> Fragment.onFlowResponse(button:Button?=null, progressBar: ProgressBar?=null, loader:Boolean=false, response: ServicesResponseWrapper<ParentData>, action:(T?)->Unit) {
    requireActivity().onFlowResponse<T>(button, progressBar, loader, response, action)
}

fun Fragment.goto(destination:Class<*>){
    requireActivity().goto(destination)
}