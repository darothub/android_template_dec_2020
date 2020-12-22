package com.peacedude.lassod_tailor_app.model.response


data class Favourite (
    val id: String,
    val artisanID: String,
    val userID: String,
    val favorite: Boolean,
    val updatedAt: String,
    val createdAt: String
)