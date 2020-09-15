package com.peacedude.lassod_tailor_app.network.user

import androidx.lifecycle.LiveData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call

interface UserRequestInterface {

    fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String
    ): Call<UserResponse> {
        return TODO()
    }

    fun activateUser(phoneNumber: String, code: String): Call<UserResponse> {
        return TODO()
    }

    fun loginRequest(phoneNumber: String, password: String): Call<UserResponse> {
        return TODO()
    }

   fun registerUser(
        user: User?
    ):Call<UserResponse> {
        return TODO()
    }

}