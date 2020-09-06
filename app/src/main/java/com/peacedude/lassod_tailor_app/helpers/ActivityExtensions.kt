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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.facebook.shimmer.ShimmerFrameLayout
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.ui.SplashScreenActivity

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
    request: LiveData<ServicesResponseWrapper<ParentData>>,
    progressBar: ProgressBar?, button: Button?
): LiveData<Pair<Boolean, Any?>> {
    val result = MutableLiveData<Pair<Boolean, Any?>>()
    val title: String by lazy {
        this.getName()
    }


    hideKeyboard()
    request.observe(this as LifecycleOwner, Observer {
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
                            gdErrorToast("$errorResponse", Gravity.BOTTOM)
                        }
                        in 400..499 ->{
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
                    progressBar?.hide()
                    button?.show()
                    gdToast("$errorResponse", Gravity.BOTTOM)
                    startActivity(Intent(this as? Context, MainActivity::class.java))
                    result.postValue(Pair(false, errorResponse))

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
 * Handles sign-up request live response
 *
 * @param bool
 * @param result
 */
fun Activity.onRequestResponseTask(
    bool: Boolean,
    result: Any?,
    action:()-> Unit
) {
    when (bool) {

        true -> {
            try {
                val res = result as UserResponse
                gdToast(res.message.toString(), Gravity.BOTTOM)
                action()
                Log.i("onResponseTask", "result of registration ${res.message} ${res.data.firstName}\n${res.data.userId}")
            }
            catch (e:java.lang.Exception){
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
    }else{
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