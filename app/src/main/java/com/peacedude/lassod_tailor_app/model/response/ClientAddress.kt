package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class AddressData(
    val address: ClientAddress
) : Serializable, ParentData

data class DeliveryAddress(
    val deliveryAddress: List<ClientAddress>
) : Serializable, ParentData

data class ClientAddress(
    val id: String,
    val clientId: String,
    val deliveryAddress: String,
    val updatedAt: String,
    val createdAt: String
) : Serializable, ParentData
