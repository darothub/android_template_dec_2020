package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData

data class Photo (
    val id: String?,
    @SerializedName("artisanId")
    val tailorID: String,
    val photo: String,
    val photoAwsDetails: AwsDetails,
    var info: String,
    val createdAt: String,
    val updatedAt: String
):ParentData

data class PhotoList (
    val photo: List<Photo>
):ParentData

data class UpdatedPhoto(
    val newPhoto:Photo
)

