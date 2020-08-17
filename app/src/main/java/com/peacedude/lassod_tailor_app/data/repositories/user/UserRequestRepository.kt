package com.peacedude.lassod_tailor_app.data.repositories.user

import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.services.user.UserServices
import retrofit2.Call
import javax.inject.Inject

class UserRequestRepository @Inject constructor( val userServices: UserServices):UserRequestInterface {
    override fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phone: String,
        password: String
    ): Call<UserResponse> {
        return userServices.registerUser(firstName, lastName, otherName, category, phone, password)
    }
}