package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class UploadFileResponse (
    val id: String,
    @SerializedName("tailorId")
    val tailorID: String,
    val photo: String,
    val info: String,
    val photoAwsDetails: PhotoAwsDetails,
    val createdAt: String,
    val updatedAt: String
): Serializable

data class PhotoAwsDetails (
    val fieldname: String,
    val originalname: String,
    val encoding: String,
    val mimetype: String,
    val size: Long,
    val bucket: String,
    val key: String,
    val acl: String,
    val contentType: String,
    val contentDisposition: Any? = null,
    val storageClass: String,
    val serverSideEncryption: Any? = null,
    val metadata: Metadata,
    val location: String,
    val etag: String
):Serializable

data class Metadata (
    val fieldName: String
):Serializable

data class UploadedPhoto(val the0:UploadFileResponse):ParentData, Serializable