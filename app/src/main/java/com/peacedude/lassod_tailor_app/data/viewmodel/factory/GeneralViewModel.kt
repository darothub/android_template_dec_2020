package com.peacedude.lassod_tailor_app.data.viewmodel.factory

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.storage.checkData
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import com.peacedude.lassod_tailor_app.ui.MainActivity
import com.peacedude.lassod_tailor_app.utils.bearer
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.peacedude.lassod_tailor_app.utils.newClientKey
import com.peacedude.lassod_tailor_app.utils.profileDataKey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


open class GeneralViewModel @Inject constructor(
    open var retrofit: Retrofit,
    private val storageRequest: StorageRequest
) : ViewModel(), ViewModelInterface {
    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient
    protected open val title: String = this.getName()

    //    val mGoogleSignInClient by lazy{ GoogleSignIn.getClient(, gso)}
    private val logoutLiveData = MutableLiveData<Boolean>()
    override var lastFragmentId: Int?
        get() = storageRequest.getLastFragmentId()
        set(id) = storageRequest.saveLastFragment(id)

    final override var currentUser: User? = storageRequest.checkData<User>(loggedInUserKey)
        set(currentUser) = storageRequest.keepData(currentUser, loggedInUserKey)

    override var saveUser = storageRequest.saveData(currentUser, loggedInUserKey)

    override var profileData = storageRequest.checkData<User>(profileDataKey)
        set(currentUser) = storageRequest.keepData(currentUser, profileDataKey)

    final override var header: String? = "$bearer ${currentUser?.token}"

    final override var newClient: Client? = storageRequest.checkData<Client>(newClientKey)
        set(currentClient) = storageRequest.keepData(currentClient, newClientKey)
    override var lastLoginForm: String? = storageRequest.getLastLoginForm()
        set(lastLoginForm) = storageRequest.saveLastLoginForm(lastLoginForm.toString())
    override var saveClient = storageRequest.saveData(newClient, newClientKey)

    protected fun onFailureResponse(
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
        t: Throwable
    ) {
        Log.i(title, "Throwable ${t.localizedMessage}")
        responseLiveData.postValue(ServicesResponseWrapper.Error("Bad connection, unable to connect", 0, null))
    }

    protected fun onResponseTask(
        response: Response<ParentData>,
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>
    ) {
        val res = response.body()
        val statusCode = response.code()
        Log.i(title, "${response.code()}")
        Log.i(title, "errorbody ${response.raw()}")


        when (statusCode) {
            in 400..499-> {
                try {
                    if(statusCode == 401){
                        val err = errorConverter(response)
                        responseLiveData.postValue(
                            ServicesResponseWrapper.Logout(
                                "Access Denied",
                                err.second
                            )
                        )
                    }
                    else{
                        val err = errorConverter(response)
                        responseLiveData.postValue(ServicesResponseWrapper.Error(err.first, err.second))
                    }

                } catch (e: Exception) {
                    Log.i(title, "Hello" + " " + e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }
            }
            in 500..599-> {
                val err = errorConverter(response)
                responseLiveData.postValue(ServicesResponseWrapper.Error("Internal server error", err.second))
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

    protected fun errorConverter(
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
                    activity.gdToast("Sign-out request successful", Gravity.BOTTOM)
                    activity.finish()
                    logoutLiveData.postValue(true)
                    Log.i(title, "logout")
                }
            }
        }


        return logoutLiveData
    }

    protected fun logout(): LiveData<Boolean> {
        currentUser?.token = ""
        currentUser?.loggedIn = false
        val res = storageRequest.saveData(currentUser, loggedInUserKey)
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

   protected inline fun<reified  T> enqueueRequest(
        request: Call<UserResponse<T>>,
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>
    ): MutableLiveData<ServicesResponseWrapper<ParentData>> {
        request.enqueue(object : Callback<UserResponse<T>> {
            override fun onFailure(call: Call<UserResponse<T>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(
                call: Call<UserResponse<T>>,
                response: Response<UserResponse<T>>
            ) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }

}