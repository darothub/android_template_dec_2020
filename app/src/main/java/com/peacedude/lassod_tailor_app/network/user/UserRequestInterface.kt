package com.peacedude.lassod_tailor_app.network.user

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.*
import retrofit2.Call
import retrofit2.http.Query

interface UserRequestInterface {

    fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String
    ): Call<UserResponse<User>> = TODO()

    fun registerUserWithEmail(
        category: String,
        email: String,
        password: String
    ): Call<UserResponse<User>> = TODO()
    fun activateUser(phoneNumber: String, code: String): Call<UserResponse<User>> = TODO()

    fun loginRequest(phoneNumber: String, password: String): Call<UserResponse<User>> = TODO()

    fun loginWithEmailOrPhoneNumber(field: String?, password: String?): Call<UserResponse<User>> =
        TODO()
    fun registerUser(
        user: User?
    ): Call<UserResponse<User>> = TODO()

    fun registerUser(
        header: String,
        user: User?
    ): Call<UserResponse<User>> = TODO()
    fun loginWithGoogle(
        header: String?
    ): Call<UserResponse<User>> = TODO()

    fun resendCode(
        phoneNumber: String
    ): Call<UserResponse<User>> = TODO()

    suspend fun searchArtisan(
        keyword: String?,
        location: String?,
        specialty: String?,
        category: String?,
        page: Long?,
        size: Long?
    ): UserResponse<ArtisanSearchResponse> = TODO()

    suspend fun addFavourite(
        @Query("artisanId") artisanId: String?
    ): UserResponse<Favourite> = TODO()
    suspend fun addReviewAndRating(
        rate: Float?,
        artisanId: String?,
        comment: String?
    ): UserResponse<ReviewResponse> = TODO()

    suspend fun getReviews(
        artisanId: String?
    ): UserResponse<List<ReviewResponse>> = TODO()

    suspend fun removeReview(
        id: String?
    ): UserResponse<NothingExpected> = TODO()

    suspend fun getFavourites(): UserResponse<ArrayList<Favourite>> = TODO()

    suspend fun removeFavourites(artisanId: String?): UserResponse<NothingExpected> = TODO()
}
