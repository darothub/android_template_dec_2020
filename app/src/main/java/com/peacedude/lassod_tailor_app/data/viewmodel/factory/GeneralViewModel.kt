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
import com.peacedude.lassod_tailor_app.helpers.GlobalVariables
import com.peacedude.lassod_tailor_app.helpers.SingleLiveEvent
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.PhotoList
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import retrofit2.*
import java.io.Reader
import java.io.Serializable
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


    val logoutLiveData = MutableLiveData<Boolean>()
    val netWorkLiveData = MutableLiveData<Boolean>(true)


    final override var currentUser: User? = storageRequest.checkData<User>(loggedInUserKey) ?: User()
        set(currentUser) {
            field = currentUser
            storageRequest.keepData(currentUser, loggedInUserKey)
        }
    override var lastFragmentId: Int = storageRequest.getLastFragmentId(currentUser?.email ?:currentUser?.phone.toString())
        set(id) = storageRequest.saveLastFragment(currentUser?.email ?:currentUser?.phone.toString(), id)

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
    @ExperimentalCoroutinesApi
    suspend inline fun  ProducerScope<ServicesResponseWrapper<ParentData>>.onErrorFlowResponse(
        e: HttpException
    ) {
        val msg = e.response()?.message()
        val errorbody = e.response()?.errorBody()?.charStream()
        val code = e.code()
        Log.e("Viewmodel error", "${e.response()}")
        try{
            val error = `access$errorConverter`(code, errorbody)
            when(error.second){
                401 ->{
                    offer(
                        ServicesResponseWrapper.Logout(
                            msg.toString(),
                            code
                        )
                    )
                    `access$logout`()
                }
                else->{
                    offer(
                        ServicesResponseWrapper.Error(
                            error.first,
                            code
                        )
                    )
                }
            }
        }
        catch (e:Exception){
            send(
                ServicesResponseWrapper.Error(
                    msg,
                    code
                )
            )
            Log.i(`access$title`, "CaughtError $msg")
        }


    }
    @ExperimentalCoroutinesApi
    protected suspend inline fun <reified T> ProducerScope<ServicesResponseWrapper<ParentData>>.onSuccessFlowResponse(
        request: UserResponse<T>
    ){
        val data = request.data
        send(
            ServicesResponseWrapper.Loading(
                null,
                "Loading..."
            )
        )
        netWorkLiveData.observeForever {net->
            viewModelScope.launch {
                if(net){

                    if (data != null){
                        if(data is ArrayList<*>){
                            val d = Data(data)
                            send(
                                ServicesResponseWrapper.Success(d as? ParentData)
                            )
                        }
                        else{
                            send(
                                ServicesResponseWrapper.Success(data as? ParentData)
                            )
                        }


                    }
                    else{
                        send(
                            ServicesResponseWrapper.Success(request as? ParentData)
                        )
                    }

                    i(title, "Network is ON here flow")
                }
                else{
                    i(title, "Network is OFF here flow")
                    if(isClosedForSend){
                        close(Throwable())
                    }else{
                        offer(
                            ServicesResponseWrapper.Network(502, "Bad connection")
                        )
                    }
                }
            }

        }
        awaitClose { netWorkLiveData.removeObserver {  } }
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

        mGoogleSignInClient.signOut().addOnCompleteListener { logoutTask ->
            when (logoutTask.isSuccessful) {
                true -> {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.gdToast("Sign-out request successful", Gravity.BOTTOM)
                    currentUser?.token = ""
                    currentUser?.loggedIn = false
                    this.currentUser = currentUser
                    GlobalVariables.globalUser = currentUser
                    logoutLiveData.postValue(true)
                    activity.finish()
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

    @PublishedApi
    internal fun `access$errorConverter`(statusCode: Int, errorbody: Reader?) =
        errorConverter(statusCode, errorbody)

    @PublishedApi
    internal fun `access$logout`() = logout()

    @PublishedApi
    internal val `access$title`: String
        get() = title


}

data class Data(var data:ArrayList<*>):Serializable, ParentData