package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class CommonMediaClass(
    val id: String,
    val tailorID: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val videoURL: String? = null,
    val isBlacklisted: Boolean = false,
    val body: String? = null,
    val articleImage: String? = null
) : Serializable, ParentData
