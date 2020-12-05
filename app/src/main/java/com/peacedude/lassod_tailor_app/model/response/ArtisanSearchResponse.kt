package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.UserAddress
import java.io.Serializable


data class ArtisanSearchResponse (
    val artisans: List<Artisan>,
    val meta: Meta
):Serializable, ParentData

data class Artisan (
    val id: String,
    val firstName: String,
    val lastName: String,
    val otherName: String,
    val category: String,
    val phoneNumber: String,
    val email: Any? = null,
    val code: String,
    val authType: String,
    val isVerified: Boolean,
    val isAdmin: Boolean,
    val isBlocked: Boolean,
    val isSubscribed: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val profile: Profile
):Serializable, ParentData

data class Profile (
    val id: String?="",
    val userID: String?="",
    val gender: String?="",
    val bio: Any? = null,
    val avatar: String?="",
    val avatarAwsDetails: Any? = null,
    val unionName: String?="",
    val unionWard: String?="",
    val unionLGA: String?="",
    val unionState: String?="",
    val country: String?="",
    val workshopAddress: UserAddress?=null,
    val showroomAddress: UserAddress?=null,
    val noOfEmployees: Long?=0,
    val rating: Any? = null,
    val legalStatus: String?="",
    val specialty: List<String>,
    val genderFocus: List<String>,
    val obiomaCERT: String?="",
    val visitUsMeasurement: Boolean=false,
    val acceptSelfMeasurement: Boolean=false,
    val deliveryTimePeriod: String?="",
    val deliveryTimeNo: Long?=0,
    val paymentTerms: List<String>,
    val subscriptions: Any? = null,
    val paymentOptions: List<String>?=null,
    val createdAt: String?="",
    val updatedAt: String?=""
):Serializable, ParentData



data class Meta (
    val totalItems: Long,
    val totalPages: Long,
    val currentPage: Long,
    val prevPage: Long,
    val nextPage: Long,
    val perPage: Long
)