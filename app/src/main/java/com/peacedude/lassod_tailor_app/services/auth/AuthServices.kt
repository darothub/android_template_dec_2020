package com.peacedude.lassod_tailor_app.services.auth

import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
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

    @PATCH("client")
    fun editClient(
        @Header("Authorization") header: String,
        @Body client: Client
    ): Call<UserResponse<SingleClient>>

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

    @POST("avatar")
    fun uploadProfilePicture(
        @Header("Authorization") header: String?,
        @Body avatar:RequestBody
    ): Call<UserResponse<User>>


    @POST("avatar")
    @Multipart
    fun uploadProfilePicture(
        @Header("Authorization") header: String?,
        @Part avatar:MultipartBody.Part
    ): Call<UserResponse<UploadImageClass>>

    @POST("measurement")
    fun addMeasurement(
        @Header("Authorization") header: String?,
        @Body measurementValues: MeasurementValues
    ): Call<UserResponse<ClientMeasurement>>

    @GET("media")
    fun getAllPhoto(
        @Header("Authorization") header: String?
    ): Call<UserResponse<PhotoList>>

    @HTTP(method = "DELETE", path = "media", hasBody = true)
    @FormUrlEncoded
    fun deleteMedia(
        @Header("Authorization") header: String?,
        @Field("id") id:String?
    ): Call<UserResponse<NothingExpected>>

    @GET("video")
    fun getAllVideos(
        @Header("Authorization") header: String?
    ): Call<UserResponse<VideoList>>

    @GET("article")
    fun getAllArticles(
        @Header("Authorization") header: String?
    ): Call<UserResponse<ArticleList>>

    @GET("video")
    suspend fun getVideos(
        @Header("Authorization") header: String?
    ): VideoList

    @GET("article")
    suspend fun getArticles(
        @Header("Authorization") header: String?
    ): ArticleList

    @GET("measurementypes")
    suspend fun getMeasurementTypes(
        @Header("Authorization") header: String?
    ): UserResponse<MeasurementTypeList>

    @POST("address")
    @FormUrlEncoded
    suspend fun addDeliveryAddress(
        @Header("Authorization") header: String?,
        @Field("clientId") clientId:String?,
        @Field("deliveryAddress") deliveryAddress:String?
    ): UserResponse<AddressData>
}


