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
    var workshopAddress: String? = null
    var showroomAddress: String? = null
    var no_Employee: String? = null
    var legalStatus: String? = null
    var specialty: String? = null
    var genderFocus: String? = null
    var obiomaCert: String? = null
    var visitUsMeasurement: String? = null
    var acceptSelfMeasurement: String? = null
    var deliveryTime: String? = null
    var paymentOptions: String? = null
    var name_union: String? = null
    var ward: String? = null
    var lga: String? = null
    var state: String? = null
    var country: String? = null
    var email: String? = null
    var loggedIn: Boolean? = null
    var imageUrl: String? = null

}

