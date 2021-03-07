package com.peacedude.lassod_tailor_app.data.viewmodel.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.helpers.SingleLiveEvent
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*
import java.lang.Exception
import javax.inject.Inject

open class AuthViewModel @Inject constructor(
    private val authRequestInterface: AuthRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest,
    internal val context: Context,
    var mGoogleSignInClient: GoogleSignInClient
) : GeneralViewModel(retrofit, storage, context, mGoogleSignInClient), ViewModelInterface {


    override fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.getUserData(header)
       return enqueueRequest<User>(request)
    }

    var theUserData =netWorkLiveData.switchMap{
        getTheUserData(it)
    }

    private fun getTheUserData(it: Boolean) = if (it) {
        var request: Call<UserResponse<User>> = authRequestInterface.getUserData()
        enqueueRequest(request)
        responseObserver
    } else {
        i(title, "Bad Connection")
        badNetworkServiceLiveData
    }


    override fun  getUserData(): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = authRequestInterface.getUserData()
        return enqueueRequest<User>(request)
//        return netWorkLiveData.switchMap {
//            if (it){
//                responseObserver
//            }
//            else{
//                responseObserver.postValue(ServicesResponseWrapper.Network(502, "Bad connection"))
//                responseObserver
//            }
//        }

    }

   override fun updateUserData(user: User) = netWorkLiveData.switchMap {
       if (it) {
           var request: Call<UserResponse<User>> = authRequestInterface.updateUserData(user)
           enqueueRequest(request)
           responseObserver
       } else {
           i(title, "Bad Connection")
           badNetworkServiceLiveData
       }
   }


    override fun forgetPassword(field: String)=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.forgetPassword(field)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }



    override fun resetPassword(password:String?, cPassword:String?)=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.resetPassword(header.toString(), password.toString(), cPassword.toString())
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun addClient(
        client: Client
    )=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.addClient(client)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }

    }

    override fun editClient(
        client: Client
    )=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.editClient(client)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }

    }

    override fun getAllClient(): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = authRequestInterface.getAllClient()
        return enqueueRequest<ClientsList>(request)
    }



    override suspend fun getAllClientsTwo(): Flow<ServicesResponseWrapper<ParentData>> = netWorkStateFlow.flatMapLatest{
        if (it) {
            try {
                val request = authRequestInterface.getAllClients()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            uiState
        }
    }


    override fun deleteClient(
        id: String?
    )=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.deleteClient(id)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun addPhoto(
        body: RequestBody
    )=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.addPhoto(body)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun addPhoto(
        photo: List<MultipartBody.Part>?
    )=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.addPhoto(photo)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun editPhotoInfo(
        id: String,
        info: String
    )=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.editPhotoInfo(id, info)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun uploadProfilePicture(
        body: RequestBody
    )=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.uploadProfilePicture(body)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun addMeasurement(
        body: MeasurementValues
    )=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.addMeasurement(body)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }



    var theClientsList = netWorkLiveData.switchMap {

        if(it){
            try{
                viewModelScope.launch {
                    val r = authRequestInterface.getAllClients()
                    onSuccessStateFlow(r)
                }
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState.asLiveData()
        }
        else{
            onNetworkStateFlow()
            uiState.asLiveData()
        }

    }


    val allPhotos  = netWorkLiveData.switchMap{
        if(it){
            try{
                viewModelScope.launch {
                    val request = authRequestInterface.getAllPhoto()
                    onSuccessStateFlow(request)
                }
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState.asLiveData()
        }
        else{
            badNetworkServiceLiveData
        }

    }
    val photos = netWorkStateFlow.flatMapLatest{
        if (it) {
            try {
                val request = authRequestInterface.getAllPhoto()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }

    }
    
    @ExperimentalCoroutinesApi
    override suspend fun getAllPhoto()= netWorkStateFlow.flatMapLatest{
        if (it) {
            try {
                val request = authRequestInterface.getAllPhoto()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }

    }


    val getClientStateFlow = netWorkStateFlow.flatMapLatest{
        if (it) {
            try {
                val request = authRequestInterface.getAllClients()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }

    }

    @ExperimentalCoroutinesApi
    override suspend fun deleteMeasurements(
        id: String
    ) = netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.deleteMeasurement(id)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun editMeasurement(
        measurementValues: MeasurementValues
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.editMeasurement(measurementValues)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }



    @ExperimentalCoroutinesApi
    override suspend fun verifyPayment(
        reference: String
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.verifyPayment(reference)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

//
//    override fun addPhoto(map: HashMap<String, RequestBody>): LiveData<ServicesResponseWrapper<ParentData>> {
//        val request = authRequestInterface.addPhoto(map)
//        return enqueueRequest(request)
//    }

//    override fun getAllPhoto(): LiveData<ServicesResponseWrapper<ParentData>> {
//
//        val request = authRequestInterface.getAllPhoto()
//        return enqueueRequest<PhotoList>(request)
//    }

    override fun deleteMedia(
        id: String?
    )=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.deleteMedia(id)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    val articles =netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.getAllArticles()
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    override fun getAllArticles()=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.getAllArticles()
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }


    var videos = netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getVideos()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }
    @ExperimentalCoroutinesApi
    override suspend fun getVideos()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getVideos()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }


    @ExperimentalCoroutinesApi
    override suspend fun getArticles()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getArticles()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    var measurementTypes = netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getMeasurementTypes()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getMeasurementTypes()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getMeasurementTypes()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }


    @ExperimentalCoroutinesApi
    override suspend fun addDeliveryAddress(
        clientId: String?,
        deliveryAddress: String?
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.addDeliveryAddress(clientId, deliveryAddress)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }




    @ExperimentalCoroutinesApi
    override suspend fun addCard(
        email: String?,
        amount: String?
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.addCard(email, amount)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }



    @ExperimentalCoroutinesApi
    override suspend fun getAllClients()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getAllClients()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }





    @ExperimentalCoroutinesApi
    override fun getAllAddress(
        clientId: String
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getAllAddress(clientId)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    var userDetails = netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getUserDetails()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }
    @ExperimentalCoroutinesApi
    override suspend fun getUserDetails()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getUserDetails()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }



    override fun getAllVideos()=netWorkLiveData.switchMap{
        if (it) {
            val request = authRequestInterface.getAllVideos()
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }


    override fun uploadProfilePicture(
        body: MultipartBody.Part
    )=netWorkLiveData.switchMap {
        if (it) {
            val request = authRequestInterface.uploadProfilePicture(body)
            enqueueRequest(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            badNetworkServiceLiveData
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun changePassword(
        oldPassword: String?,
        newPassword: String?
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.changePassword(oldPassword, newPassword)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override  fun getAllMeasurements(
        clientId: String
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getAllMeasurements(clientId)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }


    @ExperimentalCoroutinesApi
    override suspend fun addReviewAndRating(
        rate: Float,
        artisanId: String,
        comment: String
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.addReviewAndRating(rate, artisanId, comment)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun subscribe(
        plan: String,
        customer: String
    )= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.subscribe(plan, customer)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    val plans = netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getAllPlans()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getAllPlans()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getAllPlans()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    val subscriptions = netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getUserAllSubscriptions()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getUserAllSubscriptions()= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getUserAllSubscriptions()
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getSubscriptions(code: String)= netWorkStateFlow.flatMapLatest {
        if (it) {
            try {
                val request = authRequestInterface.getSubscriptions(code)
                onSuccessStateFlow(request)
            }
            catch (e:HttpException){
                onErrorStateFlow(e)
            }
            uiState
        } else {
            i(title, "Bad Connection")
            badNetworkState
        }
    }
}


