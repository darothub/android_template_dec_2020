package com.peacedude.lassod_tailor_app.services.user

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.*
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

    @GET("artisans")
    suspend fun searchArtisan(
        @Query("keyword") keyword:String?,
        @Query("location") location:String?,
        @Query("specialty") specialty:String?,
        @Query("category") category:String?,
        @Query("page") page:Long?,
        @Query("size") size:Long?
    ): UserResponse<ArtisanSearchResponse>

    @POST("addfavourite")
    suspend fun addFavourite(
        @Query("artisanId") artisanId:String?
    ): UserResponse<Favourite>

    @GET("favourites")
    suspend fun getFavourites(): UserResponse<List<Favourite>>

    @HTTP(method = "DELETE", path = "removefavourite", hasBody = true)
    suspend fun removeFavourites(
        @Query("artisanId") artisanId:String?
    ): UserResponse<NothingExpected>

    @POST("addfavourite")
    @FormUrlEncoded
    suspend fun addReviewAndRating(
        @Field("rate") rate: Float?,
        @Field("artisanId") artisanId:String?,
        @Field("comment") comment:String?
    ): UserResponse<ReviewResponse>

    @POST("reviews")
    suspend fun getReviews(
        @Query("artisanId") artisanId:String?
    ): UserResponse<List<ReviewResponse>>

    @HTTP(method = "DELETE", path = "removereview", hasBody = true)
    suspend fun removeReview(
        @Query("id") id:String?
    ): UserResponse<NothingExpected>
}