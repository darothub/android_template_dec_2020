package com.peacedude.lassod_tailor_app.services.auth

import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.model.typealiases.SubscriptionList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface AuthServices {
    @GET("auth/profile/me")
    fun getUserData(@Header("Authorization") header: String): Call<UserResponse<User>>

    @GET("auth/profile/me")
    fun getUserData(): Call<UserResponse<User>>
    @GET("auth/profile/me")
    suspend fun getUserDetails(@Header("Authorization") header: String): UserResponse<User>

    @PATCH("auth/profile/me")
    fun updateUserData(
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
    fun getAllClient(): Call<UserResponse<ClientsList>>

    @GET("client")
    suspend fun getAllClients(): UserResponse<ClientsList>

    @HTTP(method = "DELETE", path = "client", hasBody = true)
    @FormUrlEncoded
    fun deleteClient(
        @Header("Authorization") header: String?,
        @Field("id") id: String?
    ): Call<UserResponse<NothingExpected>>

    @POST("media")
    @Headers("enctype:multipart/form-data")
    fun addPhoto(
        @Body file: RequestBody
    ): Call<UserResponse<NothingExpected>>

    @Multipart
    @POST("media")
    fun addPhoto(
        @PartMap map: HashMap<String, RequestBody>
    ): Call<UserResponse<NothingExpected>>

    @Multipart
    @POST("media")
    fun addPhoto(
        @Part photo: List<MultipartBody.Part>?
    ): Call<UserResponse<NothingExpected>>

    @PATCH("media")
    @FormUrlEncoded
    fun editPhotoInfo(
        @Field("id") id:String,
        @Field("info") info:String
    ): Call<UserResponse<UpdatedPhoto>>

    @POST("avatar")
    fun uploadProfilePicture(
        @Body avatar: RequestBody
    ): Call<UserResponse<User>>

    @POST("avatar")
    @Multipart
    fun uploadProfilePicture(
        @Header("Authorization") header: String?,
        @Part avatar: MultipartBody.Part
    ): Call<UserResponse<UploadImageClass>>

    @POST("measurement")
    fun addMeasurement(
        @Header("Authorization") header: String?,
        @Body measurementValues: MeasurementValues
    ): Call<UserResponse<ClientMeasurement>>

    @GET("measurements")
    suspend fun getAllMeasurements(
        @Query("clientId") clientId: String
    ): UserResponse<ListOfMeasurement>

    @GET("media")
    fun getAllPhoto(
        @Header("Authorization") header: String?
    ): Call<UserResponse<PhotoList>>

    @HTTP(method = "DELETE", path = "media", hasBody = true)
    @FormUrlEncoded
    fun deleteMedia(
        @Header("Authorization") header: String?,
        @Field("id") id: String?
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
    ):UserResponse<VideoList>

    @GET("article")
    suspend fun getArticles(
        @Header("Authorization") header: String?
    ):UserResponse<ArticleList>

    @GET("measurementypes")
    suspend fun getMeasurementTypes(
        @Header("Authorization") header: String?
    ): UserResponse<MeasurementTypeList>

    @POST("address")
    @FormUrlEncoded
    suspend fun addDeliveryAddress(
        @Header("Authorization") header: String?,
        @Field("clientId") clientId: String?,
        @Field("deliveryAddress") deliveryAddress: String?
    ): UserResponse<AddressData>

    @POST("addcard")
    @FormUrlEncoded
    suspend fun addCard(
        @Header("Authorization") header: String?,
        @Field("email") email: String?,
        @Field("amount") amount: String?
    ): UserResponse<AddCardWrapper<AddCardRes>>


    @GET("verifypayment")
    suspend fun verifyPayment(
        @Header("Authorization") header: String?,
        @Query("reference") reference: String
    ): UserResponse<UserResponse<AddCardResponse>>

    @POST("chargecard")
    suspend fun chargeCard(
        @Field("email") email: String?,
        @Field("amount") amount: String?,
        @Field("authorization_code") authorizationCode: String?
    ): UserResponse<ChargeCardResponse>

    @GET("addresses")
    suspend fun getAllAddress(
        @Header("Authorization") header: String?,
        @Query("clientId") clientId: String
    ): UserResponse<DeliveryAddress>


    @PATCH("  auth/chpassword")
    @FormUrlEncoded
    suspend fun changePassword(
        @Header("Authorization") header: String?,
        @Field("oldPassword") oldPassword: String?,
        @Field("newPassword") newPassword: String?
    ): UserResponse<NothingExpected>

    @HTTP(method = "DELETE", path = "measurement", hasBody = true)
    @FormUrlEncoded
    suspend fun deleteMeasurement(
        @Header("Authorization") header: String?,
        @Field("id") id: String?
    ): UserResponse<NothingExpected>

    @PATCH("measurement")
    suspend fun editMeasurement(
        @Header("Authorization") header: String?,
        @Body measurementValues: MeasurementValues
    ): UserResponse<EditMeasurement>

    @GET("media")
    suspend fun getAllPhoto(): UserResponse<PhotoList>

    @POST("review")
    @FormUrlEncoded
    suspend fun addReviewAndRating(
        @Field("rate")rate:Float,
        @Field("artisanId")artisanId:String,
        @Field("comment")comment: String
    ): UserResponse<ReviewData>

    @GET("plans")
    suspend fun getAllPlans(): UserResponse<SubscriptionResponse<List<SubscriptionData>>>

    @POST("subscribe")
    @FormUrlEncoded
    suspend fun subscribe(
        @Field("plan")plan:String,
        @Field("customer")customer:String
    ): UserResponse<SubscriptionResponse<List<SubscriptionData>>>

    @GET("subscribe")
    suspend fun getSubscriptions(
        @Query("code")code:String
    ): UserResponse<List<SubscribedData>>

    @GET("subscribers")
    suspend fun getUserAllSubscriptions(): UserResponse<SubscriptionList>

}


