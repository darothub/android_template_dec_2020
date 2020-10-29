package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.internal.main.DialogLayout
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import com.peacedude.lassod_tailor_app.ui.MainActivity

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
fun Activity.observeRequest(
    request: LiveData<ServicesResponseWrapper<ParentData>>?,
    progressBar: ProgressBar?, button: Button?, loader:Boolean=false
): LiveData<Pair<Boolean, Any?>> {
    val result = MutableLiveData<Pair<Boolean, Any?>>()
    val title: String by lazy {
        this.getName()
    }
    val dialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    hideKeyboard()
    request?.observe(this as LifecycleOwner, Observer {
        try {
            val responseData = it.data
            val errorResponse = it.message
            val errorCode = it.code
            when (it) {
                is ServicesResponseWrapper.Loading<*> -> {
                    if(loader) dialog.show() else dialog.hide()
                    progressBar?.show()
                    button?.hide()
                    i(title, "Loading..")
                }
                is ServicesResponseWrapper.Success -> {
                    dialog.hide()
                    progressBar?.hide()
                    button?.show()
                    result.postValue(Pair(true, responseData))

                    i(title, "success ${it.data}")
                }
                is ServicesResponseWrapper.Error -> {
                    dialog.hide()
                    progressBar?.hide()
                    button?.show()
                    when (errorCode) {
                        0 -> {
                            Log.i(title, "Errorcode ${errorCode}")
                            gdErrorToast("$errorResponse", Gravity.BOTTOM)
                        }
                        in 400..499 -> {
                            result.postValue(Pair(false, errorResponse))
//                            toast("$errorResponse")
                            gdErrorToast("$errorResponse", Gravity.BOTTOM)
                        }
                        in 500..600 -> {
                            gdErrorToast(getString(R.string.server_error), Gravity.BOTTOM)
                        }
                        else -> {
                            result.postValue(Pair(false, errorResponse))
//                            toast("$errorResponse")
                            gdErrorToast("$errorResponse", Gravity.BOTTOM)
                        }
                    }

                    Log.i(title, "Error ${it.message}")
                }
                is ServicesResponseWrapper.Logout -> {
                    dialog.hide()
                    val unAuthorizedString = getString(R.string.unauthorized_user)
                    progressBar?.hide()
                    button?.show()
                    gdToast(unAuthorizedString, Gravity.BOTTOM)
                    startActivity(Intent(this as? Context, MainActivity::class.java))
                    result.postValue(Pair(false, unAuthorizedString))

                    i(title, "Log out $unAuthorizedString")

//                    navigateWithUri("android-app://anapfoundation.navigation/signin".toUri())
                }
            }
        } catch (e: Exception) {
            dialog.hide()
            i(title, e.localizedMessage)
        }

    })

    return result
}

fun Activity.requestObserver(
    progressBar: ProgressBar?,
    btn: Button?,
    req: LiveData<ServicesResponseWrapper<ParentData>>,
    loader: Boolean=false,
    action: (Boolean, Any?) -> Unit
) {

    val response = observeRequest(req, progressBar, btn, loader)
    response.observe(this as LifecycleOwner, Observer {
        val (bool, result) = it
        action(bool, result)
    })
}

/**
 * Handles sign-up request live response
 *
 * @param bool
 * @param result
 */
inline fun <reified T>Activity.onRequestResponseTask(
    bool: Boolean,
    result: Any?,
    action: () -> Unit
) {
    val title = this.getName()
    when (bool) {

        true -> {
            try {
                val res = result as UserResponse<T>
                Log.i(title, "OnResponseTaskFrag ${res.message}")
//                requireActivity().gdToast(res.message.toString(), Gravity.BOTTOM)
                action()
//                i("onResponseTask", "result of registration ${res.message} ${res.data.token}\n${res.data.userId}")
            } catch (e: java.lang.Exception) {
                Log.i(title, "OnResponseTaskFrag error ${e.localizedMessage}")
            }

        }
        else -> Log.i(title, "errorOnResponseTask $result")
    }
}

fun Activity.hideKeyboard() {

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
        val view: View? = currentFocus
        val controller = view?.windowInsetsController
        controller?.hide(WindowInsets.Type.ime())
    } else {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view: View? = currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            val manager: InputMethodManager? = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager?
            manager
                ?.hideSoftInputFromWindow(
                    view.getWindowToken(), 0
                )
        }
    }


}

fun Activity.getEditTextName(checkForEmpty: EditText, pattern: Regex) {
    checkForEmpty.error = getString(R.string.field_required)
    val nameFinder =
        pattern.find(resources.getResourceEntryName(checkForEmpty.id))?.value
    val nameSplit = nameFinder?.split("_")
    val editTextName =
        if (nameSplit?.size!! > 1) "${nameSplit[0]} ${nameSplit[1]}" else nameSplit[0]
    gdErrorToast("$editTextName is empty", Gravity.BOTTOM)
}

fun Any.i(tag:String, message:String){
    Log.i(tag, message)
}
fun Any.e(tag:String, message:String){
    Log.e(tag, message)
}

