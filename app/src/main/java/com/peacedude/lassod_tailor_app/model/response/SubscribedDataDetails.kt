package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class SubscribedDataDetails(
    val invoices: List<Any?>,
    val domain: String,
    val start: Long,
    val status: String,
    val quantity: Long,
    val amount: Long,
    @SerializedName("subscription_code")
    val subscriptionCode: String,
    val emailToken: String,
    val easyCronID: String,
    val cronExpression: String,
    @SerializedName("next_payment_date")
    val nextPaymentDate: String,
    val openInvoice: Any? = null,
    val invoiceLimit: Long,
    val id: Long,
    val customer: Customer,
    val plan: Plan,
    val integration: Long,
    val authorization: Long,
    val createdAt: String,
    val updatedAt: String
) : ParentData, Serializable

data class Plan(
    val subscriptions: List<Any?>,
    val pages: List<Any?>,
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
    val integration: Long,
    val createdAt: String,
    val updatedAt: String
) : ParentData, Serializable
