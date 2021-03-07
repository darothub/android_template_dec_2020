package com.peacedude.lassod_tailor_app.helpers

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.*
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.MeasurementValues
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.ui.REQUEST_CODE
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

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
inline fun <reified T> Activity.observeRequest(
    request: LiveData<ServicesResponseWrapper<ParentData>>?,
    progressBar: ProgressBar?, button: Button?, loader: Boolean = false,
    crossinline success: (UserResponse<T>) -> Unit,
    crossinline error: (String) -> Unit
) {
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
    val textView by lazy {
        dialog.findViewById<TextView>(R.id.loader_layout_tv)
    }


    hideKeyboard()
    request?.observe(this as LifecycleOwner, Observer {
        try {
            val responseData = it.data
            val errorResponse = it.message
            val errorCode = it.code

            when (it) {
                is ServicesResponseWrapper.Loading<*> -> {
                    if (loader) dialog.show()
                    progressBar?.show()
                    button?.hide()
                    i(title, "Loading..")
                }
                is ServicesResponseWrapper.Success -> {
                    dialog.dismiss()
                    progressBar?.hide()
                    button?.show()

                    responseData as UserResponse<T>
                    success(responseData)
                    i(title, "success ${it.data}")


                }
                is ServicesResponseWrapper.Error -> {
                    dialog.dismiss()
                    progressBar?.hide()
                    button?.show()
//                    gdErrorToast("$errorResponse", Gravity.BOTTOM)
                    error(errorResponse.toString())
                    Log.i(title, "Error ${it.message}")

                }
                is ServicesResponseWrapper.Logout -> {
                    dialog.dismiss()
//                    val unAuthorizedString = getString(R.string.unauthorized_user)
                    progressBar?.hide()
                    button?.show()
                    gdToast(errorResponse.toString(), Gravity.BOTTOM)
                    goto(MainActivity::class.java)
                    finish()
//                    result.postValue(Pair(false, errorResponse.toString()))
                    i(title, "Log out ${errorResponse.toString()}")


//                    navigateWithUri("android-app://anapfoundation.navigation/signin".toUri())
                }
                is ServicesResponseWrapper.Network -> {

                    i(title, "Network is bad here")
                    textView.text = getString(R.string.bad_network)
                    dialog.show()
                }
            }
        } catch (e: Exception) {
            progressBar?.hide()
            dialog.dismiss()
            button?.show()
            i(title, e.localizedMessage ?: "")
        }

    })
}

fun Activity.requestObserver(
    progressBar: ProgressBar?,
    btn: Button?,
    req: LiveData<ServicesResponseWrapper<ParentData>>,
    loader: Boolean = false,
    action: (Boolean, Any?) -> Unit
) {
//
//    val response = observeRequest(req, progressBar, btn, loader)
//    response.observe(this as LifecycleOwner, Observer {
//        val (bool, result) = it
//        action(bool, result)
//    })
}


/**
 * Handles sign-up request live response
 *
 * @param bool
 * @param result
 */
inline fun <reified T> Activity.onRequestResponseTask(
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

fun Any.i(tag: String, message: String) {
    Log.i(tag, message)
}

fun Any.e(tag: String, message: String) {
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

fun Activity.saveBitmap(bmp: Bitmap?): File? {
    val extStorageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    var outStream: OutputStream? = null
    var file: File? = null
    val time = System.currentTimeMillis()

    val child = "JPEG_${time}_.jpg"
    // String temp = null;
    if (extStorageDirectory != null) {
        file = File(extStorageDirectory, child)
        if (file.exists()) {
            file.delete()
            file = File(extStorageDirectory, child)
        }
        try {
            outStream = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
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
    if (Build.VERSION.SDK_INT < 28) {
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

fun Activity.setUpCountrySpinnerWithDialCode(header: String, spinner: Spinner) {
    val locale = Locale.getAvailableLocales()
    val countriesIsoAndName: HashMap<String, String> = HashMap()
    locale.associateByTo(countriesIsoAndName, {
        it.displayCountry
    }, {
        "${it.displayCountry}(+${
            PhoneNumberUtil.createInstance(this).getCountryCodeForRegion(it.country)
        })"
    })
    val countries = countriesIsoAndName.values.sorted().toMutableList()
    countries.add(0, header)
    val adapter = ArrayAdapter(this, R.layout.spinner_colored_text_layout, countries)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
    spinner.adapter = adapter

}

fun Activity.setUpCountrySpinner(header: String, spinner: Spinner) {
    val locale = Locale.getAvailableLocales()
    var countriesIsoAndName: HashMap<String, String> = HashMap()
    locale.associateByTo(countriesIsoAndName, {
        it.displayCountry
    }, {
        it.displayCountry
    })
    val countries = countriesIsoAndName.values.sorted().toMutableList()
    countries.add(0, header)
    val adapter = ArrayAdapter(this, R.layout.spinner_colored_text_layout, countries)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
    spinner.adapter = adapter
}

fun Activity.setUpSpinnerWithList(
    header: String? = null,
    spinner: Spinner,
    list: ArrayList<String>
) {


    if (header != null) {
        list.add(0, header)
    }
    val adapter = ArrayAdapter(this, R.layout.spinner_colored_text_layout, list)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
    spinner.adapter = adapter
}

object GlobalVariables {
    var globalWrapper: LiveData<ServicesResponseWrapper<ParentData>>? = null
    var globalClient: Client? = null
    var globalClientList: List<Client?>? = null
    var globalListOfString: List<String>? = null
    var globalMeasuremenValues: MeasurementValues? = null
    var globalUser: User? = null
    var globalPhoto: Photo? = null
    var globalVideo: VideoResource? = null
    var globalArticle: Article? = null
    var gloabalToken: String? = ""
    var globalArticleList: List<CommonMediaClass>? = null
    var globalArticlesList: List<Article>? = null
    var globalVideosList: List<VideoResource>? = null
    var globalVideoList: List<CommonMediaClass>? = null
    var globalString: String = ""
    var globalString1: String = ""
    var globalArticleListLiveData = MutableLiveData<List<Article>>()
    var globalVideoListLiveData = MutableLiveData<List<CommonMediaClass>>()
    var globalPosition = 0
    var globalId = ""
    var globalAddCardRes: AddCardRes? = null

}

fun Activity.networkMonitor(): MutableLiveData<Boolean> {
    val netWorkLiveData = MutableLiveData<Boolean>()

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            //take action when network connection is gained
            i("Network", "onAvailable")
            netWorkLiveData.postValue(true)
        }

        override fun onLost(network: Network) {
            //take action when network connection is lost
            i("Network", "onLost")
            netWorkLiveData.postValue(false)
        }

    }

    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.let {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            it.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request: NetworkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            it.registerNetworkCallback(request, networkCallback)
        }
    }
    return netWorkLiveData
}

fun getBitmapFromImageView(view: ImageView): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val bgDrawable = view.background
    if (bgDrawable != null) {
        bgDrawable.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }
    view.draw(canvas)
    return bitmap
}

