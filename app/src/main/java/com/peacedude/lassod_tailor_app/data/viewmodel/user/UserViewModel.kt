package com.peacedude.lassod_tailor_app.data.viewmodel.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.model.error.ErrorModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class UserViewModel @Inject constructor(
    val userRequestInterface: UserRequestInterface,
    val retrofit: Retrofit
//    val storage: StorageRequest
) : ViewModel() {


    val title: String by lazy {
        this.getName()
    }
//    val registeringUser:String by lazy {
//        "registeringUser"
//    }
//    var registerFormData:User? = storage.checkUser(registeringUser)
//        set(user) = storage.keepData(user, registeringUser)
//
//    val clearSavedUser= storage.clearByKey<User>(registeringUser)
//

    fun registerUser(user: User): LiveData<ServicesResponseWrapper<ParentData>>  {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.registerUser(user)
        request.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }
    fun activateUser(phoneNumber: String, code:String): LiveData<ServicesResponseWrapper<ParentData>> {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.activateUser(phoneNumber, code)
        request.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
                Log.i(title, "Error $t")
            }
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }

    fun loginUserRequest(
        phoneNumber: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.loginRequest(phoneNumber, password)
        request.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
                Log.i(title, "Error $t")
            }
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }

    internal fun onFailureResponse(
        responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>,
        t: Throwable
    ) {
        responseLiveData.postValue(ServicesResponseWrapper.Error(t.localizedMessage, 0, null))
    }
    internal fun onResponseTask(response: Response<ParentData>, responseLiveData: MutableLiveData<ServicesResponseWrapper<ParentData>>){
        val res = response.body()
        val statusCode = response.code()
        Log.i(title, "${response.code()}")
        when(statusCode) {

            401 -> {
                try {
                    Log.i(title, "errorbody ${response.raw()}")
                    val a = object : Annotation{}
                    val converter = retrofit.responseBodyConverter<ErrorModel>(
                        ErrorModel::class.java, arrayOf(a))
                    val error = converter.convert(response.errorBody())

                    Log.i(title, "message ${error!!.errors?.get(0)}")
                    responseLiveData.postValue(ServicesResponseWrapper.Logout(error.errors?.get(0).toString()))
                }
                catch (e:Exception){
                    Log.i(title, e.message.toString())
                }

            }
            in 400..501 ->{
                try {
                    Log.i(title, "errorbody ${response.raw()}")
                    val a = object : Annotation{}
                    val converter = retrofit.responseBodyConverter<ErrorModel>(
                        ErrorModel::class.java, arrayOf(a))
                    val error = converter.convert(response.errorBody())

                    Log.i(title, "message ${error!!.errors?.get(0)}")
                    responseLiveData.postValue(ServicesResponseWrapper.Error(error.errors?.get(0).toString()))
                }
                catch (e:Exception){
                    Log.i(title, e.message.toString())
                }

            }
            else -> {
                try {
                    Log.i(title, "errors ${response.errorBody()}")
                    responseLiveData.postValue(ServicesResponseWrapper.Success(res))
                }catch (e:java.lang.Exception){
                    Log.i(title, e.message.toString())
                }


            }
        }

    }
}