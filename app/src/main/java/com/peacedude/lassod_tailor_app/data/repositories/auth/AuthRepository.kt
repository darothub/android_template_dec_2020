package com.peacedude.lassod_tailor_app.data.repositories.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.model.typealiases.SubscriptionList
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.services.auth.AuthServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authServices: AuthServices):AuthRequestInterface {

    override fun getUserData(header: String): Call<UserResponse<User>> {
        return authServices.getUserData(header)
    }

    override fun getUserData(): Call<UserResponse<User>> {
        return authServices.getUserData()
    }

    override suspend fun getUserDetails(): UserResponse<User> {
        return authServices.getUserDetails()
    }

    override fun getAllVideos(): Call<UserResponse<VideoList>> {
        return authServices.getAllVideos()
    }

    override fun getAllArticles(): Call<UserResponse<ArticleList>> {
        return authServices.getAllArticles()
    }

    override suspend fun getVideos(): UserResponse<VideoList> {
        return authServices.getVideos()
    }

    override suspend fun getArticles(): UserResponse<ArticleList> {
        return authServices.getArticles()
    }

    override fun updateUserData(user: User): Call<UserResponse<User>> {
        return authServices.updateUserData(user)
    }

    override fun forgetPassword(field: String): Call<UserResponse<String>> {
        return authServices.forgetPassword(field)
    }

    override fun resetPassword(token: String, password:String, cPassword: String): Call<UserResponse<String>> {
        return authServices.resetPassword(token, password, cPassword)
    }

    override fun addClient(client: Client): Call<UserResponse<Client>> {
        return authServices.addClient(client)
    }

    override fun editClient(client: Client): Call<UserResponse<SingleClient>> {
        return authServices.editClient( client)
    }

    override fun getAllClient(): Call<UserResponse<ClientsList>> {
        return authServices.getAllClient()
    }

    override fun deleteClient(id: String?): Call<UserResponse<NothingExpected>> {
        return authServices.deleteClient( id)
    }

    override fun addPhoto(
        photo: List<MultipartBody.Part>?
    ): Call<UserResponse<NothingExpected>> {
        return authServices.addPhoto(photo)
    }

    override fun editPhotoInfo(id: String, info: String): Call<UserResponse<UpdatedPhoto>> {
        return authServices.editPhotoInfo(id, info)
    }

    override fun addPhoto(
        body: RequestBody
    ): Call<UserResponse<NothingExpected>> {
        return authServices.addPhoto(body)
    }

    override fun uploadProfilePicture(
        body: RequestBody
    ): Call<UserResponse<User>> {
        return authServices.uploadProfilePicture(body)
    }

    override fun uploadProfilePicture(
        body: MultipartBody.Part
    ): Call<UserResponse<UploadImageClass>> {
        return authServices.uploadProfilePicture(body)
    }

    override fun deleteMedia(id: String?): Call<UserResponse<NothingExpected>> {
        return authServices.deleteMedia(id)
    }

    override fun addMeasurement(
        body: MeasurementValues
    ): Call<UserResponse<ClientMeasurement>> {
        return authServices.addMeasurement(body)
    }

//    override fun getAllPhotos(): Call<UserResponse<PhotoList>> {
//        return authServices.getAllPhoto()
//    }

    override suspend fun getAllPhoto(): UserResponse<PhotoList> {
        return authServices.getAllPhoto()
    }

    override suspend fun getMeasurementTypes(): UserResponse<MeasurementTypeList> {
        val h = authServices.getMeasurementTypes()
        Log.i("Repository", "Data ${h.data}")
        return h
    }

    override suspend fun addDeliveryAddress(
        clientId: String?,
        deliveryAddress: String?
    ): UserResponse<AddressData> {
        return authServices.addDeliveryAddress( clientId, deliveryAddress)
    }

    override suspend fun addCard(
        email: String?,
        amount: String?
    ): UserResponse<AddCardWrapper<AddCardRes>>{
        return authServices.addCard(email, amount)
    }

    override suspend fun verifyPayment(
        reference: String
    ): UserResponse<UserResponse<AddCardResponse>> {
        return authServices.verifyPayment(reference)
    }

    override suspend fun chargeCard(
        email: String?,
        amount: String?,
        authorizationCode: String?
    ): UserResponse<ChargeCardResponse> {
        return authServices.chargeCard(email, amount, authorizationCode)
    }

    override suspend fun subscribe(
        plan: String,
        customer: String
    ): UserResponse<SubscriptionResponse<List<SubscriptionData>>> {
        return authServices.subscribe(plan, customer)
    }

    override suspend fun getAllPlans(): UserResponse<SubscriptionResponse<List<SubscriptionData>>> {
        return authServices.getAllPlans()
    }

    override suspend fun getSubscriptions(code: String): UserResponse<List<SubscribedData>> {
        return authServices.getSubscriptions(code)
    }

    override suspend fun getUserAllSubscriptions(): UserResponse<SubscriptionList> {
        return authServices.getUserAllSubscriptions()
    }

    override suspend fun getAllAddress(
        clientId: String
    ): UserResponse<DeliveryAddress> {
        return authServices.getAllAddress(clientId)
    }

    override suspend fun addReviewAndRating(
        rate: Float,
        artisanId: String,
        comment: String
    ): UserResponse<ReviewData> {
        return authServices.addReviewAndRating(rate, artisanId, comment)
    }

    override suspend fun getAllClients(): UserResponse<ClientsList> {
        return authServices.getAllClients()
    }

    override suspend fun getAllClientsTwo(): Flow<UserResponse<ClientsList>> {
        return flow {
            // exectute API call and map to UI object
            val fooList = authServices.getAllClientsTwo()
            fooList.data?.clients
                ?.map { fooFromApi ->

                }

            // Emit the list to the stream
            emit(fooList)
        }.flowOn(Dispatchers.IO)
    }

    override fun addPhoto(map: HashMap<String, RequestBody>): Call<UserResponse<NothingExpected>> {
        return authServices.addPhoto(map)
    }

    override suspend fun changePassword(
        oldPassword: String?,
        newPassword: String?
    ): UserResponse<NothingExpected> {
        return authServices.changePassword(oldPassword, newPassword)
    }

    override suspend fun deleteMeasurement(
        id: String?
    ): UserResponse<NothingExpected> {
        return authServices.deleteMeasurement(id)
    }

    override suspend fun editMeasurement(
        measurementValues: MeasurementValues
    ): UserResponse<EditMeasurement> {
        return authServices.editMeasurement(measurementValues)
    }

    override suspend fun getAllMeasurements(
        clientId: String
    ): UserResponse<ListOfMeasurement> {
        return authServices.getAllMeasurements(clientId)
    }



//    override suspend fun getM(header:String): Flow<MeasurementTypeList> = flow{
//        val h = authServices.getMeasurementTypes(header)
//        emit(h)
//    }.flowOn(IO)

}

