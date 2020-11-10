package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.internal.main.DialogLayout
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.ui.REQUEST_CODE
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

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
            setCanceledOnTouchOutside(false)
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
                    if(loader) dialog.show() else dialog.dismiss()
                    progressBar?.show()
                    button?.hide()
                    i(title, "Loading..")
                }
                is ServicesResponseWrapper.Success -> {
                    dialog.dismiss()
                    progressBar?.hide()
                    button?.show()
                    result.postValue(Pair(true, responseData))

                    i(title, "success ${it.data}")
                }
                is ServicesResponseWrapper.Error -> {
                    dialog.dismiss()
                    progressBar?.hide()
                    button?.show()
                    result.postValue(Pair(false, errorResponse))
                    gdErrorToast("$errorResponse", Gravity.BOTTOM)

                    Log.i(title, "Error ${it.message}")
                }
                is ServicesResponseWrapper.Logout -> {
                    dialog.dismiss()
//                    val unAuthorizedString = getString(R.string.unauthorized_user)
                    progressBar?.hide()
                    button?.show()
                    gdToast(errorResponse.toString(), Gravity.BOTTOM)
                    startActivity(Intent(this as? Context, MainActivity::class.java))
//                    result.postValue(Pair(false, errorResponse.toString()))
                    i(title, "Log out ${errorResponse.toString()}")

//                    navigateWithUri("android-app://anapfoundation.navigation/signin".toUri())
                }
            }
        } catch (e: Exception) {
            progressBar?.hide()
            dialog.dismiss()
            button?.show()
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
    action: (UserResponse<T>?) -> Unit
) {
    val title = this.getName()
    when (bool) {

        true -> {
            try {
                val res = result as UserResponse<T>
                Log.i(title, "OnResponseTaskFrag ${res.message}")
//                requireActivity().gdToast(res.message.toString(), Gravity.BOTTOM)
                action(res)
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
fun Activity.changeStatusBarColor(colorRes: Int) {
    window.statusBarColor = ContextCompat.getColor(
        this,
        colorRes
    )
}

fun Activity.setCustomColor(colorRes: Int): Int {
    return ContextCompat.getColor(
        this,
        colorRes
    )
}
fun Activity.checkCameraPermission(): Boolean {
    val title = this.getName()
    if (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        i(title, "first if here we aee")
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            )
        ) {
            i(title, "second if here we are")
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle(R.string.allow_camera)
            alertBuilder.setMessage(R.string.camera_str)
            alertBuilder.setPositiveButton(getString(R.string.ok_str)) { dialog, which ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CODE
                )
                return@setPositiveButton
            }
            alertBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                this.gdToast(
                    "Permission is needed for photo upload",
                    Gravity.BOTTOM
                )
                val settingIntent = Intent()
                settingIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.packageName, null)
                settingIntent.data = uri
                startActivity(settingIntent)
                return@setNegativeButton
            }
            val alertDialog = alertBuilder.create()
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
            }
            alertDialog.show()
        } else {
            i(title, "second else Here we are")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CODE
            )
            return true
        }
    } else {
        i(title, "first else Here we aee")
        return true
    }
    return false
}

fun Activity.saveBitmap(bmp: Bitmap): File? {
    val extStorageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    var outStream: OutputStream? = null
    var file: File?=null
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val child = "JPEG_${timeStamp}_.jpg"
    // String temp = null;
    if (extStorageDirectory != null){
        file = File(extStorageDirectory, child)
        if (file.exists()) {
            file.delete()
            file = File(extStorageDirectory, child)
        }
        try {
            outStream = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    return file
}


fun Activity.uriToBitmap(uriImage: Uri): Bitmap? {
    var mBitmap: Bitmap? = null
    if(Build.VERSION.SDK_INT < 28) {
        mBitmap = MediaStore.Images.Media.getBitmap(
          contentResolver,
            uriImage
        )
    } else {
        val source = ImageDecoder.createSource(contentResolver, uriImage)
        mBitmap = ImageDecoder.decodeBitmap(source)
    }
    return mBitmap
}
fun Activity.setUpCountrySpinnerWithDialCode(header:String, spinner: Spinner){
    val locale = Locale.getAvailableLocales()
    var countriesIsoAndName: HashMap<String, String> = HashMap()
    locale.associateByTo(countriesIsoAndName, {
        it.displayCountry
    },{
        "${it.displayCountry}(+${PhoneNumberUtil.createInstance(this).getCountryCodeForRegion(it.country)})"
    })
    val countries = countriesIsoAndName.values.sorted().toMutableList()
    countries[0] = header
    val adapter = ArrayAdapter(this, R.layout.spinner_colored_text_layout, countries)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
    spinner.adapter = adapter

}
fun Activity.setUpCountrySpinner(header:String, spinner: Spinner){
    val locale = Locale.getAvailableLocales()
    var countriesIsoAndName: HashMap<String, String> = HashMap()
    locale.associateByTo(countriesIsoAndName, {
        it.displayCountry
    },{
        it.displayCountry
    })
    val countries = countriesIsoAndName.values.sorted().toMutableList()
    countries[0] = header
    val adapter = ArrayAdapter(this, R.layout.spinner_colored_text_layout, countries)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
    spinner.adapter = adapter
}

fun Activity.setUpSpinnerWithList(header:String, spinner: Spinner, list:ArrayList<String>){
    list[0] = header
    val adapter = ArrayAdapter(this, R.layout.spinner_colored_text_layout, list)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
    spinner.adapter = adapter
}

object GlobalVariables{
    var globalClient: Client? = null
    var globalUser: User? = null
    var globalString:String = ""
    var globalPosition = 0
    var globalId = ""

}

