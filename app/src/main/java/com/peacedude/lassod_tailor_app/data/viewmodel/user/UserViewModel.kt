package com.peacedude.lassod_tailor_app.data.viewmodel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.helpers.safeApiCall
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import com.peacedude.lassod_tailor_app.network.user.ViewModelInterface
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.*
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRequestInterface: UserRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest,
    val context: Context,
    var mGoogleSignInClient: GoogleSignInClient
) : GeneralViewModel(retrofit, storage, context, mGoogleSignInClient), ViewModelInterface {

    private val responseLiveData by lazy{
        MutableLiveData<ServicesResponseWrapper<ParentData>>()
    }


    override val title: String by lazy {
        this.getName()
    }
    override fun registerUser(user: User?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.registerUser(
            user)
        return enqueueRequest(request, responseLiveData)
    }

    override fun registerUser(
        header: String,
        user: User?
    ): LiveData<ServicesResponseWrapper<ParentData>> {

        val request = userRequestInterface.registerUser(header,
            user)
        return enqueueRequest<User>(request, responseLiveData)
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
        return enqueueRequest<User>(request, responseLiveData)
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
        return enqueueRequest<User>(request, responseLiveData)
    }


    override fun loginWithGoogle(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.loginWithGoogle(header)
        return enqueueRequest<User>(request, responseLiveData)

    }

    override fun loginWithEmailOrPhoneNumber(
        field: String?,
        password: String?
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.loginWithEmailOrPhoneNumber(field, password)
        return enqueueRequest<User>(request, responseLiveData)
    }

    override fun resendCode(phoneNumber: String): LiveData<ServicesResponseWrapper<ParentData>>  {
        val request = userRequestInterface.resendCode(phoneNumber)
        return enqueueRequest<User>(request, responseLiveData)
    }

    override fun activateUser(phoneNumber:String, code: String): LiveData<ServicesResponseWrapper<ParentData>> {
        Log.i(title, "$phoneNumber $code")
        val request = userRequestInterface.activateUser(phoneNumber, code)
        return enqueueRequest<User>(request, responseLiveData)

    }

    fun loginUserRequest(
        emailOrPhone: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>> {
        val request = userRequestInterface.loginRequest(emailOrPhone, password)
        return enqueueRequest<User>(request, responseLiveData)

    }


}