package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AuthRequestInterface {
    fun getUserData(header:String): Call<UserResponse<User>> {
        return TODO()
    }
    fun getUserData(): Call<UserResponse<User>> {
        return TODO()
    }

    suspend fun getUserDetails( header: String): UserResponse<User>{
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
    fun getAllClient(): Call<UserResponse<ClientsList>>{
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

    suspend fun getAllMeasurements(
        header: String?,
        clientId:String
    ): UserResponse<ListOfMeasurement>{
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
    fun getAllArticles(header: String?) : Call<UserResponse<ArticleList>>{
        return TODO()
    }

    suspend fun getVideos(header: String?): UserResponse<VideoList>{
        return TODO()
    }

    suspend fun getArticles(header: String?): UserResponse<ArticleList>{
        return TODO()
    }

    suspend fun getMeasurementTypes(header: String?): UserResponse<MeasurementTypeList>{
        return TODO()
    }
    suspend fun getM(header:String): Flow<MeasurementTypeList>{
        return TODO()
    }
    suspend fun addDeliveryAddress(
        header: String?,
        clientId:String?,
        deliveryAddress:String?
    ): UserResponse<AddressData>{
        return TODO()
    }
    suspend fun addCard(
        header: String?,
        email:String?,
        amount:String?
    ): UserResponse<AddCardWrapper<AddCardRes>>{
        return TODO()
    }

    suspend fun verifyPayment(
        header: String?,
        reference:String
    ): UserResponse<UserResponse<AddCardResponse>>{
        return TODO()
    }

    suspend fun chargeCard(
        email:String?,
        amount:String?,
        authorizationCode: String?
    ): UserResponse<ChargeCardResponse>{
        return TODO()
    }

    suspend fun getAllAddress(
        header:String?,
        clientId:String
    ): UserResponse<DeliveryAddress>{
        return TODO()
    }

    suspend fun changePassword(
        header:String?,
        oldPassword:String?,
        newPassword:String?
    ): UserResponse<NothingExpected>{
        return TODO()
    }

    suspend fun deleteMeasurement(
        header:String?,
        id:String?
    ): UserResponse<NothingExpected>{
        return TODO()
    }

    suspend fun editMeasurement(
        header:String?,
        measurementValues: MeasurementValues
    ): UserResponse<EditMeasurement>{
        return TODO()
    }
}