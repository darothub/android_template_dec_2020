package com.peacedude.lassod_tailor_app.data.viewmodel.factory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import android.view.Gravity
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.gdtoast.gdToast
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
import kotlinx.coroutines.flow.FlowCollector
import retrofit2.*
import javax.inject.Inject


open class GeneralViewModel @Inject constructor(
    open var retrofit: Retrofit,
    private val storageRequest: StorageRequest,
    private val context: Context,
    private val mGoogleSignInClient: GoogleSignInClient
) : ViewModel(), ViewModelInterface {


    protected open val title: String = this.getName()
    val nLiveData = MediatorLiveData<Boolean>()


    init {
        networkMonitor()

    }

    //    val mGoogleSignInClient by lazy{ GoogleSignIn.getClient(, gso)}
    private val logoutLiveData = MutableLiveData<Boolean>()
    val netWorkLiveData = MutableLiveData<Boolean>()
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
        responseLiveData.postValue(
            ServicesResponseWrapper.Error(
                "Bad connection, unable to connect",
                0
            )
        )
    }

    protected fun onResponseTask(
        response: Response<ParentData>,
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>
    ) {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        i(title, "ViewModel ON")
        val res = response.body()
        val statusCode = response.code()
        Log.i(title, "${response.code()}")
        Log.i(title, "errorbody ${response.raw()}")

        when (statusCode) {
            in 400..499 -> {
                try {
                    if (statusCode == 401) {
                        val err = errorConverter(response)
                        responseLiveData.value =
                            ServicesResponseWrapper.Logout(
                                "Access Denied",
                                err.second
                            )

                    } else {
                        val err = errorConverter(response)
                        responseLiveData.value =
                            ServicesResponseWrapper.Error(
                                err.first,
                                err.second
                            )

                    }

                } catch (e: Exception) {
                    Log.i(title, "Hello" + " " + e.message.toString())
                    responseLiveData.value = ServicesResponseWrapper.Error(e.message, statusCode)
                }
            }
            in 500..599 -> {
                val err = errorConverter(response)
                responseLiveData.postValue(
                    ServicesResponseWrapper.Error(
                        "Internal server error",
                        err.second
                    )
                )
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
    protected suspend inline fun <reified T:ParentData>FlowCollector<ServicesResponseWrapper<T>>.onErrorFlowResponse(
        e: HttpException
    ) {
        Log.e("Viewmodel error", "${e.response()}")
        try{
            val error = errorConverter(e.response() as Response<ParentData>)
            when(error.second){
                401 ->{
                    emit(
                        ServicesResponseWrapper.Logout(
                            "Access Denied",
                            error.second
                        )
                    )
                }
                else->{
                    emit(
                        ServicesResponseWrapper.Error(
                            error.first,
                            error.second
                        )
                    )
                }
            }
        }
        catch (e:Exception){
            Log.i(title, "CaughtError ${e.message}")
        }


    }

    protected suspend inline fun <reified T> FlowCollector<ServicesResponseWrapper<ParentData>>.onSuccessFlowResponse(
        request: UserResponse<T>
    ) {
        val data = request.data
        emit(
            ServicesResponseWrapper.Loading(
                null,
                "Loading..."
            )
        )
        emit(
            ServicesResponseWrapper.Success(data as ParentData)
        )
    }
    protected fun errorConverter(
        response: Response<ParentData>
    ): Pair<String, Int> {
        val a = object : Annotation {}
        val converter =
            retrofit.responseBodyConverter<ErrorModel>(ErrorModel::class.java, arrayOf(a))
        val error = converter.convert(response.errorBody())
        val errors = error?.errors
        val msg = error?.message
        Log.i(title, "messages $msg")
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

    protected inline fun <reified T> enqueueRequest(
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

    protected inline fun <reified T> enqueueRequest(
        request: Call<UserResponse<T>>,
        responseLiveData: ServicesResponseWrapper<ParentData>

    ): ServicesResponseWrapper<ParentData> {
        request.enqueue(object : Callback<UserResponse<T>> {
            override fun onFailure(call: Call<UserResponse<T>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<UserResponse<T>>,
                response: Response<UserResponse<T>>
            ) {


            }

        })
        return responseLiveData
    }

    protected fun networkMonitor(): MutableLiveData<Boolean> {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //take action when network connection is gained
//                i(title, "onAvailable")
                netWorkLiveData.postValue(true)
            }

            override fun onLost(network: Network) {
                //take action when network connection is lost
//                i(title, "onLost")
                netWorkLiveData.postValue(false)
            }

        }

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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


//    protected fun onFailureResponse(
//        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
//        t: Throwable
//    ) {
//        Log.i(title, "Throwable ${t.localizedMessage}")
//        responseLiveData.postValue(
//            ServicesResponseWrapper.Error(
//                "Bad connection, unable to connect",
//                0
//            )
//        )
//    }
//
//    protected fun onResponseTask(
//        response: Response<ParentData>,
//        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>
//    ) {
//        responseLiveData.value = ServicesResponseWrapper.Loading(
//            null,
//            "Loading..."
//        )
//        i(title, "ViewModel ON")
//        val res = response.body()
//        val statusCode = response.code()
//        Log.i(title, "${response.code()}")
//        Log.i(title, "errorbody ${response.raw()}")
//
//        when (statusCode) {
//            in 400..499 -> {
//                try {
//                    if (statusCode == 401) {
//                        val err = errorConverter(response)
//                        responseLiveData.value =
//                            ServicesResponseWrapper.Logout(
//                                "Access Denied",
//                                err.second
//                            )
//
//                    } else {
//                        val err = errorConverter(response)
//                        responseLiveData.value =
//                            ServicesResponseWrapper.Error(
//                                err.first,
//                                err.second
//                            )
//
//                    }
//
//                } catch (e: Exception) {
//                    Log.i(title, "Hello" + " " + e.message.toString())
//                    responseLiveData.value = ServicesResponseWrapper.Error(e.message, statusCode)
//                }
//            }
//            in 500..599 -> {
//                val err = errorConverter(response)
//                responseLiveData.postValue(
//                    ServicesResponseWrapper.Error(
//                        "Internal server error",
//                        err.second
//                    )
//                )
//            }
//            else -> {
//                try {
//                    Log.i(title, "success $res")
//                    responseLiveData.postValue(ServicesResponseWrapper.Success(res))
//                } catch (e: java.lang.Exception) {
//                    Log.i(title, e.message.toString())
//                }
//            }
//        }
//
//
//    }

}