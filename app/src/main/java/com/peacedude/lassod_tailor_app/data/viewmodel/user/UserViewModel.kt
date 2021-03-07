package com.peacedude.lassod_tailor_app.data.viewmodel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.helpers.SingleLiveEvent
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.i
import com.peacedude.lassod_tailor_app.helpers.safeApiCall
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import retrofit2.*
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRequestInterface: UserRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest,
    val context: Context,
    var mGoogleSignInClient: GoogleSignInClient
) : GeneralViewModel(retrofit, storage, context, mGoogleSignInClient), ViewModelInterface {


    override val title: String by lazy {
        this.getName()
    }
    override fun registerUser(user: User?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.registerUser(
            user)
        return enqueueRequest(request)
    }

    override fun registerUser(
        header: String,
        user: User?
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = userRequestInterface.registerUser(header,
            user)
        return enqueueRequest<User>(request)
    }


    override fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String


    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = userRequestInterface.registerUser(
            firstName,
            lastName,
            otherName,
            category,
            phoneNumber,
            password
        )
        return enqueueRequest<User>(request)
    }

    override fun registerUserWithEmail(
        category: String,
        email: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.registerUserWithEmail(
            category,
            email,
            password
        )
        return enqueueRequest<User>(request)
    }


    override fun loginWithGoogle(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.loginWithGoogle(header)
        return enqueueRequest<User>(request)

    }



    override fun loginWithEmailOrPhoneNumber(
        field: String?,
        password: String?
    ) = netWorkLiveData.switchMap {
        if (it) {
            val request = userRequestInterface.loginWithEmailOrPhoneNumber(field, password)
            enqueueRequest<User>(request)
            responseObserver
        } else {
            i(title, "Bad Connection")
            responseObserver.postValue(ServicesResponseWrapper.Network(502, "Bad connection"))
            responseObserver
        }

    }

    override fun resendCode(phoneNumber: String): LiveData<ServicesResponseWrapper<ParentData>>  {
        val request = userRequestInterface.resendCode(phoneNumber)
        return enqueueRequest<User>(request)
    }

    override fun activateUser(phoneNumber:String, code: String): LiveData<ServicesResponseWrapper<ParentData>> {
        Log.i(title, "$phoneNumber $code")
        val request = userRequestInterface.activateUser(phoneNumber, code)
        return enqueueRequest<User>(request)

    }

    fun loginUserRequest(
        emailOrPhone: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.loginRequest(emailOrPhone, password)
        return enqueueRequest<User>(request)

    }

    @ExperimentalCoroutinesApi
    override suspend fun searchArtisan(
        keyword: String?,
        location: String?,
        specialty: String?,
        category: String?,
        page: Long?,
        size: Long?
    ): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = userRequestInterface.searchArtisan(keyword, location, specialty, category, page, size)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun addFavourite(artisanId: String): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = userRequestInterface.addFavourite(artisanId)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getReviews(artisanId: String?): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = userRequestInterface.getReviews(artisanId)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }
    @ExperimentalCoroutinesApi
    override suspend fun removeReview(id: String?): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = userRequestInterface.removeReview(id)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
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
            val request = userRequestInterface.addReviewAndRating(rate, artisanId, comment)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getFavourites(): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = userRequestInterface.getFavourites()
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun removeFavourites(artisanId: String?): Flow<ServicesResponseWrapper<ParentData>> = channelFlow {

        try {
            val request = userRequestInterface.removeFavourites(artisanId)
            onSuccessFlowResponse(request)
        }
        catch (e:HttpException){
            onErrorFlowResponse(e)
        }
    }
}