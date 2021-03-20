package com.peacedude.lassod_tailor_app.model.error

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class ErrorModel(
    val status: String?,
    val code: String?,
    val message: String?,
    val errors: List<String>?
) : ParentData, Serializable
