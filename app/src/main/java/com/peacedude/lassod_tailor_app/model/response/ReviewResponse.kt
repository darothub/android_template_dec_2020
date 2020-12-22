package com.peacedude.lassod_tailor_app.model.response

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class ReviewResponse (
    val id: String,
    val userID: String,
    val artisanID: String,
    val rate: String,
    val comment: String,
    val userDetails: UserDetail,
    val updatedAt: String,
    val createdAt: String
):Serializable, ParentData

data class UserDetail (
    val avatar: String,
    val lastName: String,
    val firstName: String,
    val otherName: String
):Serializable, ParentData