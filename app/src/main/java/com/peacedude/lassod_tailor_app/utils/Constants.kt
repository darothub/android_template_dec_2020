package com.peacedude.lassod_tailor_app.utils

const val BASE_URL_STAGING= "https://obioma-staging.herokuapp.com/api/v1/"
const val BASE_URL  = ""
const val loggedInUserKey = "loggedInUser"
const val bearer = "Bearer"
const val profileDataKey = "profileData"
data class RecyclerItem(val text: String, val selected: Boolean = false)