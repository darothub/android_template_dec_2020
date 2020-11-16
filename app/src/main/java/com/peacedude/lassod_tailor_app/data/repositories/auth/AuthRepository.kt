package com.peacedude.lassod_tailor_app.data.repositories.auth

import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class AuthRepository@Inject constructor(private val authServices: AuthServices):AuthRequestInterface {
    val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()

    override fun getUserData(header: String): Call<UserResponse<User>> {
        return authServices.getUserData(header)
    }

    override fun getAllVideos(header: String?): Call<UserResponse<VideoList>> {
        return authServices.getAllVideos(header)
    }

    override fun getAllArticles(header: String?): Call<UserResponse<ArticleList>> {
        return authServices.getAllArticles(header)
    }

    override fun updateUserData(header: String, user: User): Call<UserResponse<User>> {
        return authServices.updateUserData(header, user)
    }

    override fun forgetPassword(field: String): Call<UserResponse<String>> {
        return authServices.forgetPassword(field)
    }

    override fun resetPassword(token: String, password:String, cPassword: String): Call<UserResponse<String>> {
        return authServices.resetPassword(token, password, cPassword)
    }

    override fun addClient(header: String, client: Client): Call<UserResponse<Client>> {
        return authServices.addClient(header, client)
    }

    override fun editClient(header: String, client: Client): Call<UserResponse<SingleClient>> {
        return authServices.editClient(header, client)
    }

    override fun getAllClient(header:String?): Call<UserResponse<ClientsList>> {
        return authServices.getAllClient(header)
    }

    override fun deleteClient(header: String?, id: String?): Call<UserResponse<NothingExpected>> {
        return authServices.deleteClient(header, id)
    }

    override fun addPhoto(
        header: String?,
        body: RequestBody
    ): Call<UserResponse<NothingExpected>> {
        return authServices.addPhoto(header, body)
    }

    override fun uploadProfilePicture(
        header: String?,
        body: RequestBody
    ): Call<UserResponse<User>> {
        return authServices.uploadProfilePicture(header, body)
    }

    override fun uploadProfilePicture(
        header: String?,
        body: MultipartBody.Part
    ): Call<UserResponse<UploadImageClass>> {
        return authServices.uploadProfilePicture(header, body)
    }

    override fun deleteMedia(header: String?, id: String?): Call<UserResponse<NothingExpected>> {
        return authServices.deleteMedia(header, id)
    }

    override fun addMeasurement(
        header: String?,
        body: MeasurementValues
    ): Call<UserResponse<ClientMeasurement>> {
        return authServices.addMeasurement(header, body)
    }

    override fun getAllPhoto(header: String?): Call<UserResponse<PhotoList>> {
        return authServices.getAllPhoto(header)
    }


}

