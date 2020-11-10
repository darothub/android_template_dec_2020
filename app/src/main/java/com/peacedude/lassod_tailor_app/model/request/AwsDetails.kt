package com.peacedude.lassod_tailor_app.model.request

import com.peacedude.lassod_tailor_app.model.parent.ParentData

data class AwsDetails (
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
): ParentData

data class Metadata (
    val fieldName: String
): ParentData