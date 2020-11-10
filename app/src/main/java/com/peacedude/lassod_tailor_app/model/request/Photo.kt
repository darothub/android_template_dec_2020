package com.peacedude.lassod_tailor_app.model.request

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData

data class Photo (
    val id: String,
    @SerializedName("tailorId")
    val tailorID: String,
    val photo: String,
    val photoAwsDetails: AwsDetails,
    val info: String,
    val createdAt: String,
    val updatedAt: String
):ParentData

data class PhotoList (
    val photo: List<Photo>
):ParentData