suspend inline fun <reified T> Activity.onFlowResponse(
    button: Button? = null,
    progressBar: ProgressBar? = null,
    loader: Boolean = false,
    response: ServicesResponseWrapper<out ParentData>,
    noinline error: ((String) -> Unit)? = null,
    success: (T?) -> Unit

) {

    val dialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
    val textView by lazy {
        dialog.findViewById<TextView>(R.id.loader_layout_tv)
    }

    when (response) {
        is ServicesResponseWrapper.Loading<*> -> {
//            if (loader) dialog.show() else dialog.dismiss()
            dialog.show()
            progressBar?.show()
            button?.hide()
            delay(1000)
            Log.i("onFlowResponse", "Loading ${response.message} dialog showin ${dialog.isShowing}")
        }

        is ServicesResponseWrapper.Error -> {
            dialog.dismiss()
            progressBar?.hide()
            button?.show()
            error(response.message.toString())
//            gdToast(it.message.toString(), Gravity.BOTTOM)
            Log.e("onFlowResponse", "Error ${response?.message}")
        }
        is ServicesResponseWrapper.Logout -> {
            dialog.dismiss()
            progressBar?.hide()
            button?.show()
            goto(MainActivity::class.java)
            this.finish()
            Log.i("onFlowResponse", "Logout ${response?.message}")
        }
        is ServicesResponseWrapper.Network -> {

            i("ActivityOnFlow", "Network is bad here")
            textView.text = getString(R.string.bad_network)
            dialog.show()
        }
        is ServicesResponseWrapper.Success -> {
            dialog.dismiss()
            progressBar?.hide()
            button?.show()

            if (response.data != null) {
                success(response.data as T)
            }


            Log.i("onFlowResponse", "Success ${response?.data} dialog showin ${dialog.isShowing}")
        }
    }

}

inline fun <T : ParentData> Activity.observeResponseState(
    state: StateFlow<ServicesResponseWrapper<ParentData>>,
    progressBar: ProgressBar?,
    button: Button?,
    crossinline success: (UserResponse<T>) -> Unit,
    noinline error: ((String?) -> Unit)? = null
) {

    this as LifecycleOwner
    lifecycleScope.launchWhenStarted {
        val d = showOrHideLoader()
        state.collectLatest {
            when (it) {
                is ServicesResponseWrapper.Loading<*>  -> {
                    i("Stateflow", "Loading")
                    d.second.text = it.message
                    d.first.show()
                    toggleProgressBarAndButton(progressBar, button)
                }

                is ServicesResponseWrapper.Error -> {
                    i("Stateflow", "Error")
                    d.first.dismiss()
                    toggleProgressBarAndButton(progressBar, button)
                    if (error != null) {
                        val data = it.data as UserResponse<T>
                        error(it.data.message)
                    }
                }
                is ServicesResponseWrapper.Success -> {
                    i("Stateflow", "Success")
                    d.first.dismiss()
                    val data = it.data as UserResponse<T>
                    toggleProgressBarAndButton(progressBar, button)
                    success(data)

                }
                is ServicesResponseWrapper.Logout -> {
                    i("Stateflow", "Logout")
                    d.first.dismiss()
                    toggleProgressBarAndButton(progressBar, button)

                }
                is ServicesResponseWrapper.Network -> {
                    i("Stateflow", "Network")
                    d.first.show()
                    d.second.text = it.message
                    toggleProgressBarAndButton(progressBar, button)
                }

                else -> {
                    showOrHideLoader()
                }
            }
        }
    }
}



fun Activity.toggleProgressBarAndButton(progressBar: ProgressBar?, button: Button?){
   if(progressBar?.show() == true){
       button?.show()
       progressBar?.hide()
   }
    else{
       button?.hide()
       progressBar?.show()
   }
}

fun Activity.showOrHideLoader(message: String?=null):Pair<Dialog, TextView>{
    val dialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
    val textView by lazy {
        dialog.findViewById<TextView>(R.id.loader_layout_tv)
    }
    if (message.isNullOrEmpty()){
    }
    else{
        textView.text = message
    }
    return Pair(dialog, textView)
}



fun Activity.goto(destination: Class<*>) {
    startActivity(Intent(this, destination))
}