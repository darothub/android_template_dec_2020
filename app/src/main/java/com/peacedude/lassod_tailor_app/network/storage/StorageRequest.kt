package com.peacedude.lassod_tailor_app.network.storage

import com.peacedude.lassod_tailor_app.model.request.User

interface StorageRequest {
    fun clearData()
    fun <T>saveData(user: T?, key:String):ArrayList<String>
    fun <T>keepData(user: T?, key:String)
    fun getUserData(user:String):User?
    fun checkUser(key: String): User?
    fun <T>clearByKey(key: String):Boolean
}