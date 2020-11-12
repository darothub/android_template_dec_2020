package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.response.Metadatas
import java.io.Serializable

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
    val metadata: Metadatas,
    val location: String,
    val etag: String
): Serializable, ParentData

