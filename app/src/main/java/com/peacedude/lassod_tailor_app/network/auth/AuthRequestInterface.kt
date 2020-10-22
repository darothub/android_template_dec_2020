package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import retrofit2.Call

interface AuthRequestInterface {
    fun getUserData(header:String): Call<UserResponse<User>> {
        return TODO()
    }

    fun updateUserData(header:String, user: User): Call<UserResponse<User>> {
        return TODO()
    }
    fun forgetPassword(field:String): Call<UserResponse<String>> {
        return TODO()
    }
    fun resetPassword(token: String, password:String, cPassword:String): Call<UserResponse<String>> {
        return TODO()
    }
    fun addClient(header: String, client: Client): Call<UserResponse<Client>>{
        return TODO()
    }
    fun getAllClient(header: String?, tailorId:String?): Call<UserResponse<List<Client>>>{
        return TODO()
    }
}