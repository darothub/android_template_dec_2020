package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class User(
    @SerializedName("firstName")
    var firstName: String?,
    @SerializedName("lastName")
    var lastName: String?,
    @SerializedName("otherName")
    var otherName:String?,
    @SerializedName("category")
    var category:String?,
    @SerializedName("phoneNumber")
    var phone:String?

) : Serializable{
    @SerializedName("password")
    var password:String?=""
    @SerializedName("user_id")
    var userId: String? = ""
    var id:String? = ""
    var token: String?= ""
    var isVerified: Boolean = false
    var gender:String?=null
    var workshopAddress:String?=null
    var showroomAddress:String?=null
    var no_Employee:String?=null
    var legalStatus:String?=null
    var specialty:String?=null
    var genderFocus:String?=null
    var obiomaCert:String?="NO"
    var visitUsMeasurement:String?="YES"
    var acceptSelfMeasurement:String?="NO"
    var deliveryTime:String?=null
    var paymentOptions:String?=""
    var name_union:String?=null
    var ward:String?=null
    var lga:String?=null
    var state:String?=null
    var country:String? = ""
    var email:String?=""
    var loggedIn:Boolean = false
}

