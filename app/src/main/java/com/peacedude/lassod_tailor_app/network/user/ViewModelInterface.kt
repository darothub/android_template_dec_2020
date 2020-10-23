package com.peacedude.lassod_tailor_app.network.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.peacedude.lassod_tailor_app.utils.profileDataKey

interface ViewModelInterface {
    var lastFragmentId:Int?
    var currentUser: User?
    var saveUser:ArrayList<String>
    var saveClient:ArrayList<String>
    var profileData:User?
    var header:String?
    var newClient:Client?
    fun activateUser(phoneNumber:String, code: String):LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun registerUser(user: User?):LiveData<ServicesResponseWrapper<ParentData>> {
        TODO()
    }
    fun registerUser(header:String, user: User?):LiveData<ServicesResponseWrapper<ParentData>> {
        TODO()
    }
    fun registerUser(
        firstName: String,
        lastName: String,
        otherName: String,
        category: String,
        phoneNumber: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun registerUserWithEmail(
        category: String,
        email: String,
        password: String
    ): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }

    fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun updateUserData(header: String, user: User): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun forgetPassword(email:String): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun resetPassword(header: String?, password: String?, cpasswod:String?): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun addClient(header: String?, client: Client):LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }

    fun loginWithGoogle(header: String?): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun loginWithEmailOrPhoneNumber(field:String?, password: String?): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }

    fun resendCode(phoneNumber: String): LiveData<ServicesResponseWrapper<ParentData>> {
        TODO()
    }

    fun getAllClient(header: String?): LiveData<ServicesResponseWrapper<ParentData>> {
        TODO()
    }
}