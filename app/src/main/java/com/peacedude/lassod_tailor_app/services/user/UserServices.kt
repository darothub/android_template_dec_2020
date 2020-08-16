package com.peacedude.lassod_tailor_app.services.user

import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserServices {
    @POST("auth/signup")
    @FormUrlEncoded
    fun registerUser(
        @Field("firstName") first:String,
        @Field("lastName") last:String,
        @Field("otherName") otherName:String,
        @Field("category") category:String,
        @Field("phone") phone:String,
        @Field("password") password:String

    ): Call<UserResponse>
}