package com.peacedude.lassod_tailor_app.data.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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

    override fun editClient(
        header: String?,
        client: Client
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.editClient(header.toString(), client)
        return enqueueRequest<SingleClient>(request, responseLiveData)
    }

    override fun getAllClient(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.getAllClient(header)
        return enqueueRequest<ClientsList>(request, responseLiveData)
    }

    override fun deleteClient(
        header: String?,
        id: String?
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.deleteClient(header, id)
        return enqueueRequest<NothingExpected>(request, responseLiveData)
    }

    override fun addPhoto(
        header: String?,
        body: RequestBody
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.addPhoto(header, body)
        return enqueueRequest<NothingExpected>(request, responseLiveData)
    }

    override fun uploadProfilePicture(
        header: String?,
        body: RequestBody
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.uploadProfilePicture(header, body)
        return enqueueRequest<User>(request, responseLiveData)
    }

    override fun addMeasurement(
        header: String?,
        body: MeasurementValues
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.addMeasurement(header, body)
        return enqueueRequest<ClientMeasurement>(request, responseLiveData)
    }

    override fun getAllPhoto(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.getAllPhoto(header)
        return enqueueRequest<PhotoList>(request, responseLiveData)
    }

    override fun uploadProfilePicture(
        header: String?,
        body: MultipartBody.Part
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.uploadProfilePicture(header, body)
        return enqueueRequest<UploadImageClass>(request, responseLiveData)
    }

}