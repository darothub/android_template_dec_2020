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
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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

fun setupSpannableLinkAndDestination(text:String, textView: TextView,
                       textColor:Int, spannableString: SpannableString, start:Int, end:Int, onSpannableClick:()->Unit
) {
    onSpannableClick()
    spannableString.setColorToSubstring(textColor, start, end)
    spannableString.removeUnderLine(start, end)
    textView.text = spannableString
    textView.movementMethod = LinkMovementMethod.getInstance()

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
    request: LiveData<ServicesResponseWrapper<ParentData>>,
    progressBar: ProgressBar?, button: Button?
): LiveData<Pair<Boolean, Any?>> {
    val result = MutableLiveData<Pair<Boolean, Any?>>()
    val title: String by lazy {
        this.getName()
    }

    hideKeyboard()
    request.observe(viewLifecycleOwner, Observer {
        try {
            val responseData = it.data
            val errorResponse = it.message
            val errorCode = it.code
            when (it) {
                is ServicesResponseWrapper.Loading<*> -> {
                    progressBar?.show()
                    button?.hide()
                    Log.i(title, "Loading..")
                }
                is ServicesResponseWrapper.Success -> {
                    progressBar?.hide()
                    button?.show()
                    result.postValue(Pair(true, responseData))

                    Log.i(title, "success ${it.data}")
                }
                is ServicesResponseWrapper.Error -> {
                    progressBar?.hide()
                    button?.show()
                    when (errorCode) {
                        0 -> {
                            Log.i(title, "Errorcode ${errorCode}")
                            requireActivity().gdErrorToast(getString(R.string.bad_network), Gravity.BOTTOM)
                        }
                        in 400..499 ->{
                            result.postValue(Pair(false, errorResponse))
//                            toast("$errorResponse")
                            requireActivity().gdErrorToast("$errorResponse", Gravity.BOTTOM)
                        }
                        in 500..600 -> {
                            requireActivity().gdErrorToast(getString(R.string.server_error), Gravity.BOTTOM)
                        }
                        else -> {
                            result.postValue(Pair(false, errorResponse))
//                            toast("$errorResponse")
                            requireActivity().gdErrorToast("$errorResponse", Gravity.BOTTOM)
                        }
                    }

                    Log.i(title, "Error ${it.message}")
                }
                is ServicesResponseWrapper.Logout -> {
                    progressBar?.hide()
                    button?.show()
                    result.postValue(Pair(false, errorResponse))
                    requireActivity().gdToast("$errorResponse", Gravity.BOTTOM)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    Log.i(title, "Log out $errorResponse")
//                    navigateWithUri("android-app://anapfoundation.navigation/signin".toUri())
                }
            }
        } catch (e: Exception) {
            Log.i(title, e.localizedMessage)
        }

    })

    return result
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

