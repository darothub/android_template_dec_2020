package com.peacedude.lassod_tailor_app.data.viewmodel.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.helpers.SingleLiveEvent
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*
import javax.inject.Inject

open class AuthViewModel @Inject constructor(
    private val authRequestInterface: AuthRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest,
    internal val context: Context,
    var mGoogleSignInClient: GoogleSignInClient
) : GeneralViewModel(retrofit, storage, context, mGoogleSignInClient), ViewModelInterface {



    val responseLiveData by lazy{
        SingleLiveEvent<ServicesResponseWrapper<ParentData>>()
    }



    val responseLiveDatas by lazy{
        MutableLiveData<MeasurementTypeList>()
    }
    override fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.getUserData(header)
       return enqueueRequest<User>(request, responseLiveData)
    }

    override fun getUserData(): LiveData<ServicesResponseWrapper<ParentData>> {


        val request = authRequestInterface.getUserData()
        return enqueueRequest<User>(request, responseLiveData)
    }

   override fun updateUserData(user: User): LiveData<ServicesResponseWrapper<ParentData>> {


        val request = authRequestInterface.updateUserData(user)
       return enqueueRequest<User>(request, responseLiveData)
    }

    override fun forgetPassword(field: String): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.forgetPassword(field)
        return enqueueRequest<String>(request, responseLiveData)
    }
    override fun resetPassword(header:String?, password:String?, cPassword:String?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.resetPassword(header.toString(), password.toString(), cPassword.toString())
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

    override fun getAllClient(): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.getAllClient()
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
        body: RequestBody
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.addPhoto(body)
        return enqueueRequest(request, responseLiveData)
    }

    override fun addPhoto(
        photo: List<MultipartBody.Part>?
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.addPhoto(photo)
        return enqueueRequest(request, responseLiveData)
    }

    override fun editPhotoInfo(
        id: String,
        info: String
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.editPhotoInfo(id, info)
        return enqueueRequest(request, responseLiveData)
    }

    override fun uploadProfilePicture(
        body: RequestBody
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.uploadProfilePicture(body)
        return enqueueRequest<User>(request, responseLiveData)
    }

    override fun addMeasurement(
        header: String?,
        body: MeasurementValues
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.addMeasurement(header, body)
        return enqueueRequest<ClientMeasurement>(request, responseLiveData)
    }

    @ExperimentalCoroutinesApi
    override suspend fun getAllPhoto(): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.getAllPhoto()
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun deleteMeasurements(
        header: String?,
        id: String
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.deleteMeasurement(header, id)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun editMeasurement(
        header: String?,
        measurementValues: MeasurementValues
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.editMeasurement(header, measurementValues)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }


    @ExperimentalCoroutinesApi
    override suspend fun verifyPayment(
        header: String?,
        reference: String
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.verifyPayment(header, reference)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    override fun addPhoto(map: HashMap<String, RequestBody>): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.addPhoto(map)
        return enqueueRequest(request, responseLiveData)
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

    @ExperimentalCoroutinesApi
    override suspend fun getVideos(header: String?): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.getVideos(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getArticles(header: String?): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.getArticles(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }

    }

    @ExperimentalCoroutinesApi
    override suspend fun getMeasurementTypes(header: String?): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = authRequestInterface.getMeasurementTypes(header)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun addDeliveryAddress(
        header: String?,
        clientId: String?,
        deliveryAddress: String?
    ): Flow<ServicesResponseWrapper<ParentData>> =channelFlow {
        try {
            val request = authRequestInterface.addDeliveryAddress(header, clientId, deliveryAddress)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }



    @ExperimentalCoroutinesApi
    override suspend fun addCard(
        header: String?,
        email: String?,
        amount: String?
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {
        try {
            val request = authRequestInterface.addCard(header, email, amount)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getAllClients(): Flow<ServicesResponseWrapper<ParentData>> = channelFlow{
        try {
            val request = authRequestInterface.getAllClients()
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }


    @ExperimentalCoroutinesApi
    override fun getAllAddress(
        header: String?,
        clientId: String
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow{
        try {
            val request = authRequestInterface.getAllAddress(header, clientId)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getUserDetails(header: String): Flow<ServicesResponseWrapper<ParentData>> =channelFlow{
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

    @ExperimentalCoroutinesApi
    override suspend fun changePassword(
        header: String?,
        oldPassword: String?,
        newPassword: String?
    ): Flow<ServicesResponseWrapper<ParentData>> =channelFlow {
        try {
            val request = authRequestInterface.changePassword(header, oldPassword, newPassword)
            onSuccessFlowResponse(request)
        } catch (e: HttpException) {
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override  fun getAllMeasurements(
        clientId: String
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {
        try {
            val request = authRequestInterface.getAllMeasurements(clientId)
            onSuccessFlowResponse(request)
        } catch (e: HttpException) {
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun addReviewAndRating(
        rate: Float,
        artisanId: String,
        comment: String
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {
        try {
            val request = authRequestInterface.addReviewAndRating(rate, artisanId, comment)
            onSuccessFlowResponse(request)
        } catch (e: HttpException) {
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getAllPlans(): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {
        try {
            val request = authRequestInterface.getAllPlans()
            onSuccessFlowResponse(request)
        } catch (e: HttpException) {
            onErrorFlowResponse(e)
        }
    }
}