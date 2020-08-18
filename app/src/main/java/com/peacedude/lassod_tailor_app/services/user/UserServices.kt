package com.peacedude.lassod_tailor_app.services.user

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserServices {
    @POST("auth/signup")
    fun registerUser(
        @Body user: User
    ): Call<UserResponse>

    @POST("auth/signin")
    @FormUrlEncoded
    fun loginRequest(
        @Field("phoneNumber") phone:String,
        @Field("password") password:String
    ):Call<UserResponse>

}