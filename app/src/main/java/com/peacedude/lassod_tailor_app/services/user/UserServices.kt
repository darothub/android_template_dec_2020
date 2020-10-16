package com.peacedude.lassod_tailor_app.services.user

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserServices {
    @POST("auth/signup")
    @FormUrlEncoded
    fun registerUser(
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("otherName") otherName:String,
        @Field("category") category:String,
        @Field("phoneNumber") phoneNumber:String,
        @Field("password") password:String
    ): Call<UserResponse<User>>

    @POST("auth/signup")
    @FormUrlEncoded
    fun registerUserWithEmail(
        @Field("category") category:String,
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<UserResponse<User>>

    @POST("auth/activate")
    @FormUrlEncoded
    fun activateUser(@Field("phoneNumber") phoneNumber:String, @Field("code") code:String  ):Call<UserResponse<User>>

    @POST("auth/signin")
    @FormUrlEncoded
    fun loginRequest(
        @Field("phoneNumber") phone:String,
        @Field("password") password:String
    ):Call<UserResponse<User>>

    @POST("auth/signin")
    @FormUrlEncoded
    fun login(
        @Field("email") field:String?,
        @Field("password") password:String?
    ):Call<UserResponse<User>>

    @POST("auth/signup")
    fun registerUser(
        @Body user: User?
    ): Call<UserResponse<User>>

    @POST("auth/signup/google")
    fun registerUser(
        @Header("Authorization") header: String,
        @Body user: User?
    ): Call<UserResponse<User>>

    @POST("auth/signin/google")
    fun loginWithGoogle(
        @Header("Authorization") header: String?
    ): Call<UserResponse<User>>

    @POST("auth/resendcode")
    @FormUrlEncoded
    fun resendCode(
        @Field("phoneNumber") phoneNumber: String?
    ): Call<UserResponse<User>>

}