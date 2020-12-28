package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class SubscriptionResponse <T>(
    val status: Boolean,
    val message: String,
    val data: T,
    val meta: Meta
):ParentData, Serializable

data class SubscriptionData (
    val subscriptions: List<Subscription>,
    val integration: Long,
    val domain: String,
    val name: String,
    @SerializedName("plan_code")
    val planCode: String,
    val description: String,
    val amount: Long,
    val interval: String,
    val invoiceLimit: Long,
    val sendInvoices: Boolean,
    val sendSMS: Boolean,
    val hostedPage: Boolean,
    val hostedPageURL: Any? = null,
    val hostedPageSummary: Any? = null,
    val currency: String,
    val migrate: Boolean,
    val id: Long,
    val createdAt: String,
    val updatedAt: String
):ParentData, Serializable

data class Subscription (
    val customer: Long,
    val plan: Long,
    val integration: Long,
    val domain: String,
    val start: Long,
    val status: String,
    val quantity: Long,
    val amount: Long,
    val subscriptionCode: String,
    val emailToken: String,
    val authorization: Long,
    val easyCronID: String,
    val cronExpression: String,
    val nextPaymentDate: String,
    val openInvoice: Any? = null,
    val invoiceLimit: Long,
    val id: Long,
    val createdAt: String,
    val updatedAt: String
):ParentData, Serializable

data class SubscribedData (
    val id: String,
    val customerID: String,
    val tailorID: String,
    val planID: String,
    val createdAt: String,
    val updatedAt: String
):ParentData, Serializable