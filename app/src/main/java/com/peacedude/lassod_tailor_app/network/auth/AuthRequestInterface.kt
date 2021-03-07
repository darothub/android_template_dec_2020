package com.peacedude.lassod_tailor_app.network.auth

import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.model.typealiases.SubscriptionList
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AuthRequestInterface {
    fun getUserData(header:String): Call<UserResponse<User>> = TODO()
    fun getUserData(): Call<UserResponse<User>> = TODO()

    suspend fun getUserDetails(): UserResponse<User> = TODO()
    fun updateUserData(user: User): Call<UserResponse<User>> = TODO()
    fun forgetPassword(field:String): Call<UserResponse<String>> = TODO()
    fun resetPassword(token: String, password:String, cPassword:String): Call<UserResponse<String>> =
        TODO()
    fun addClient(client: Client): Call<UserResponse<Client>> = TODO()
    fun editClient(client: Client): Call<UserResponse<SingleClient>> = TODO()
    fun getAllClient(): Call<UserResponse<ClientsList>> = TODO()
    fun deleteClient(id: String?): Call<UserResponse<NothingExpected>> = TODO()
    fun addPhoto(body: RequestBody): Call<UserResponse<NothingExpected>> = TODO()
    fun addPhoto(photo:List<MultipartBody.Part>?): Call<UserResponse<NothingExpected>> = TODO()
    fun editPhotoInfo(id:String, info:String): Call<UserResponse<UpdatedPhoto>> = TODO()
    fun uploadProfilePicture(body: RequestBody): Call<UserResponse<User>> = TODO()
    fun uploadProfilePicture(body: MultipartBody.Part): Call<UserResponse<UploadImageClass>> =
        TODO()
    fun addMeasurement(body: MeasurementValues): Call<UserResponse<ClientMeasurement>> =
        TODO()

    suspend fun getAllMeasurements(
        clientId:String
    ): UserResponse<ListOfMeasurement> = TODO()

    fun getAllPhotos() : Call<UserResponse<PhotoList>> = TODO()
    fun deleteMedia( id: String?) : Call<UserResponse<NothingExpected>> = TODO()
    fun getAllVideos() : Call<UserResponse<VideoList>> = TODO()
    fun getAllArticles() : Call<UserResponse<ArticleList>> = TODO()

    suspend fun getVideos(): UserResponse<VideoList> = TODO()

    suspend fun getArticles(): UserResponse<ArticleList> = TODO()

    suspend fun getMeasurementTypes(): UserResponse<MeasurementTypeList> = TODO()
    suspend fun getM(header:String): Flow<MeasurementTypeList> = TODO()
    suspend fun addDeliveryAddress(
        clientId:String?,
        deliveryAddress:String?
    ): UserResponse<AddressData> = TODO()
    suspend fun addCard(
        email:String?,
        amount:String?
    ): UserResponse<AddCardWrapper<AddCardRes>> = TODO()

    suspend fun verifyPayment(
        reference:String
    ): UserResponse<UserResponse<AddCardResponse>> = TODO()

    suspend fun chargeCard(
        email:String?,
        amount:String?,
        authorizationCode: String?
    ): UserResponse<ChargeCardResponse> = TODO()

    suspend fun getAllAddress(
        clientId:String
    ): UserResponse<DeliveryAddress> = TODO()

    suspend fun changePassword(
        oldPassword:String?,
        newPassword:String?
    ): UserResponse<NothingExpected> = TODO()

    suspend fun deleteMeasurement(
        id:String?
    ): UserResponse<NothingExpected> = TODO()

    suspend fun editMeasurement(
        measurementValues: MeasurementValues
    ): UserResponse<EditMeasurement> = TODO()

    suspend fun getAllPhoto(): UserResponse<PhotoList> = TODO()

    fun addPhoto(
        @PartMap map: HashMap<String, RequestBody>
    ): Call<UserResponse<NothingExpected>> = TODO()

    suspend fun addReviewAndRating(
        rate:Float,
        artisanId:String,
        comment: String
    ): UserResponse<ReviewData> = TODO()

    suspend fun getAllClients(): UserResponse<ClientsList>  = TODO()
    suspend fun getAllClientsTwo(): Flow<UserResponse<ClientsList>> = TODO()

    suspend fun getAllPlans(): UserResponse<SubscriptionResponse<List<SubscriptionData>>>  = TODO()

    suspend fun subscribe(
        plan:String,
        customer:String
    ): UserResponse<SubscriptionResponse<List<SubscriptionData>>> = TODO()

    suspend fun getSubscriptions(code:String): UserResponse<List<SubscribedData>> = TODO()

    suspend fun getUserAllSubscriptions(): UserResponse<List<SubscribedDataDetails>> = TODO()
}