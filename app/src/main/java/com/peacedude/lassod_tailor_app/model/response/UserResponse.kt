package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import java.io.Serializable

class UserResponse<T>(
    val status: Int?,
    val message: String?,
    val error: List<String>?,
    val data: T
) : ParentData, Serializable





