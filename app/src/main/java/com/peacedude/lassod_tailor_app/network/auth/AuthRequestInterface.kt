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
    fun getUserData(header:String): Call<UserResponse<User>> = TODO()
    fun getUserData(): Call<UserResponse<User>> = TODO()

    suspend fun getUserDetails( header: String): UserResponse<User> = TODO()
    fun updateUserData(user: User): Call<UserResponse<User>> = TODO()
    fun forgetPassword(field:String): Call<UserResponse<String>> = TODO()
    fun resetPassword(token: String, password:String, cPassword:String): Call<UserResponse<String>> =
        TODO()
    fun addClient(header: String, client: Client): Call<UserResponse<Client>> = TODO()
    fun editClient(header: String, client: Client): Call<UserResponse<SingleClient>> = TODO()
    fun getAllClient(): Call<UserResponse<ClientsList>> = TODO()
    fun deleteClient(header: String?, id: String?): Call<UserResponse<NothingExpected>> = TODO()
    fun addPhoto(body: RequestBody): Call<UserResponse<NothingExpected>> = TODO()
    fun addPhoto(image:MultipartBody.Part, name:RequestBody): Call<UserResponse<NothingExpected>> = TODO()
    fun uploadProfilePicture(body: RequestBody): Call<UserResponse<User>> = TODO()
    fun uploadProfilePicture(header: String?, body: MultipartBody.Part): Call<UserResponse<UploadImageClass>> =
        TODO()
    fun addMeasurement(header: String?, body: MeasurementValues): Call<UserResponse<ClientMeasurement>> =
        TODO()

    suspend fun getAllMeasurements(
        clientId:String
    ): UserResponse<ListOfMeasurement> = TODO()

    fun getAllPhoto(header: String?) : Call<UserResponse<PhotoList>> = TODO()
    fun deleteMedia(header: String?, id: String?) : Call<UserResponse<NothingExpected>> = TODO()
    fun getAllVideos(header: String?) : Call<UserResponse<VideoList>> = TODO()
    fun getAllArticles(header: String?) : Call<UserResponse<ArticleList>> = TODO()

    suspend fun getVideos(header: String?): UserResponse<VideoList> = TODO()

    suspend fun getArticles(header: String?): UserResponse<ArticleList> = TODO()

    suspend fun getMeasurementTypes(header: String?): UserResponse<MeasurementTypeList> = TODO()
    suspend fun getM(header:String): Flow<MeasurementTypeList> = TODO()
    suspend fun addDeliveryAddress(
        header: String?,
        clientId:String?,
        deliveryAddress:String?
    ): UserResponse<AddressData> = TODO()
    suspend fun addCard(
        header: String?,
        email:String?,
        amount:String?
    ): UserResponse<AddCardWrapper<AddCardRes>> = TODO()

    suspend fun verifyPayment(
        header: String?,
        reference:String
    ): UserResponse<UserResponse<AddCardResponse>> = TODO()

    suspend fun chargeCard(
        email:String?,
        amount:String?,
        authorizationCode: String?
    ): UserResponse<ChargeCardResponse> = TODO()

    suspend fun getAllAddress(
        header:String?,
        clientId:String
    ): UserResponse<DeliveryAddress> = TODO()

    suspend fun changePassword(
        header:String?,
        oldPassword:String?,
        newPassword:String?
    ): UserResponse<NothingExpected> = TODO()

    suspend fun deleteMeasurement(
        header:String?,
        id:String?
    ): UserResponse<NothingExpected> = TODO()

    suspend fun editMeasurement(
        header:String?,
        measurementValues: MeasurementValues
    ): UserResponse<EditMeasurement> = TODO()

    suspend fun getAllPhoto(): UserResponse<PhotoList> = TODO()

    fun addPhoto(
        @PartMap map: HashMap<String, RequestBody>
    ): Call<UserResponse<NothingExpected>> = TODO()
}