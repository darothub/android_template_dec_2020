package com.peacedude.lassod_tailor_app.services.auth

import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface AuthServices {
    @GET("profile/me")
    fun getUUserData(@Header("Authorization") header:String): Call<UserResponse>
}

