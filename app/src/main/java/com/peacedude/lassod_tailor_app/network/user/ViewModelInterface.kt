package com.peacedude.lassod_tailor_app.network.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.ServicesResponseWrapper
import com.peacedude.lassod_tailor_app.utils.loggedInUserKey
import com.peacedude.lassod_tailor_app.utils.profileDataKey

interface ViewModelInterface {
    var currentUser: User?
    var saveUser:ArrayList<String>
    var profileData:User?

    fun registerUser(user: User?):LiveData<ServicesResponseWrapper<ParentData>> {
        TODO()
    }
    fun activateUser(phoneNumber:String, code: String):LiveData<ServicesResponseWrapper<ParentData>>{
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

    fun getUserData(header:String): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun updateUserData(header: String, user: User): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun forgetPassword(email:String): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
}