package com.peacedude.lassod_tailor_app.data.repositories.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authServices: AuthServices):AuthRequestInterface {
    val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()

    override fun getUserData(header: String): Call<UserResponse<User>> {
        return authServices.getUserData(header)
    }

    override suspend fun getUserDetails(header: String): UserResponse<User> {
        return authServices.getUserDetails(header)
    }

    override fun getAllVideos(header: String?): Call<UserResponse<VideoList>> {
        return authServices.getAllVideos(header)
    }

    override fun getAllArticles(header: String?): Call<UserResponse<ArticleList>> {
        return authServices.getAllArticles(header)
    }

    override suspend fun getVideos(header: String?): UserResponse<VideoList> {
        return authServices.getVideos(header)
    }

    override suspend fun getArticles(header: String?): UserResponse<ArticleList> {
        return authServices.getArticles(header)
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

    override suspend fun getMeasurementTypes(header: String?): UserResponse<MeasurementTypeList> {
        val h = authServices.getMeasurementTypes(header)
        Log.i("Repository", "Data ${h.data}")
        return h
    }

    override suspend fun addDeliveryAddress(
        header: String?,
        clientId: String?,
        deliveryAddress: String?
    ): UserResponse<AddressData> {
        return authServices.addDeliveryAddress(header, clientId, deliveryAddress)
    }

    override suspend fun addCard(
        header: String?,
        email: String?,
        amount: String?
    ): UserResponse<AddCardWrapper<AddCardRes>>{
        return authServices.addCard(header, email, amount)
    }

    override suspend fun verifyPayment(
        header: String?,
        reference: String
    ): UserResponse<UserResponse<AddCardResponse>> {
        return authServices.verifyPayment(header, reference)
    }

    override suspend fun chargeCard(
        email: String?,
        amount: String?,
        authorizationCode: String?
    ): UserResponse<ChargeCardResponse> {
        return authServices.chargeCard(email, amount, authorizationCode)
    }

    override suspend fun getAllAddress(
        header: String?,
        clientId: String
    ): UserResponse<DeliveryAddress> {
        return authServices.getAllAddress(header, clientId)
    }

    override suspend fun changePassword(
        header: String?,
        oldPassword: String?,
        newPassword: String?
    ): UserResponse<NothingExpected> {
        return authServices.changePassword(header, oldPassword, newPassword)
    }

//    override suspend fun getM(header:String): Flow<MeasurementTypeList> = flow{
//        val h = authServices.getMeasurementTypes(header)
//        emit(h)
//    }.flowOn(IO)

}

