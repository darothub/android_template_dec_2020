package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
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
    var workshopUserAddress: UserAddress? = null
    var showroomUserAddress: UserAddress? = null
    var noOfEmployees: Int? = 0
    var legalStatus: String? = null
    var specialty: ArrayList<String>? = null
    var genderFocus: ArrayList<String>? = null
    var obiomaCert: String? = null
    var visitUsMeasurement: Boolean = false
    var acceptSelfMeasurement: Boolean = false
    var deliveryTimePeriod: String? = null
    var deliveryTimeNo: Int? = 0
    var paymentOptions: String? = null
    var paymentTerms: ArrayList<String>? = null
    var unionName: String? = null
    var unionWard: String? = null
    var unionLga: String? = null
    var unionState: String? = null
    var country: String? = null
    var email: String? = null
    var loggedIn: Boolean? = null
    var imageUrl: String? = null

}
data class UserAddress(var street:String?, var city:String?, var state:String?):Serializable


