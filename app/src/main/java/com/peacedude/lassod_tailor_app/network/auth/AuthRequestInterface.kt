package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
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
    fun uploadProfilePicture(header: String?, body: MultipartBody.Part): Call<UserResponse<UploadImageClass>>{
        return TODO()
    }
    fun addMeasurement(header: String?, body: MeasurementValues): Call<UserResponse<ClientMeasurement>>{
        return TODO()
    }

    fun getAllPhoto(header: String?) : Call<UserResponse<PhotoList>>{
        return TODO()
    }
    fun deleteMedia(header: String?, id: String?) : Call<UserResponse<NothingExpected>>{
        return TODO()
    }
    fun getAllVideos(header: String?) : Call<UserResponse<VideoList>>{
        return TODO()
    }

}