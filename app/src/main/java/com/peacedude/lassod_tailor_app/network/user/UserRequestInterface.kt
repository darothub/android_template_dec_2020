package com.peacedude.lassod_tailor_app.network.user

import androidx.lifecycle.LiveData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ArtisanSearchResponse
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserRequestInterface {

    fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String
    ): Call<UserResponse<User>> {
        return TODO()
    }

    fun registerUserWithEmail(
        category: String,
        email: String,
        password: String
    ): Call<UserResponse<User>> {
        return TODO()
    }
    fun activateUser(phoneNumber: String, code: String): Call<UserResponse<User>> {
        return TODO()
    }

    fun loginRequest(phoneNumber: String, password: String): Call<UserResponse<User>> {
        return TODO()
    }

    fun loginWithEmailOrPhoneNumber(field: String?, password: String?): Call<UserResponse<User>> {
        return TODO()
    }
   fun registerUser(
        user: User?
    ):Call<UserResponse<User>> {
        return TODO()
    }

    fun registerUser(
        header:String,
        user: User?
    ):Call<UserResponse<User>> {
        return TODO()
    }
    fun loginWithGoogle(
        header:String?
    ):Call<UserResponse<User>> {
        return TODO()
    }

    fun resendCode(
        phoneNumber: String
    ):Call<UserResponse<User>> {
        return TODO()
    }


    suspend fun searchArtisan(
        keyword:String?,
        location:String?,
        specialty:String?,
        category:String?,
        page:Long?,
        size:Long?
    ): UserResponse<ArtisanSearchResponse>{
        return TODO()
    }
}