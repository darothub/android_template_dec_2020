package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

class Client(
    val name: String,
    @SerializedName("phoneNumber")
    val phone: String,
    val email: String,
    @SerializedName("deliveryAddress")
    val deliveryAddress: String
) : Serializable,
    ParentData {
    var id: String? = null
    var state: String? = null
    var gender: String? = null
    var country: String? = null
    var tailorId:String?=null
}
data class SingleClient(val client:Client): Serializable, ParentData
data class ClientsList(val clients:List<Client?>?): Serializable,
    ParentData