package com.peacedude.lassod_tailor_app.services.auth

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface AuthServices {
    @GET("auth/profile/me")
    fun getUserData(@Header("Authorization") header:String): Call<UserResponse>

    @PATCH("auth/profile/me")
    fun updateUserData(@Header("Authorization") header:String, @Body user: User): Call<UserResponse>
}

