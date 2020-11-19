package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class AddressData (
    val address: Address
):Serializable, ParentData

data class Address (
    val id: String,
    val clientID: String,
    val deliveryAddress: String,
    val updatedAt: String,
    val createdAt: String
):Serializable, ParentData