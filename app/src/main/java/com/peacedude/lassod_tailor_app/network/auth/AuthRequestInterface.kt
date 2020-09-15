package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call

interface AuthRequestInterface {
    fun getUserData(header:String): Call<UserResponse> {
        return TODO()
    }

    fun updateUserData(header:String, user: User): Call<UserResponse> {
        return TODO()
    }
    fun forgetPassword(email:String): Call<UserResponse> {
        return TODO()
    }
}