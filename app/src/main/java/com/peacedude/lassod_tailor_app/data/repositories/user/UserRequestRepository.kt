package com.peacedude.lassod_tailor_app.data.repositories.user

import android.util.Log
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.services.user.UserServices
import retrofit2.Call
import javax.inject.Inject

class UserRequestRepository @Inject constructor(private val userServices: UserServices):UserRequestInterface {
    override fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String
    ): Call<UserResponse<User>> {
        return userServices.registerUser(firstName, lastName, otherName, category, phoneNumber, password)
    }

    override fun registerUser(user: User?): Call<UserResponse<User>> {
        return userServices.registerUser(user)

    }

    override fun registerUser(header: String, user: User?): Call<UserResponse<User>> {
        return userServices.registerUser(header, user)
    }

    override fun registerUserWithEmail(
        category: String,
        email: String,
        password: String
    ): Call<UserResponse<User>> {
        return userServices.registerUserWithEmail(category, email, password)
    }

    override fun activateUser(phoneNumber: String, code: String): Call<UserResponse<User>> {
        Log.i("Repository", "$phoneNumber $code")
        return userServices.activateUser(phoneNumber, code)
    }

    override fun loginRequest(phoneNumber: String, password: String): Call<UserResponse<User>> {
        return userServices.loginRequest(phoneNumber, password)
    }

    override fun loginWithEmailOrPhoneNumber(
        field: String?,
        password: String?
    ): Call<UserResponse<User>> {
        return userServices.login(field, password)
    }

    override fun loginWithGoogle(header: String?): Call<UserResponse<User>> {
        return userServices.loginWithGoogle(header)
    }

    override fun resendCode(phoneNumber: String): Call<UserResponse<User>> {
        return userServices.resendCode(phoneNumber)
    }

    override suspend fun addReviewAndRating(
        rate: Float?,
        artisanId: String?,
        comment: String?
    ): UserResponse<ReviewResponse> {
        return userServices.addReviewAndRating(rate, artisanId, comment)
    }

    override suspend fun getReviews(artisanId: String?): UserResponse<List<ReviewResponse>> {
        return userServices.getReviews(artisanId)
    }

    override suspend fun removeReview(id: String?): UserResponse<NothingExpected> {
        return userServices.removeReview(id)
    }

    override suspend fun addFavourite(artisanId: String?): UserResponse<Favourite> {
        return userServices.addFavourite(artisanId)
    }

    override suspend fun searchArtisan(
        keyword: String?,
        location: String?,
        specialty: String?,
        category: String?,
        page: Long?,
        size: Long?
    ): UserResponse<ArtisanSearchResponse> {
        return userServices.searchArtisan(keyword, location, specialty, category, page, size)
    }


}