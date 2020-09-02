package com.peacedude.lassod_tailor_app.data.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.GeneralViewModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.network.auth.AuthRequestInterface
import com.peacedude.lassod_tailor_app.network.storage.StorageRequest
import com.peacedude.lassod_tailor_app.network.user.UserRequestInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRequestInterface: AuthRequestInterface,
    override var retrofit: Retrofit,
    val storage: StorageRequest
) : GeneralViewModel(retrofit,storage) {

    fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>> {
        val responseLiveData = MutableLiveData<ServicesResponseWrapper<ParentData>>()
        responseLiveData.value = ServicesResponseWrapper.Loading(
            null,
            "Loading..."
        )
        val request = authRequestInterface.getUserData(header)
        request.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onFailureResponse(responseLiveData, t)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onResponseTask(response as Response<ParentData>, responseLiveData)
            }

        })
        return responseLiveData
    }

}