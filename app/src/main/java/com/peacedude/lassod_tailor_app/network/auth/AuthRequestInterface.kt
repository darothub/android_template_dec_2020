package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.SingleClient
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.UploadFileResponse
import com.peacedude.lassod_tailor_app.model.response.UploadedPhoto
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    fun editClient(header: String, client: Client): Call<UserResponse<SingleClient>>{
        return TODO()
    }
    fun getAllClient(header: String?): Call<UserResponse<ClientsList>>{
        return TODO()
    }
    fun deleteClient(header: String?, id: String?): Call<UserResponse<NothingExpected>>{
        return TODO()
    }
    fun addPhoto(header: String?, body: RequestBody): Call<UserResponse<NothingExpected>>{
        return TODO()
    }
    fun uploadProfilePicture(header: String?, body: RequestBody): Call<UserResponse<User>>{
        return TODO()
    }
    fun uploadProfilePicture(header: String?, body: MultipartBody.Part): Call<UserResponse<User>>{
        return TODO()
    }
}