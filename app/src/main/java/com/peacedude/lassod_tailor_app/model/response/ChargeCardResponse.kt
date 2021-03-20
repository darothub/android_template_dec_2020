package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class ChargeCardResponse(
    val status: Boolean,
    val message: String
) : Serializable, ParentData
