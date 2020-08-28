package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import java.io.Serializable

class UserResponse(
    val status: String?,
    val message: String?,
    val data: User
) : ParentData, Serializable

//class Data(
//    var token: String?,
//    @SerializedName("user_id")
//    var userId: String,
//    var isVerified: Boolean = false
//) : Serializable{
//    var id:String? = ""
//    var firstName:String?=""
//    var lastName:String?=""
//    var otherName:String?=""
//    var gender:String?=null
//    var workshopAddress:String?=null
//    var showroomAddress:String?=null
//    var no_Employee:String?=null
//    var legalStatus:String?=null
//    var specialty:String?=null
//    var genderFocus:String?=null
//    var obiomaCert:String?="NO"
//    var visitUsMeasurement:String?="YES"
//    var acceptSelfMeasurement:String?="NO"
//    var deliveryTime:String?=null
//    var paymentOptions:String?="bank account"
//    var name_union:String?=null
//    var ward:String?=null
//    var lga:String?=null
//    var state:String?=null
//    var country:String?= " "
//    var category:String?=""
//    var phoneNumber:String?=""
//    var email:String?=""
//}

