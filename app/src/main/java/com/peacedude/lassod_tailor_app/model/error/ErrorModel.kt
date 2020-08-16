package com.peacedude.lassod_tailor_app.model.error

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

class ErrorModel(
    val status:String?,
    val errors:List<String>?
):ParentData, Serializable
