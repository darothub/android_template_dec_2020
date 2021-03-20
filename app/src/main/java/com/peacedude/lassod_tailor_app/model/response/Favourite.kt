package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class Favourite(
    val id: String,
    val artisanID: String,
    val userID: String,
    val favorite: Boolean,
    val updatedAt: String,
    val createdAt: String,
    val user: Artisan?
) : Serializable, ParentData
