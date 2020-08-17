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
    var category:String?="",
    @SerializedName("phone")
    var phone:String?,
    @SerializedName("password")
    var password:String?

) : Serializable {
    var loggedIn:Boolean = false
    var message = ""
}