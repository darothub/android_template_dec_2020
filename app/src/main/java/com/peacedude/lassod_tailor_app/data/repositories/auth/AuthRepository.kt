package com.peacedude.lassod_tailor_app.data.repositories.auth

import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.AuthResponse
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import retrofit2.Call
import javax.inject.Inject

class AuthRepository@Inject constructor(private val authServices: AuthServices):AuthRequestInterface {
    override fun getUserData(header: String): Call<UserResponse<User>> {
        return authServices.getUserData(header)
    }

    override fun updateUserData(header: String, user: User): Call<UserResponse<User>> {
        return authServices.updateUserData(header, user)
    }

    override fun forgetPassword(field: String): Call<UserResponse<String>> {
        return authServices.forgetPassword(field)
    }

    override fun resetPassword(token: String, password:String, cPassword: String): Call<UserResponse<User>> {
        return authServices.resetPassword(token, password, cPassword)
    }
}

