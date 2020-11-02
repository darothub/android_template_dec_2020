package com.peacedude.lassod_tailor_app.services.auth

import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.UploadFileResponse
import com.peacedude.lassod_tailor_app.model.response.UploadedPhoto
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AuthServices {
    @GET("auth/profile/me")
    fun getUserData(@Header("Authorization") header: String): Call<UserResponse<User>>

    @PATCH("auth/profile/me")
    fun updateUserData(
        @Header("Authorization") header: String,
        @Body user: User
    ): Call<UserResponse<User>>

    @POST("auth/forgetpassword")
    @FormUrlEncoded
    fun forgetPassword(@Field("field") field: String): Call<UserResponse<String>>

    @PATCH("auth/reset-password")
    @FormUrlEncoded
    fun resetPassword(
        @Query("authorization") token: String,
        @Field("password") password: String,
        @Field("confirmPassword") cpassword: String
    ): Call<UserResponse<String>>

    @POST("client")
    fun addClient(
        @Header("Authorization") header: String,
        @Body client: Client
    ): Call<UserResponse<Client>>

    @GET("client")
    fun getAllClient(
        @Header("Authorization") header: String?
    ): Call<UserResponse<ClientsList>>

    @HTTP(method = "DELETE", path = "client", hasBody = true)
    @FormUrlEncoded
    fun deleteClient(
        @Header("Authorization") header: String?,
        @Field("id") id:String?
    ): Call<UserResponse<NothingExpected>>


    @POST("media")
    fun addPhoto(
        @Header("Authorization") header: String?,
        @Body photo:RequestBody
    ): Call<UserResponse<NothingExpected>>

}

