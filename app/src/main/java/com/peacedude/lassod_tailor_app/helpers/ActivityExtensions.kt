package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.*
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
    progressBar: ProgressBar?, button: Button?
): LiveData<Pair<Boolean, Any?>> {
    val result = MutableLiveData<Pair<Boolean, Any?>>()
    val title: String by lazy {
        this.getName()
    }


    hideKeyboard()
    request?.observe(this as LifecycleOwner, Observer {
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

                    Log.i("Observer", "success ${it.data}")
                }
                is ServicesResponseWrapper.Error -> {
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
                    val unAuthorizedString = getString(R.string.unauthorized_user)
                    progressBar?.hide()
                    button?.show()
                    gdToast(unAuthorizedString, Gravity.BOTTOM)
                    startActivity(Intent(this as? Context, MainActivity::class.java))
                    result.postValue(Pair(false, unAuthorizedString))

                    Log.i(title, "Log out $unAuthorizedString")

//                    navigateWithUri("android-app://anapfoundation.navigation/signin".toUri())
                }
            }
        } catch (e: Exception) {
            Log.i(title, e.localizedMessage)
        }

    })

    return result
}

fun Activity.requestObserver(
    progressBar: ProgressBar?,
    btn: Button?,
    req: LiveData<ServicesResponseWrapper<ParentData>>,
    action: (Boolean, Any?) -> Unit
) {

    val response = observeRequest(req, progressBar, btn)
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
    when (bool) {

        true -> {
            try {
                when(T::class.java){
                    User::class.java ->{
                        val res = result as UserResponse<User>
                        gdToast(res.message.toString(), Gravity.BOTTOM)
                    }
                    String::class.java ->{
                        val res = result as UserResponse<String>
                        gdToast(res.message.toString(), Gravity.BOTTOM)
                    }
                }


                action()
//                Log.i(
//                    "onResponseTask",
//                    "result of registration ${res.message} ${res.data.firstName}\n${res.data.userId}"
//                )
            } catch (e: java.lang.Exception) {
                gdErrorToast(getString(R.string.server_error), Gravity.BOTTOM)
            }

//               Log.i(title, "clearedRegister $clearRegister")
        }
        else -> Log.i("onResponseTask", "error $result")
    }
}

private fun Activity.hideKeyboard() {

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

