package com.peacedude.lassod_tailor_app.data.viewmodel.user

import android.util.Log
import androidx.lifecycle.*
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.safeApiCall
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.*
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRequestInterface: UserRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest
) : GeneralViewModel(retrofit,storage), ViewModelInterface {


    override val title: String by lazy {
        this.getName()
    }
    val registeringUser:String by lazy {
        "registeringUser"
    }
    var storeData:User? = storage.checkUser(registeringUser)
        set(user) = storage.keepData(user, registeringUser)

    val clearSavedUser= storage.clearByKey(registeringUser)

    override fun registerUser(user: User?): LiveData<ServicesResponseWrapper<ParentData>> {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.registerUser(
            user)
        request.enqueue(object : Callback<UserResponse<User>> {
            override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(call: Call<UserResponse<User>>, response: Response<UserResponse<User>>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }

    override fun registerUser(
        header: String,
        user: User?
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.registerUser(header,
            user)
        request.enqueue(object : Callback<UserResponse<User>> {
            override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(call: Call<UserResponse<User>>, response: Response<UserResponse<User>>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }


    override fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String


    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.registerUser(
            firstName,
            lastName,
            otherName,
            category,
            phoneNumber,
            password
        )
        request.enqueue(object : Callback<UserResponse<User>> {
            override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(call: Call<UserResponse<User>>, response: Response<UserResponse<User>>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }
    override fun activateUser(phoneNumber:String, code: String): LiveData<ServicesResponseWrapper<ParentData>> {
        Log.i(title, "$phoneNumber $code")
        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.activateUser(phoneNumber, code)
        request.enqueue(object : Callback<UserResponse<User>> {
            override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
                Log.i(title, "Error $t")
            }
            override fun onResponse(call: Call<UserResponse<User>>, response: Response<UserResponse<User>>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }

    fun loginUserRequest(
        emailOrPhone: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = userRequestInterface.loginRequest(emailOrPhone, password)
        request.enqueue(object : Callback<UserResponse<User>> {
            override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
                Log.i(title, "ErrorViewModel $t")
            }
            override fun onResponse(call: Call<UserResponse<User>>, response: Response<UserResponse<User>>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }


}