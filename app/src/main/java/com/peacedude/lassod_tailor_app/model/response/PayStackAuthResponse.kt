package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable
import java.security.SecureRandom

data class PayStackAuthResponse(
    @SerializedName("authorization_url")
    val authorizationURL: String,
    val accessCode: String,
    val reference: String
): Serializable, ParentData {
}