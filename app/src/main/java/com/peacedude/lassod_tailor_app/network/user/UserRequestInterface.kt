package com.peacedude.lassod_tailor_app.network.user

import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call

interface UserRequestInterface {

    fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phone: String,
        password: String
    ): Call<UserResponse> {
        return TODO()
    }
}