package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

class UserResponse (
    val status:String?,
    val message: String?,
    val data:Data
): ParentData, Serializable

class Data (token:String?)


