package com.peacedude.lassod_tailor_app.data.viewmodel.factory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.peacedude.lassod_tailor_app.utils.profileDataKey
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

open class GeneralViewModel @Inject constructor(open var retrofit: Retrofit, val storageRequest: StorageRequest): ViewModel() {

    open val title: String by lazy {
        this.getName()
    }
    var currentUser: User? = storageRequest.checkUser(loggedInUserKey)
        set(currentUser) = storageRequest.keepData(currentUser, loggedInUserKey)

    var saveUser = storageRequest.saveData(currentUser, loggedInUserKey)

    var profileData = storageRequest.checkUser(profileDataKey)
        set(currentUser) = storageRequest.keepData(currentUser, profileDataKey)

    fun onFailureResponse(
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
        t: Throwable
    ) {
        Log.i(title, "Throwable $t")
        responseLiveData.postValue(ServicesResponseWrapper.Error(t.localizedMessage, 0, null))
    }
    fun onResponseTask(response: Response<ParentData>, responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>){
        val res = response.body()
        val statusCode = response.code()
        Log.i(title, "${response.code()}")
        Log.i(title, "errorbody ${response.raw()}")


        when(statusCode) {

            401 -> {
                try {

                    val err = errorConverter(response)
                    logout()
                    responseLiveData.postValue(ServicesResponseWrapper.Logout(err.first, err.second))
                }
                catch (e:Exception){
                    Log.i(title, e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }

            }
            in 400..599 ->{
                try{
                    val err = errorConverter(response)
                    responseLiveData.postValue(ServicesResponseWrapper.Error(err.first, err.second))
                }
                catch (e:Exception){
                    Log.i(title, "Hello" + " " + e.message.toString())
                    responseLiveData.postValue(ServicesResponseWrapper.Error(e.message, statusCode))
                }


            }
            else -> {
                try {
                    Log.i(title, "success ${response.errorBody()}")
                    responseLiveData.postValue(ServicesResponseWrapper.Success(res))
                }catch (e:java.lang.Exception){
                    Log.i(title, e.message.toString())
                }


            }
        }

    }

    fun errorConverter(
        response: Response<ParentData>
    ):Pair<String, Int> {
        val a = object : Annotation{}
        val converter = retrofit.responseBodyConverter<ErrorModel>(ErrorModel::class.java, arrayOf(a))
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

    fun logout(){
        currentUser?.token = ""
        currentUser?.loggedIn = false
        val res= storageRequest.saveData(currentUser, loggedInUserKey)
        Log.i(title, "resArray ${res.size}")
    }
}