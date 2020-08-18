package com.peacedude.lassod_tailor_app.network.user

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call

interface UserRequestInterface {

    fun registerUser(
        user:User): Call<UserResponse>{
        return TODO()
    }

    fun loginRequest(phoneNumber:String, password: String): Call<UserResponse> {
        return TODO()
    }

}