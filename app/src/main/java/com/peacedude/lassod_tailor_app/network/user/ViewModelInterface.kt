package com.peacedude.lassod_tailor_app.network.user

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

interface ViewModelInterface  {
    var lastFragmentId:Int
    var currentUser: User?
    var saveUser:ArrayList<String>
    var saveClient:ArrayList<String>
    var profileData:User?
    var header:String?
    var newClient:Client?
    var lastLoginForm:String?
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
    fun getUserData(): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun updateUserData(user: User): LiveData<ServicesResponseWrapper<ParentData>>{
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
    fun editClient(header: String?, client: Client):LiveData<ServicesResponseWrapper<ParentData>>{
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

    fun getAllClient(): LiveData<ServicesResponseWrapper<ParentData>> {
        TODO()
    }
    fun deleteClient(header:String?, id: String?): LiveData<ServicesResponseWrapper<ParentData>>{
        TODO()
    }
    fun addPhoto(body: RequestBody): LiveData<ServicesResponseWrapper<ParentData>> = TODO()
    fun addPhoto(photo:List<MultipartBody.Part>?): LiveData<ServicesResponseWrapper<ParentData>> = TODO()

    fun uploadProfilePicture(header: String?, body: MultipartBody.Part): LiveData<ServicesResponseWrapper<ParentData>>  = TODO()

    fun uploadProfilePicture(body: RequestBody): LiveData<ServicesResponseWrapper<ParentData>>  = TODO()

    fun addMeasurement(header: String?, body: MeasurementValues): LiveData<ServicesResponseWrapper<ParentData>>  = TODO()

    fun getAllPhoto(header: String?): LiveData<ServicesResponseWrapper<ParentData>> = TODO()

    fun deleteMedia(header: String?, id: String?): LiveData<ServicesResponseWrapper<ParentData>> = TODO()

    fun getAllVideos(header: String?): LiveData<ServicesResponseWrapper<ParentData>> = TODO()

    fun getAllArticles(header: String?): LiveData<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getVideos(header: String?): Flow<ServicesResponseWrapper<ParentData>>  = TODO()

    suspend fun getArticles(header: String?): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getMeasurementTypes(header: String?): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun addDeliveryAddress(
        header: String?,
        clientId:String?,
        deliveryAddress:String?
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun addCard(
        header: String?,
        email:String?,
        amount:String?
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun verifyPayment(
        header: String?,
        reference:String
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()


    suspend fun chargeCard(
        email:String?,
        amount:String?,
        authorizationCode: String?
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()


    fun getAllAddress(
        header:String?,
        clientId:String
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()


    suspend fun getUserDetails( header: String): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun changePassword(
        header:String?,
        oldPassword:String?,
        newPassword:String?
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

     fun getAllMeasurements(
        clientId: String
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()


    suspend fun deleteMeasurements(
        header: String?,
        id: String
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()


    suspend fun editMeasurement(
        header: String?,
        measurementValues: MeasurementValues
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun searchArtisan(
        keyword: String?,
        location: String?,
        specialty: String?,
        category: String?,
        page: Long?,
        size: Long?
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getAllPhoto(): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    fun addPhoto(map: HashMap<String, RequestBody>):LiveData<ServicesResponseWrapper<ParentData>> = TODO()
     suspend fun addReviewAndRating(
        rate: Float,
        artisanId: String,
        comment: String
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    fun editPhotoInfo(id: String, info: String):LiveData<ServicesResponseWrapper<ParentData>> = TODO()
    suspend fun addFavourite(artisanId: String): Flow<ServicesResponseWrapper<ParentData>> = TODO()
    suspend fun addReviewAndRating(
        rate:String?,
        artisanId:String?,
        comment:String?
    ): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getReviews(artisanId:String?): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun removeReview(id:String?): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getFavourites(): Flow<ServicesResponseWrapper<ParentData>> = TODO()
    suspend fun removeFavourites(artisanId:String?): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getAllClients(): Flow<ServicesResponseWrapper<ParentData>> = TODO()
    suspend fun getAllPlans(): Flow<ServicesResponseWrapper<ParentData>> = TODO()
    suspend fun subscribe(plan:String, customer:String): Flow<ServicesResponseWrapper<ParentData>> = TODO()
    suspend fun getSubscriptions(code:String): Flow<ServicesResponseWrapper<ParentData>> = TODO()

    suspend fun getUserAllSubscriptions(): Flow<ServicesResponseWrapper<ParentData>> = TODO()

}