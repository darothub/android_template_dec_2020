package com.peacedude.lassod_tailor_app.data.repositories.auth

import android.util.Log
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import com.peacedude.lassod_tailor_app.services.user.UserServices
import retrofit2.Call
import javax.inject.Inject

class AuthRepository@Inject constructor(private val authServices: AuthServices):AuthRequestInterface {
    override fun getUserData(header: String): Call<UserResponse> {
        return authServices.getUUserData(header)
    }
}

