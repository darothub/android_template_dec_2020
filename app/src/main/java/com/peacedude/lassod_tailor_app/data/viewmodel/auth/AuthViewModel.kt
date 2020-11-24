package com.peacedude.lassod_tailor_app.data.viewmodel.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*
import java.io.IOException
import javax.inject.Inject

open class AuthViewModel @Inject constructor(
    private val authRequestInterface: AuthRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest,
    internal val context: Context,
    var mGoogleSignInClient: GoogleSignInClient
) : GeneralViewModel(retrofit, storage, context, mGoogleSignInClient), ViewModelInterface {

    private val responseLiveData by lazy{
        MutableLiveData<ServicesResponseWrapper<ParentData>>()
    }
    val responseLiveDatas by lazy{
        MutableLiveData<MeasurementTypeList>()
    }
    override fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>> {
        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()

        val request = authRequestInterface.getUserData(header)
       return enqueueRequest<User>(request, responseLiveData)
    }

   override fun updateUserData(header: String, user: User): LiveData<ServicesResponseWrapper<ParentData>> {


        val request = authRequestInterface.updateUserData(header, user)
       return enqueueRequest<User>(request, responseLiveData)
    }

    override fun forgetPassword(field: String): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.forgetPassword(field)
        return enqueueRequest<String>(request, responseLiveData)
    }
    override fun resetPassword(token:String?, password:String?, cPassword:String?): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.resetPassword(token.toString(), password.toString(), cPassword.toString())
        return enqueueRequest<String>(request, responseLiveData)
    }

    override fun addClient(
        header: String?,
        client: Client
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.addClient(header.toString(), client)
        return enqueueRequest<Client>(request, responseLiveData)
    }

    override fun editClient(
        header: String?,
        client: Client
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.editClient(header.toString(), client)
        return enqueueRequest<SingleClient>(request, responseLiveData)
    }

    override fun getAllClient(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.getAllClient(header)
        return enqueueRequest<ClientsList>(request, responseLiveData)
    }

    override fun deleteClient(
        header: String?,
        id: String?
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.deleteClient(header, id)
        return enqueueRequest<NothingExpected>(request, responseLiveData)
    }

    override fun addPhoto(
        header: String?,
        body: RequestBody
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.addPhoto(header, body)
        return enqueueRequest<NothingExpected>(request, responseLiveData)
    }

    override fun uploadProfilePicture(
        header: String?,
        body: RequestBody
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.uploadProfilePicture(header, body)
        return enqueueRequest<User>(request, responseLiveData)
    }

    override fun addMeasurement(
        header: String?,
        body: MeasurementValues
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.addMeasurement(header, body)
        return enqueueRequest<ClientMeasurement>(request, responseLiveData)
    }

    override fun getAllPhoto(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.getAllPhoto(header)
        return enqueueRequest<PhotoList>(request, responseLiveData)
    }

    override fun deleteMedia(
        header: String?,
        id: String?
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.deleteMedia(header, id)
        return enqueueRequest<NothingExpected>(request, responseLiveData)
    }

    override fun getAllArticles(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.getAllArticles(header)
        return enqueueRequest<ArticleList>(request, responseLiveData)
    }

    override suspend fun getVideos(header: String?): Flow<ServicesResponseWrapper<ParentData>> = flow {

        try {
            val request = authRequestInterface.getVideos(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    override suspend fun getArticles(header: String?): Flow<ServicesResponseWrapper<ParentData>> = flow {

        try {
            val request = authRequestInterface.getArticles(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }

    }

    override suspend fun getMeasurementTypes(header: String?): Flow<ServicesResponseWrapper<ParentData>> = flow {

        try {
            val request = authRequestInterface.getMeasurementTypes(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    override suspend fun addDeliveryAddress(
        header: String?,
        clientId: String?,
        deliveryAddress: String?
    ): Flow<ServicesResponseWrapper<ParentData>> =flow {
        try {
            val request = authRequestInterface.addDeliveryAddress(header, clientId, deliveryAddress)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    override suspend fun addCard(
        header: String?,
        email: String?,
        amount: String?
    ): Flow<ServicesResponseWrapper<ParentData>> =flow {
        try {
            val request = authRequestInterface.addCard(header, email, amount)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }


    suspend fun getMea(header: String){
        viewModelScope.launch {
            authRequestInterface.getM(header)
                .catch {e ->
                    Log.d(title, "Error ${e.message}")
                }
                .collect {
                    responseLiveDatas.value = it
                }
        }
    }

    override suspend fun getAllAddress(
        header: String?,
        clientId: String
    ): Flow<ServicesResponseWrapper<ParentData>> = flow{
        try {
            val request = authRequestInterface.getAllAddress(header, clientId)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    override suspend fun getUserDetails(header: String): Flow<ServicesResponseWrapper<ParentData>> =flow{
        try {
            val request = authRequestInterface.getUserDetails(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }


    override fun getAllVideos(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.getAllVideos(header)
        return enqueueRequest<VideoList>(request, responseLiveData)
    }

    override fun uploadProfilePicture(
        header: String?,
        body: MultipartBody.Part
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.uploadProfilePicture(header, body)
        return enqueueRequest<UploadImageClass>(request, responseLiveData)
    }


}