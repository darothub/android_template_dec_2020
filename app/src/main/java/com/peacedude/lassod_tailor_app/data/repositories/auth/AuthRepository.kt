package com.peacedude.lassod_tailor_app.data.repositories.auth

import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UploadFileResponse
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import okhttp3.MultipartBody
import retrofit2.Call
import javax.inject.Inject

class AuthRepository@Inject constructor(private val authServices: AuthServices):AuthRequestInterface {
    val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()

    override fun getUserData(header: String): Call<UserResponse<User>> {
        return authServices.getUserData(header)
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

    override fun getAllClient(header:String?): Call<UserResponse<ClientsList>> {
        return authServices.getAllClient(header)
    }

    override fun deleteClient(header: String?, id: String?): Call<UserResponse<NothingExpected>> {
        return authServices.deleteClient(header, id)
    }

    override fun addPhoto(
        header: String?,
        photo: MultipartBody.Part
    ): Call<UserResponse<List<UploadFileResponse>>> {
        return authServices.addPhoto(header, photo)
    }


}

