package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.User
import java.io.Serializable

class UserResponse(
    val status: String?,
    val message: String?,
    val data: User
) : ParentData, Serializable


