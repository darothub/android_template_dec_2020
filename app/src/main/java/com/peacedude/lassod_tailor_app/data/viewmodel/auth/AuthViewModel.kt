package com.peacedude.lassod_tailor_app.data.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

open class AuthViewModel @Inject constructor(
    private val authRequestInterface: AuthRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest
) : GeneralViewModel(retrofit,storage), ViewModelInterface {
    val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
    override fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>> {
        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.getUserData(header)
       return enqueueRequest<User>(request, responseLiveData)
    }

   override fun updateUserData(header: String, user: User): LiveData<ServicesResponseWrapper<ParentData>> {

        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.updateUserData(header, user)
       return enqueueRequest<User>(request, responseLiveData)
    }

    override fun forgetPassword(field: String): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.forgetPassword(field)
        return enqueueRequest<String>(request, responseLiveData)
    }
    override fun resetPassword(token:String?, password:String?, cPassword:String?): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.resetPassword(token.toString(), password.toString(), cPassword.toString())
        return enqueueRequest<String>(request, responseLiveData)
    }

    override fun addClient(
        header: String?,
        client: Client
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.addClient(header.toString(), client)
        return enqueueRequest<Client>(request, responseLiveData)
    }

    override fun getAllClient(header: String?, tailorId: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.getAllClient(header, tailorId)
        return enqueueRequest<List<Client>>(request, responseLiveData)
    }

}