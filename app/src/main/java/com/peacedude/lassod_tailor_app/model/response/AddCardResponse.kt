package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable


data class AddCardResponse (
    val id: Long,
    val domain: String,
    val status: String,
    val reference: String,
    val amount: Long,
    val message: Any? = null,
    val gatewayResponse: String,
    val dataPaidAt: Any? = null,
    val dataCreatedAt: String,
    val channel: String,
    val currency: String,
    val ipAddress: String,
    val metadata: String,
    val log: Any? = null,
    val fees: Any? = null,
    val feesSplit: Any? = null,
    val authorization: Authorization,
    val customer: Customer,
    val plan: Any? = null,
    val split: Authorization,
    val orderID: Any? = null,
    val paidAt: Any? = null,
    val createdAt: String,
    val requestedAmount: Long,
    val posTransactionData: Any? = null,
    val transactionDate: String,
    val planObject: Authorization,
    val subaccount: Authorization
): Serializable, ParentData

class Authorization()

data class Customer (
    val id: Long,
    val firstName: Any? = null,
    val lastName: Any? = null,
    val email: String,
    val customerCode: String,
    val phone: Any? = null,
    val metadata: Any? = null,
    val riskAction: String,
    val internationalFormatPhone: Any? = null
):Serializable, ParentData


data class AddCardWrapper<T>(
    val status: Boolean,
    val message: String,
    val data: T
):Serializable, ParentData

data class AddCardRes (
    @SerializedName("authorization_url")
    val authorizationURL: String,
    @SerializedName("access_code")
    val accessCode: String,
    val reference: String
):Serializable, ParentData