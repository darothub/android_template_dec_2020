package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.AwsDetails
import java.io.Serializable

data class User(
    var token: String? = null,
    @SerializedName("role")
    var role: String? = null
) : Serializable, ParentData {
    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("otherName")
    var otherName: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("phoneNumber")
    var phone: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("user_id")
    var userId: String? = null
    var id: String? = null
    var isVerified: Boolean? = null
    var gender: String? = null
    var avatar:String? = null
    @SerializedName("deliveryAddress")
    var deliveryAddress: UserAddress? = null
    @SerializedName("workshopAddress")
    var workshopUserAddress: UserAddress? = null
    @SerializedName("showroomAddress")
    var showroomUserAddress: UserAddress? = null
    var noOfEmployees: Int? = null
    var legalStatus: String? = null
    var specialty: ArrayList<String>? = null
    var genderFocus: ArrayList<String>? = null
    var obiomaCert: String? = null
    var visitUsMeasurement: Boolean? = null
    var acceptSelfMeasurement: Boolean? = null
    var deliveryTimePeriod: String? = null
    var deliveryTimeNo: Int? = null
    var paymentOptions: List<String>?  = null
    var paymentTerms: List<String>? = null
    var unionName: String? = null
    var unionWard: String? = null
    var unionLga: String? = null
    var unionState: String? = null
    var country: String? = null
    var email: String? = null
    var loggedIn: Boolean = false
    var imageUrl: String? = null

}
data class UserAddress(var street:String?, var city:String?, var state:String?):Serializable



data class UploadImageClass (
    val id: String,
    val firstName: String,
    val lastName: String,
    val otherName: Any? = null,
    val category: String,
    val phoneNumber: Any? = null,
    val email: String,
    val password: String,
    val token: String,
    val code: Any? = null,
    val authType: Any? = null,
    val isVerified: Boolean,
    val isAdmin: Boolean,
    val isBlocked: Boolean,
    val isSubscribed: Any? = null,
    val gender: Any? = null,
    val bio: Any? = null,
    val avatar: String,
    val avatarAwsDetails: AwsDetails,
    val unionName: Any? = null,
    val unionWard: Any? = null,
    val unionLGA: Any? = null,
    val unionState: Any? = null,
    val country: Any? = null,
    val workshopAddress: Any? = null,
    val showroomAddress: Any? = null,
    val noOfEmployees: Any? = null,
    val legalStatus: Any? = null,
    val specialty: List<String>,
    val genderFocus: List<String>,
    val obiomaCERT: Any? = null,
    val visitUsMeasurement: Boolean,
    val acceptSelfMeasurement: Boolean,
    val deliveryTimePeriod: String,
    val deliveryTimeNo: Long,
    val paymentTerms: Any? = null,
    val subscriptions: Any? = null,
    val paymentOptions: Any? = null,
    val createdAt: String,
    val updatedAt: String
):ParentData



