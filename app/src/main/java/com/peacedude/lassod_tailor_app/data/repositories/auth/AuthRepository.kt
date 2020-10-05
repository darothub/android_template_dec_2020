package com.peacedude.lassod_tailor_app.data.repositories.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository@Inject constructor(private val authServices: AuthServices):AuthRequestInterface {
    val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()

    override fun getUserData(header: String): Call<UserResponse<User>> {
        return authServices.getUserData(header)
    }

    override fun updateUserData(header: String, user: User): Call<UserResponse<User>> {
        return authServices.updateUserData(header, user)
    }

    override fun forgetPassword(field: String): Call<UserResponse<String>> {
        return authServices.forgetPassword(field)
    }

    override fun resetPassword(token: String, password:String, cPassword: String): Call<UserResponse<String>> {
        return authServices.resetPassword(token, password, cPassword)
    }

    override fun addClient(header: String, client: Client): Call<UserResponse<Client>> {
        return authServices.addClient(header, client)
    }

    fun doSomething(header: String):LiveData<ServicesResponseWrapper<ParentData>>{
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authServices.getUserData(header)
        request.enqueue(object : Callback<UserResponse<User>> {
            override fun onFailure(call: Call<UserResponse<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<UserResponse<User>>, response: Response<UserResponse<User>>) {

            }

        })
        return responseLiveData
    }
}

