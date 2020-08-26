package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call

interface AuthRequestInterface {
    fun getUserData(header:String): Call<UserResponse> {
        return TODO()
    }
}