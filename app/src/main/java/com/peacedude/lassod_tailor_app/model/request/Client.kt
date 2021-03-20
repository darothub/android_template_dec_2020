package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class Client(
    var name: String,
    @SerializedName("phoneNumber")
    var phone: String,
    var email: String,
    @SerializedName("deliveryAddress")
    var deliveryAddress: String
) : Serializable,
    ParentData {
    var id: String? = null
    var state: String? = null
    var gender: String? = null
    var country: String? = null
    var tailorId: String? = null
}
data class SingleClient(val client: Client) : Serializable, ParentData
data class ClientsList(val clients: List<Client?>?) :
    Serializable,
    ParentData
