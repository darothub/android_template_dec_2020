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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.helpers.SingleLiveEvent
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
import kotlinx.coroutines.launch
import retrofit2.*
import java.io.Reader
import javax.inject.Inject
import kotlin.properties.Delegates


open class GeneralViewModel @Inject constructor(
    open var retrofit: Retrofit,
    private val storageRequest: StorageRequest,
    private val context: Context,
    private val mGoogleSignInClient: GoogleSignInClient
) : ViewModel(), ViewModelInterface {


    protected open val title: String = this.getName()


    init{
        networkMonitor()
    }

    //    val mGoogleSignInClient by lazy{ GoogleSignIn.getClient(, gso)}
    private val logoutLiveData = MutableLiveData<Boolean>()
    val netWorkLiveData = MutableLiveData<Boolean>(true)
    var data:ParentData?=null
    override var lastFragmentId: Int
        get() = storageRequest.getLastFragmentId()
        set(id) = storageRequest.saveLastFragment(id)

    var curUser: User by Delegates.vetoable(User()) { property, oldValue, newValue ->
        newValue != oldValue
    }

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


//    val responseLiveData by lazy{
//        SingleLiveEvent<ServicesResponseWrapper<ParentData>>()
//    }

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
        responseLiveData: SingleLiveEvent<ServicesResponseWrapper<ParentData>>
    ) {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        i(title, "ViewModel ON")
        val res = response.body()
        val statusCode = response.code()
        val msg = response.message()
        val errorMsg = response.raw().message
        val errorbody = response.errorBody()?.charStream()
        Log.i(title, "messages $msg $errorMsg")
        Log.i(title, "errorbody ${response.raw().message}")

        try {
            when (statusCode) {
                in 400..499 -> {
                    if (statusCode == 401) {
                        val err = errorConverter(statusCode, errorbody)
                        responseLiveData.value =
                            ServicesResponseWrapper.Logout(
                                "Access Denied",
                                statusCode
                            )
                        logout()

                    } else {
                        val err = errorConverter(statusCode, errorbody)
                        responseLiveData.value =
                            ServicesResponseWrapper.Error(
                                err.first,
                                statusCode
                            )

                    }

                }
                in 500..599 -> {
                    val err = errorConverter(statusCode, errorbody)
                    responseLiveData.postValue(
                        ServicesResponseWrapper.Error(
                            msg,
                            err.second
                        )
                    )
                }
                else -> {
                    try {
                        Log.i(title, "success $res")
                        responseLiveData.postValue(ServicesResponseWrapper.Success(res))
                    } catch (e: java.lang.Exception) {
                        Log.i(title, "GeneralViewModel success exception ${e.message.toString()}")
                    }
                }
            }

        } catch (e: Exception) {
            Log.i(title, "ViewModel Exception" + " " + e.message.toString())
            responseLiveData.value = ServicesResponseWrapper.Error(msg, statusCode)
        }



    }
    protected suspend inline fun <reified T:ParentData>FlowCollector<ServicesResponseWrapper<T>>.onErrorFlowResponse(
        e: HttpException
    ) {
        val msg = e.response()?.message()
        val errorbody = e.response()?.errorBody()?.charStream()
        val code = e.code()
        Log.e("Viewmodel error", "${e.response()}")
        try{
            val error = errorConverter(code, errorbody)
            when(error.second){
                401 ->{
                    emit(
                        ServicesResponseWrapper.Logout(
                            msg.toString(),
                            code
                        )
                    )
                    logout()
                }
                else->{
                    emit(
                        ServicesResponseWrapper.Error(
                            error.first,
                            code
                        )
                    )
                }
            }
        }
        catch (e:Exception){
            emit(
                ServicesResponseWrapper.Error(
                    msg,
                    code
                )
            )
            Log.i(title, "CaughtError $msg")
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
        netWorkLiveData.observeForever {
            viewModelScope.launch {
                if(it){

                    if (data != null){

                        emit(
                            ServicesResponseWrapper.Success(data as? ParentData)
                        )
                    }
                    else{
                        emit(
                            ServicesResponseWrapper.Success(request as? ParentData)
                        )
                    }

                    i(title, "Network is ON here flow")
                }
                else{
                    i(title, "Network is OFF here flow")
                    emit(
                        ServicesResponseWrapper.Network(502, "Bad connection")
                    )
                }
            }

        }


    }
//    protected fun errorConverter(
//        response: Response<ParentData>
//    ): Pair<String, Int> {
//        val a = object : Annotation {}
//        val converter =
//            retrofit.responseBodyConverter<ErrorModel>(ErrorModel::class.java, arrayOf(a))
//        val error = converter.convert(response.errorBody())
//        val errors = error?.errors
//        val msg = error?.message
//        Log.i(title, "messages $msg")
//        var errorString1 = ""
//        if (error?.errors?.size!! > 1) {
//            errorString1 = "${errors?.get(0)}, ${errors?.get(1)}"
//        } else {
//            errorString1 = errors?.get(0).toString()
//        }
//        Log.i(title, "message $errorString1")
//        return Pair(errorString1, response.code())
//
//    }
    protected fun errorConverter(
        statusCode:Int,
        errorbody: Reader?
    ): Pair<String?, Int> {
        val gson = Gson()
        val type = object : TypeToken<ErrorModel>() {}.type
        val errorResponse: ErrorModel? = gson.fromJson(errorbody, type)
        val errorBodyString = "Invalid session"
        var er:String by Delegates.vetoable(errorBodyString, {property, oldValue, newValue ->
            newValue != "jwt malformed"
        })

        er = errorResponse?.errors?.get(0).toString()

        return Pair(er, statusCode)

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
        responseLiveData: SingleLiveEvent<ServicesResponseWrapper<ParentData>>

    ): SingleLiveEvent<ServicesResponseWrapper<ParentData>> {
        request.enqueue(object : Callback<UserResponse<T>> {
            override fun onFailure(call: Call<UserResponse<T>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(
                call: Call<UserResponse<T>>,
                response: Response<UserResponse<T>>
            ) {
                netWorkLiveData.observeForever {
                    if(it){
                        onResponseTask(response as Response<ParentData>, responseLiveData)
                        i(title, "Network is ON here")
                    }
                    else{
                        responseLiveData.postValue(ServicesResponseWrapper.Network(502, "Bad connection"))
                        i(title, "Network is OFF here")
                    }
                }

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

    override fun onCleared() {
        super.onCleared()
        netWorkLiveData.removeObserver {  }
    }


}