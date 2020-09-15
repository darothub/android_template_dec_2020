package com.peacedude.lassod_tailor_app.data.repositories.user

import android.util.Log
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.services.user.UserServices
import retrofit2.Call
import javax.inject.Inject

class UserRequestRepository @Inject constructor(private val userServices: UserServices):UserRequestInterface {
    override fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String
    ): Call<UserResponse> {
        return userServices.registerUser(firstName, lastName, otherName, category, phoneNumber, password)
    }

    override fun registerUser(user: User?): Call<UserResponse> {
        return userServices.registerUser(user)
    }

    override fun activateUser(phoneNumber: String, code: String): Call<UserResponse> {
        Log.i("Repository", "$phoneNumber $code")
        return userServices.activateUser(phoneNumber, code)
    }

    override fun loginRequest(phoneNumber: String, password: String): Call<UserResponse> {
        return userServices.loginRequest(phoneNumber, password)
    }


}