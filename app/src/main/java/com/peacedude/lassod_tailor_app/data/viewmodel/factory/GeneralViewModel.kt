package com.peacedude.lassod_tailor_app.data.viewmodel.factory

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.utils.bearer
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.peacedude.lassod_tailor_app.utils.profileDataKey
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


open class GeneralViewModel @Inject constructor(
    open var retrofit: Retrofit,
    val storageRequest: StorageRequest
) : ViewModel(), ViewModelInterface {
    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient
    open val title: String by lazy {
        this.getName()
    }

    //    val mGoogleSignInClient by lazy{ GoogleSignIn.getClient(, gso)}
    private val logoutLiveData = MutableLiveData<Boolean>()
    final override var currentUser: User? = storageRequest.checkUser(loggedInUserKey)
        set(currentUser) = storageRequest.keepData(currentUser, loggedInUserKey)

    override var saveUser = storageRequest.saveData(currentUser, loggedInUserKey)

    override var profileData = storageRequest.checkUser(profileDataKey)
        set(currentUser) = storageRequest.keepData(currentUser, profileDataKey)

    override var header: String? = null
        get() = "$bearer ${currentUser?.token}"


    fun onFailureResponse(
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
        t: Throwable
    ) {
        Log.i(title, "Throwable ${t.localizedMessage}")
        responseLiveData.postValue(ServicesResponseWrapper.Error(t.localizedMessage, 0, null))
    }

    fun onResponseTask(
        response: Response<ParentData>,
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>
    ) {
        val res = response.body()
        val statusCode = response.code()
        Log.i(title, "${response.code()}")
        Log.i(title, "errorbody ${response.raw()}")


        when (statusCode) {

            401 -> {
                try {

                    val err = errorConverter(response)
                    logout()
                    responseLiveData.postValue(
                        ServicesResponseWrapper.Logout(
                            err.first,
                            err.second
                        )
                    )
                } catch (e: Exception) {
                    Log.i(title, e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }

            }
            404 -> {
                try {

                    responseLiveData.postValue(
                        ServicesResponseWrapper.Error(
                            response.message(),
                            404
                        )
                    )
                } catch (e: Exception) {
                    Log.i(title, "Hello" + " " + e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }
            }
            in 400..599 -> {
                try {
                    val err = errorConverter(response)
                    responseLiveData.postValue(ServicesResponseWrapper.Error(err.first, err.second))
                } catch (e: Exception) {
                    Log.i(title, "Hello" + " " + e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }


            }
            else -> {
                try {
                    Log.i(title, "success $res")
                    responseLiveData.postValue(ServicesResponseWrapper.Success(res))
                } catch (e: java.lang.Exception) {
                    Log.i(title, e.message.toString())
                }


            }
        }

    }

    fun errorConverter(
        response: Response<ParentData>
    ): Pair<String, Int> {
        val a = object : Annotation {}
        val converter =
            retrofit.responseBodyConverter<ErrorModel>(ErrorModel::class.java, arrayOf(a))
        val error = converter.convert(response.errorBody())
        val errors = error?.errors
        var errorString1 = ""
        if (error?.errors?.size!! > 1) {
            errorString1 = "${errors?.get(0)}, ${errors?.get(1)}"
        } else {
            errorString1 = errors?.get(0).toString()
        }
        Log.i(title, "message $errorString1")
        return Pair(errorString1, response.code())

    }

    fun logout(activity: Activity): LiveData<Boolean> {

        currentUser?.token = ""
        currentUser?.loggedIn = false
        val res = storageRequest.saveData(currentUser, loggedInUserKey)
        Log.i(title, "resArray ${res.size}")
        mGoogleSignInClient.signOut().addOnCompleteListener { logoutTask ->
            when (logoutTask.isSuccessful) {
                true -> {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.gdToast("Sign-out not request successful", Gravity.BOTTOM)
                    activity.finish()
                    logoutLiveData.postValue(true)
                    Log.i(title, "logout")
                }
            }
        }


        return logoutLiveData
    }

    fun logout(): LiveData<Boolean> {
        currentUser?.token = ""
        currentUser?.loggedIn = false
        val res = storageRequest.saveData(currentUser, loggedInUserKey)
        Log.i(title, "resArray2 ${res.size}")
        mGoogleSignInClient.signOut().addOnCompleteListener { logoutTask ->
            when (logoutTask.isSuccessful) {
                true -> {
                    logoutLiveData.postValue(true)
                    Log.i(title, "logout")
                }
            }
        }
        return logoutLiveData
    }

//    suspend fun LiveDataScope<ServicesResponseWrapper<ParentData>>.onResponse(
//        status: Int?,
//        error: List<String>?,
//        registration: UserResponse
//    ) {
//        when (status) {
//
//            401 -> {
//                try {
//
//                    emit(ServicesResponseWrapper.Logout(error?.joinToString().toString()))
//                } catch (e: Exception) {
//                    Log.i(title, e.message.toString())
//                    emit(ServicesResponseWrapper.Error(e.message, status))
//                }
//            }
//            in 400..599 -> {
//                try {
//                    emit(ServicesResponseWrapper.Error(error?.joinToString().toString()))
//                } catch (e: Exception) {
//                    Log.i(title, "Hello" + " " + e.message.toString())
//                    emit(ServicesResponseWrapper.Error(e.message, status))
//                }
//            }
//            else -> {
//                try {
//                    emit(ServicesResponseWrapper.Success(registration))
//                } catch (e: java.lang.Exception) {
//                    Log.i(title, e.message.toString())
//                }
//            }
//        }
//    }


//    return try {
//        val a = object : Annotation{}
//        val converter = retrofit.responseBodyConverter<ErrorModel>(ErrorModel::class.java, arrayOf(a))
//        val error = converter.convert(throwable.response()?.errorBody())
//        val errors = error?.errors
//        var errorString1 = if (error?.errors?.size!! > 1) {
//            "${errors?.get(0)}, ${errors?.get(1)}"
//        } else {
//            errors?.get(0).toString()
//        }
//        Log.i(title, "message $errorString1")
//        return Pair(errorString1, throwable.code())
////            throwable.response()?.errorBody()?.source()?.let {
////
////
////        //                val moshiAdapter = Moshi.Builder().build().adapter(ErrorModel::class.java)
////        //                moshiAdapter.fromJson(it)
////            }!!
//    } catch (exception: Exception) {
//        Pair(exception.localizedMessage, throwable.code())
//    }
}